package com.bcl.polygons;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.junit.Test;

public class PolygonTest {

	@Test
	public void equals() {
		final Point point1 = new Point(0, 0);
		final Point point2 = new Point(2, 0);
		final Point point3 = new Point(2, 2);
		final Point point4 = new Point(0, 2);

		final Point point5 = new Point(5, 5);

		final Polygon polygon1 = new Polygon(Arrays.asList(point1, point2, point3));

		// start at different point, same direction
		final Polygon polygon2 = new Polygon(Arrays.asList(point2, point3, point1));

		// reverse direction
		final Polygon polygon3 = new Polygon(Arrays.asList(point1, point3, point2));

		// quadrilateral with no crossings
		final Polygon polygon4 = new Polygon(Arrays.asList(point1, point2, point3, point4));

		// same vertices, but with a crossing
		final Polygon polygon5 = new Polygon(Arrays.asList(point1, point3, point2, point4));

		// contains a different vertex
		final Polygon polygon6 = new Polygon(Arrays.asList(point1, point2, point5));

		assertThat(polygon1.equals(polygon1), equalTo(true));
		assertThat(polygon1.equals(polygon2), equalTo(true));
		assertThat(polygon1.equals(polygon3), equalTo(true));

		assertThat(polygon4.equals(polygon5), equalTo(false));

		assertThat(polygon1.equals(polygon6), equalTo(false));

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
