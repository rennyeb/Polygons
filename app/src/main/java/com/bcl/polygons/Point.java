package com.bcl.polygons;

import java.util.Comparator;
import java.util.Objects;

public final class Point implements Comparable<Point> {

	private static final Comparator<Point> comparator = Comparator.comparing(Point::getRow)
			.thenComparing(Point::getColumn);

	private final int row;
	private final int column;

	public Point(final int row, final int column) {
		this.row = row;
		this.column = column;
	}

	public Point(final int p) {
		this.row = MainApp.pToR(p);
		this.column = MainApp.pToC(p);
	}

	public Point add(final Point point) {
		return new Point(this.row + point.row, this.column + point.column);
	}

	public Point subtract(final Point point) {
		return new Point(this.row - point.row, this.column - point.column);
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column);
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Point point = (Point) obj;
			return this.row == point.row && this.column == point.column;
		} catch (final ClassCastException | NullPointerException e) {
			return false;
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public int compareTo(final Point point) {
		return comparator.compare(this, point);
	}

	@Override
	public String toString() {
		return String.format("(r=%d,c=%d)", row, column);
	}

	// TODO move to line
	public boolean parallelTo(final Point point) {
		return row * point.column == point.row * column;
	}

	public boolean rightAngleTo(final Point point) {
		return row * point.row + column * point.column == 0;
	}

}
