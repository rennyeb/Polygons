package com.bcl.polygons;

import java.util.Comparator;
import java.util.Objects;

public final class Point implements Comparable<Point> {

	private static final Comparator<Point> comparator = Comparator.comparing(Point::getRow)
			.thenComparing(Point::getColumn);

	private final int row;
	private final int column;
	
    private Cached<Integer> hashCode = Cached.of(() -> Objects.hash(getRow(), getColumn()));

	public Point(final int row, final int column) {
		this.row = row;
		this.column = column;
	}

	public Point(final int p) {
		this.row = MainApp.pToR(p);
		this.column = MainApp.pToC(p);
	}

	public Point subtract(final Point point) {
		return new Point(this.row - point.row, this.column - point.column);
	}

	@Override
	public int hashCode() {
		return hashCode.get();
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

}
