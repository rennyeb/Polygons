package com.bcl.polygons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class Polygon {

	private final List<Point> vertices;
	private final SortedSet<Point> sortedVertices;
	private final List<Side> sides;
	private final List<AdjacentSides> adjacentSides;

	public Polygon(final List<Point> vertices) {
		this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));
		this.sortedVertices = new TreeSet<>(vertices);

		// compute the sides
		sides = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			final Point vertex1 = vertices.get(i);
			final int j = i + 1;
			final int vertex2Index = j < vertices.size() ? j : 0;
			final Point vertex2 = vertices.get(vertex2Index);
			sides.add(new Side(vertex1, vertex2));
		}

		// compute the adjacent sides
		adjacentSides = new ArrayList<>();
		for (int i = 0; i < sides.size(); i++) {
			final Side side1 = sides.get(i);
			final int j = i + 1;
			final int side2Index = j < sides.size() ? j : 0;
			final Side side2 = sides.get(side2Index);
			adjacentSides.add(new AdjacentSides(side1, side2));
		}

	}

	@Override
	public int hashCode() {
		return Objects.hash(sortedVertices);
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Polygon polygon = (Polygon) obj;
			// check if this polygon has the same sides (regardless of direction) as the
			// polygon passed in
			return new HashSet<>(sides).equals(new HashSet<>(polygon.sides));
		} catch (final ClassCastException | NullPointerException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return vertices.toString();
	}

	public List<Point> getVertices() {
		return vertices;
	}

	public long countRightAngles() {
		return adjacentSides.stream().filter(AdjacentSides::isRightAngle).count();
	}

	List<AdjacentSides> getAdjacentSides() {
		return adjacentSides;
	}

	List<Side> getSides() {
		return sides;
	}

	public int countDistinctSides() {

		final Set<Double> distances = new HashSet<>(getSides().stream().map(Side::length).collect(Collectors.toList()));

		return distances.size();
	}

	public double getArea() {
		final double twiceArea = getTwiceArea();
		return twiceArea / 2;
	}

	public int getTwiceArea() {

		final List<Side> sides = getSides();
		int accumulator = 0;
		for (final Side side : sides) {
			accumulator += side.getStart().getColumn() * side.getEnd().getRow()
					- side.getStart().getRow() * side.getEnd().getColumn();
		}

		return Math.abs(accumulator);

	}

	public boolean isValid() {

		if (adjacentSides.stream().anyMatch(AdjacentSides::isParallel)) {
			// Check no two adjoining sides are in the same direction
			return false;
		}

		// check pairs of non-adjacent sides to check they do not overlap or intersect
		for (int i = 0; i < sides.size(); i++) {
			final Side side1 = sides.get(i);
			for (int j = i + 1; j < sides.size(); j++) {
				final Side side2 = sides.get(j);
				if (side2.isNonAdjacentTo(side1)) {
					if (side1.projectionContains(side2.getStart())) {
						// overlaps
						return false;
					} else if (side1.intersects(side2)) {
						return false;
					}
				}
			}
		}

		return true;
	}
}
