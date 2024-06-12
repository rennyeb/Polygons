package com.bcl.polygons;

import java.util.function.Supplier;

final class Cached<T> {

	private final Supplier<T> supplier;
	private T value = null;

	private Cached(final Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> Cached<T> of(final Supplier<T> supplier) {
		return new Cached<T>(supplier);
	}

	public T get() {
		if (value == null) {
			value = supplier.get();
		}
		return value;
	}
}
