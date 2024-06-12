package com.bcl.polygons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static final int SIZE = 3;
	private static final int INNER_REMOVALS = 1;
	private static final int VERTICES = 3;

	private Pane pane;

	private final List<Polygon> polygons = new ArrayList<>();
	private final List<Node> lastShapes = new ArrayList<>();

	@Override
	public void start(final Stage primaryStage) throws Exception {

		populatePolygons();

		pane = new Pane();

		drawCircles();

		primaryStage.setTitle("My JavaFX Application");
		primaryStage.setScene(new Scene(pane, 800, 600));

		final Label countLabel = new Label();
		countLabel.setLayoutX(500);
		countLabel.setLayoutY(400);
		pane.getChildren().add(countLabel);

		final AtomicInteger polygonIndexAtomic = new AtomicInteger();

		final AnimationTimer timer = new AnimationTimer() {
			private long lastUpdate = System.nanoTime();

			@Override
			public void handle(final long now) {
				if (now - lastUpdate >= 1_000_000_000 / 20
//						/ 10
				) {

					pane.getChildren().removeAll(lastShapes);
					lastShapes.clear();

					final int polygonIndex = polygonIndexAtomic.getAndIncrement();

					if (polygonIndex < polygons.size()) {

						countLabel.setText(String.valueOf(polygonIndexAtomic.get()));

						System.out.println(polygonIndex);

						final Polygon polygon = polygons.get(polygonIndex);
						System.out.println("Polygon: " + polygon);
						System.out.println("Area: " + polygon.getArea());

						// draw a circle on each vertex
						polygon.getVertices().forEach(vertex -> lastShapes.add(getCircle(vertex, Color.BLACK, 30)));

						// draw a line for each side
						polygon.getSides()
								.forEach(side -> lastShapes.add(getLine(side.getStart(), side.getEnd(), Color.BLACK)));

						pane.getChildren().addAll(lastShapes);

					}

					lastUpdate = now;
				}
			}
		};

		final Button startButton = new Button();
		startButton.setLayoutX(200);
		startButton.setLayoutY(600);
		startButton.setText("Start");
		startButton.setOnAction(e -> {
			timer.start();
		});
		pane.getChildren().add(startButton);

		final Button stopButton = new Button();
		stopButton.setLayoutX(300);
		stopButton.setLayoutY(600);
		stopButton.setText("Stop");
		stopButton.setOnAction(e -> {
			timer.stop();
		});
		pane.getChildren().add(stopButton);

		final Button resetButton = new Button();
		resetButton.setLayoutX(400);
		resetButton.setLayoutY(600);
		resetButton.setText("Reset");
		resetButton.setOnAction(e -> {
			polygonIndexAtomic.set(0);
			countLabel.setText("");
		});
		pane.getChildren().add(resetButton);

		primaryStage.setMaximized(true);
		primaryStage.show();

	}

	private final Comparator<Polygon> comparatorSize = Comparator.comparing(Polygon::getTwiceArea);
	private final Comparator<Polygon> comparatorRightAngle = Comparator.comparing(Polygon::countRightAngles);
	private final Comparator<Polygon> comparatorDistinctSides = Comparator.comparing(Polygon::countDistinctSides);

	private final Comparator<Polygon> comparator = comparatorSize.reversed()
			.thenComparing(comparatorRightAngle.reversed()).thenComparing(comparatorDistinctSides);

	private void populatePolygons() {

		final SortedSet<Point> points = new TreeSet<>();
		for (int i = 0; i < SIZE * SIZE; i++) {
			final Point point = new Point(i);
			if (validPoint(point)) {
				points.add(point);
			}
		}

		final List<List<Point>> pointLists = new ArrayList<>();
		choosePoints(pointLists, Collections.emptyList(), points);

		// work out the polygons
		final List<Polygon> tempPolygons = new ArrayList<>();
		final Set<Polygon> tempPolygonSet = new HashSet<>();

		for (final List<Point> pointList : pointLists) {
			final Polygon polygon = new Polygon(pointList);
			if (polygon.isValid()) {
				if (tempPolygonSet.add(polygon)) {
					tempPolygons.add(polygon);
				}
			}
		}

		// sort into order
		tempPolygons.sort(comparator);

		// pick off each polygon and its rotations
		while (!tempPolygons.isEmpty()) {

			final Polygon polygon = tempPolygons.remove(0);
			polygons.add(polygon);

			Polygon rotatedPolygon = polygon;
			while (true) {
				rotatedPolygon = rotatePolygon(rotatedPolygon);
				if (!tempPolygons.remove(rotatedPolygon)) {
					// no more rotations
					break;
				}
				polygons.add(rotatedPolygon);
			}

		}

		System.out.printf("%,d polygons found.\n", polygons.size());

	}

	private void choosePoints(final List<List<Point>> chosenPointLists, final List<Point> chosenPoints,
			final SortedSet<Point> availablePoints) {

		if (chosenPoints.size() == VERTICES) {
			chosenPointLists.add(chosenPoints);
		} else {
			for (final Point availablePoint : availablePoints) {
				final List<Point> nextChosenPoints = new ArrayList<>(chosenPoints);
				nextChosenPoints.add(availablePoint);

				final SortedSet<Point> nextAvailablePoints = new TreeSet<>(availablePoints);
				nextAvailablePoints.remove(availablePoint);

				// recurse
				choosePoints(chosenPointLists, nextChosenPoints, nextAvailablePoints);
			}
		}
	}

	private Polygon rotatePolygon(final Polygon polygon) {
		return new Polygon(polygon.getVertices().stream().map(this::rotate).collect(Collectors.toList()));
	}

	private Point rotate(final Point p1) {

		final int r1 = p1.getRow();
		final int c1 = p1.getColumn();

		final int r2 = c1;
		final int c2 = SIZE - 1 - r1;

		return new Point(r2, c2);
	}

	static double round(final double d) {
		return Math.round(100d * d) / 100d;
	}

	private void drawCircles() {
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				final Point point = new Point(r, c);
				if (validPoint(point)) {
					pane.getChildren().add(getCircle(point, Color.RED, 50));
				}
			}
		}
	}

	private Line getLine(final Point p1, final Point p2, final Color c) {

		final Line line = new Line();
		line.setStartX(pointToX(p1));
		line.setStartY(pointToY(p1));
		line.setEndX(pointToX(p2));
		line.setEndY(pointToY(p2));
		line.setStroke(c);
		line.setStrokeWidth(20);
		return line;

	}

	private Circle getCircle(final Point point, final Color color, final int diameter) {

		final Circle circle = new Circle();
		circle.setCenterX(cToX(point.getColumn()));
		circle.setCenterY(rToY(point.getRow()));
		circle.setRadius(diameter / 2);
		circle.setFill(color);
		return circle;
	}

	private int cToX(final int c) {
		return 50 + c * 90;
	}

	private int rToY(final int r) {
		return 50 + r * 90;
	}

	private int pointToX(final Point point) {
		return cToX(point.getColumn());
	}

	private int pointToY(final Point point) {
		return rToY(point.getRow());
	}

	static int pToR(final int p) {
		return p / SIZE;
	}

	static int pToC(final int p) {
		return p % SIZE;
	}

	public static void main(final String[] args) {
		launch(args);
	}

	private boolean validPoint(final Point point) {
		final int boundary = (SIZE - INNER_REMOVALS) / 2;
		final int r = point.getRow();
		final int c = point.getColumn();
		return r < boundary || SIZE - 1 - r < boundary || c < boundary || SIZE - 1 - c < boundary;
	}

}
