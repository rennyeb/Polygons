package com.bcl.polygons;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class SideTest {
	@Test
	public void isNonAdjacentTo() {
		final Side side1 = new Side(new Point(0, 0), new Point(0, 2));
		// on a different row
		final Side side2 = new Side(new Point(1, 0), new Point(1, 1));

		assertThat(side1.isNonAdjacentTo(side2), equalTo(true));

		// shares a vertex
		final Side side3 = new Side(new Point(0, 2), new Point(1, 1));

		assertThat(side1.isNonAdjacentTo(side3), equalTo(false));

		// same side as side1
		final Side side4 = new Side(new Point(0, 0), new Point(0, 2));
		assertThat(side1.equals(side4), equalTo(true));

		assertThat(side1.isNonAdjacentTo(side4), equalTo(false));
	}

	@Test
	public void equals() {
		final Side side1 = new Side(new Point(0, 0), new Point(0, 2));
		final Side side2 = new Side(new Point(1, 0), new Point(1, 1));
		// reverse of side2
		final Side side3 = new Side(new Point(1, 1), new Point(1, 0));

		assertThat(side1.equals(side1), equalTo(true));
		assertThat(side1.equals(side2), equalTo(false));
		assertThat(side2.equals(side1), equalTo(false));
		assertThat(side2.equals(side3), equalTo(true));
		assertThat(side3.equals(side2), equalTo(true));

	}

	@Test
	public void projectionContents() {

		{
			// horizontal
			final Side sideHorizontal = new Side(new Point(1, 1), new Point(1, 3));

			// vertices
			assertThat(sideHorizontal.projectionContains(new Point(1, 1)), equalTo(true));
			assertThat(sideHorizontal.projectionContains(new Point(1, 3)), equalTo(true));
			// mid point
			assertThat(sideHorizontal.projectionContains(new Point(1, 2)), equalTo(true));
			// projection of line
			assertThat(sideHorizontal.projectionContains(new Point(1, 4)), equalTo(true));

			// off the line
			assertThat(sideHorizontal.projectionContains(new Point(0, 0)), equalTo(false));

		}

		{
			// vertical
			final Side sideVertical = new Side(new Point(1, 1), new Point(3, 1));

			// vertices
			assertThat(sideVertical.projectionContains(new Point(1, 1)), equalTo(true));
			assertThat(sideVertical.projectionContains(new Point(3, 1)), equalTo(true));
			// mid point
			assertThat(sideVertical.projectionContains(new Point(2, 1)), equalTo(true));
			// projection of line
			assertThat(sideVertical.projectionContains(new Point(4, 1)), equalTo(true));

			// off the line
			assertThat(sideVertical.projectionContains(new Point(0, 0)), equalTo(false));

		}

		{
			// diagonal
			final Side sideDiagona1 = new Side(new Point(1, 1), new Point(3, 3));

			// vertices
			assertThat(sideDiagona1.projectionContains(new Point(1, 1)), equalTo(true));
			assertThat(sideDiagona1.projectionContains(new Point(3, 3)), equalTo(true));
			// mid point
			assertThat(sideDiagona1.projectionContains(new Point(2, 2)), equalTo(true));
			// projection of line
			assertThat(sideDiagona1.projectionContains(new Point(4, 4)), equalTo(true));

			// off the line
			assertThat(sideDiagona1.projectionContains(new Point(1, 2)), equalTo(false));

		}

	}

}
