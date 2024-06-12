package com.bcl.polygons;

public final class Side {

	private final Point start;
	private final Point end;
	private final Point direction;

	public Side(final Point start, final Point end) {
		this.start = start;
		this.end = end;
		this.direction = end.subtract(start);
	}

	// TODO whether a line crosses another

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

}
