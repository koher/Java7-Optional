package org.koherent.util;

import java.util.NoSuchElementException;

import org.koherent.validation.Nulls;

public class Optional<T> extends Union<T, Optional.Null> {
	private static final Optional<?> EMPTY = new Optional<>(null, Null.INSTANCE);

	protected Optional(T x, Null y) {
		super(x, y);
	}

	public static <T> Optional<T> of(T value) {
		return new Optional<>(value, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> empty() {
		return (Optional<T>) EMPTY;
	}

	public static <T> Optional<T> ofNullable(T value) {
		if (value == null) {
			return empty();
		} else {
			return of(value);
		}
	}

	public boolean isPresent() {
		return is1();
	}

	public T get() throws NoSuchElementException {
		return get1();
	}

	@Override
	public Optional<T> if1(Consumer<? super T> consumer)
			throws IllegalArgumentException {
		return (Optional<T>) super.if1(consumer);
	}

	@Override
	public Optional<T> if2(Consumer<? super Null> consumer) {
		return (Optional<T>) super.if2(consumer);
	}

	public void ifPresent(Consumer<? super T> consumer)
			throws IllegalArgumentException {
		if1(consumer);
	}

	public T orElse(T other) {
		if (isPresent()) {
			return get();
		} else {
			return other;
		}
	}

	public T orElseGet(Supplier<T> supplier) throws IllegalArgumentException {
		Nulls.validate(supplier);

		if (isPresent()) {
			return get();
		} else {
			return supplier.get();
		}
	}

	public <X extends Throwable> T orElseThrow(
			Supplier<? extends X> exceptionSupplier) throws X {
		Nulls.validate(exceptionSupplier);

		if (isPresent()) {
			return get();
		} else {
			throw exceptionSupplier.get();
		}
	}

	public Optional<T> filter(Predicate<? super T> predicate) {
		Nulls.validate(predicate);

		if (isPresent() && predicate.test(get())) {
			return this;
		} else {
			return empty();
		}
	}

	public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Nulls.validate(mapper);

		if (!isPresent()) {
			return empty();
		} else {
			return Optional.ofNullable((U) mapper.apply(get()));
		}
	}

	public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Nulls.validate(mapper);

		if (!isPresent()) {
			return empty();
		} else {
			return Nulls.validate(mapper.apply(get()));
		}
	}

	public static final class Null {
		private static final Null INSTANCE = new Null();

		private Null() {
			super();
		}
	}
}
