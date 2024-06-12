package com.bcl.polygons;

public final class AdjacentSides {

	// the end of side1 is the start of side2?
	private final Side side1;
	private final Side side2;

	public AdjacentSides(final Side side1, final Side side2) {
		this.side1 = side1;
		this.side2 = side2;
	}

	@Override
	public String toString() {
		return String.format("%s+%s", side1, side2);
	}

	public boolean isParallel() {
		return side1.getDirection().getRow() * side2.getDirection().getColumn() == side2.getDirection().getRow()
				* side1.getDirection().getColumn();
	}

	public boolean isRightAngle() {
		return side1.getDirection().getRow() * side2.getDirection().getRow()
				+ side1.getDirection().getColumn() * side2.getDirection().getColumn() == 0;

	}

}
