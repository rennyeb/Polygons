package com.bcl.polygons;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class SideTest {
	@Test
	public void nonAdjacent() {
		final Side side1 = new Side(new Point(0, 0), new Point(0, 2));
		// on a different row
		final Side side2 = new Side(new Point(1, 0), new Point(1, 1));

		assertThat(side1.isNonAdjacentTo(side2), equalTo(true));
	}

	@Test
	public void vertexShared() {
		final Side side1 = new Side(new Point(0, 0), new Point(0, 2));
// shares a vertex
		final Side side2 = new Side(new Point(0, 2), new Point(1, 1));

		assertThat(side1.isNonAdjacentTo(side2), equalTo(false));
	}

	@Test
	public void sameLine() {
		final Side side1 = new Side(new Point(0, 0), new Point(0, 2));
//same line
		final Side side2 = new Side(new Point(0, 0), new Point(0, 2));

		assertThat(side1.isNonAdjacentTo(side2), equalTo(false));
	}
}
