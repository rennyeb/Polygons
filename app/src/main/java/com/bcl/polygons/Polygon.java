package com.bcl.polygons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Polygon {

	private final List<Point> vertices;

	private final Cached<List<Side>> sides = Cached.of(() -> {
		final List<Point> theVertices = getVertices();
		final List<Side> sides = new ArrayList<>();
		for (int i = 0; i < theVertices.size(); i++) {
			final Point vertex1 = theVertices.get(i);
			final int vertex2Index = (i + 1) % theVertices.size();
			final Point vertex2 = theVertices.get(vertex2Index);
			sides.add(new Side(vertex1, vertex2));
		}
		return sides;

	});

	private final Cached<List<AdjacentSides>> adjacentSides = Cached.of(() -> {
		// compute the adjacent sides
		final List<AdjacentSides> adjacentSides = new ArrayList<>();
		final List<Side> theSides = getSides();
		for (int i = 0; i < theSides.size(); i++) {
			final Side side1 = theSides.get(i);
			final int side2Index = (i + 1) % theSides.size();
			final Side side2 = theSides.get(side2Index);
			adjacentSides.add(new AdjacentSides(side1, side2));
		}
		return adjacentSides;

	});
	private final Cached<Set<Side>> sidesSet = Cached.of(() -> new HashSet<>(getSides()));
	private final Cached<Integer> twiceArea = Cached.of(() -> {

		int accumulator = 0;
		for (final Side side : getSides()) {
			accumulator += side.getStart().getColumn() * side.getEnd().getRow()
					- side.getStart().getRow() * side.getEnd().getColumn();
		}

		return Math.abs(accumulator);
	});

	private final Cached<Boolean> isValid = Cached.of(() -> {
		if (getAdjacentSides().stream().anyMatch(AdjacentSides::isParallel)) {
			// Check no two adjoining sides are in the same direction
			return false;
		}

		// check pairs of non-adjacent sides to check they do not overlap or intersect
		for (int i = 0; i < getSides().size(); i++) {
			final Side side1 = getSides().get(i);
			for (int j = i + 1; j < getSides().size(); j++) {
				final Side side2 = getSides().get(j);
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
	});

	public Polygon(final List<Point> vertices) {
		this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));
	}

	List<AdjacentSides> getAdjacentSides() {
		return adjacentSides.get();
	}

	List<Side> getSides() {
		return sides.get();
	}

	private Set<Side> getSidesSet() {
		return sidesSet.get();
	}

	@Override
	public int hashCode() {
		return getSidesSet().hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Polygon polygon = (Polygon) obj;
			// check if this polygon has the same sides (regardless of direction) as the
			// polygon passed in
			return getSidesSet().equals(polygon.getSidesSet());
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

	private final Cached<Long> countRightAngles = Cached
			.of(() -> getAdjacentSides().stream().filter(AdjacentSides::isRightAngle).count());

	public long countRightAngles() {
		return countRightAngles.get();
	}

	private final Cached<Integer> countDistinctSides = Cached
			.of(() -> new HashSet<>(getSides().stream().map(Side::length).collect(Collectors.toList())).size());

	public int countDistinctSides() {
		return countDistinctSides.get();
	}

	public double getArea() {
		final double twiceArea = getTwiceArea();
		return twiceArea / 2;
	}

	public int getTwiceArea() {
		return twiceArea.get();
	}

	public boolean isValid() {
		return isValid.get();
	}
}
