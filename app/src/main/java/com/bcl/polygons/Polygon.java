package com.bcl.polygons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class Polygon {

	private final List<Point> vertices;
	private List<Side> sides;
	private List<AdjacentSides> adjacentSides;

	public Polygon(final List<Point> vertices) {
		this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));

	}

	List<AdjacentSides> getAdjacentSides() {
		if (adjacentSides == null) {
			// compute the adjacent sides
			adjacentSides = new ArrayList<>();
			for (int i = 0; i < getSides().size(); i++) {
				final Side side1 = getSides().get(i);
				final int side2Index = (i + 1) % sides.size();
				final Side side2 = getSides().get(side2Index);
				adjacentSides.add(new AdjacentSides(side1, side2));
			}

		}
		return adjacentSides;
	}

	List<Side> getSides() {
		if (sides == null) {
			// compute the sides
			sides = new ArrayList<>();
			for (int i = 0; i < vertices.size(); i++) {
				final Point vertex1 = vertices.get(i);
				final int vertex2Index = (i + 1) % vertices.size();
				final Point vertex2 = vertices.get(vertex2Index);
				sides.add(new Side(vertex1, vertex2));
			}

		}
		return sides;
	}

	@Override
	public int hashCode() {
		return Objects.hash(new HashSet<>(sides));
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
		return getAdjacentSides().stream().filter(AdjacentSides::isRightAngle).count();
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
	}
}
