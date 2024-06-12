package com.bcl.polygons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Side {

	private final Point start;
	private final Point end;
	private final Point direction;
	private final Set<Point> points = new HashSet<>();

	public Side(final Point start, final Point end) {
		this.start = start;
		this.end = end;
		this.direction = end.subtract(start);
		points.add(start);
		points.add(end);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(points);
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Side side = (Side) obj;
			return this.points.equals(side.points);
		} catch (final ClassCastException | NullPointerException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("[%s - %s]", start, end);
	}

	public double length() {
		final double xLen = start.getColumn() - end.getColumn();
		final double yLen = start.getRow() - end.getRow();
		final double sumSquares = xLen * xLen + yLen * yLen;
		final double dist = Math.sqrt(sumSquares);
		return MainApp.round(dist);
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public Point getDirection() {
		return direction;
	}

	public boolean isNonAdjacentTo(final Side side) {
		// check how many distinct vertices there are
		return new HashSet<>(Arrays.asList(start, end, side.start, side.end)).size() == 4;
	}

	public boolean projectionContains(final Point point) {
		return (end.getRow() - start.getRow()) * (point.getColumn() - start.getColumn()) ==

				(point.getRow() - start.getRow()) * (end.getColumn() - start.getColumn());
	}

	public boolean intersects(final Side side) {

		// TODO inline once working
		final int x1a = this.start.getColumn();
		final int y1a = this.start.getRow();
		final int x2a = this.end.getColumn();
		final int y2a = this.end.getRow();

		final int x1b = side.start.getColumn();
		final int y1b = side.start.getRow();
		final int x2b = side.end.getColumn();
		final int y2b = side.end.getRow();

		// get the descriminator

		final int descriminator = (x2b - x1b) * (y1a - y2a) - (x1a - x2a) * (y2b - y1b);
		if (descriminator == 0) {
			// lines are parallel, and so do not intersect. (The case that the lines overlap
			// is dealt with outside this function)
			return false;
		}

		final int dA = (y1b - y2b) * (x1a - x1b) + (x2b - x1b) * (y1a - y1b);
		final int dB = (y1a - y2a) * (x1a - x1b) + (x2a - x1a) * (y1a - y1b);

		if (descriminator > 0) {
			// check both d values are in the range [0, descriminator]
			return dA >= 0 && dA <= descriminator && dB >= 0 && dB <= descriminator;
		} else {
			// check both d values are in the range [descriminator, 0]
			return dA >= descriminator && dA < 0 && dB >= descriminator && dB <= 0;
		}
	}
}
