package org.koherent.util;

import java.util.NoSuchElementException;

import org.koherent.validation.Nulls;

public class Union<T, U> {
	private T value1;
	private U value2;

	protected Union(T value1, U value2) throws IllegalArgumentException {
		super();

		if (value1 == null) {
			this.value2 = Nulls.validate(value2);
		} else if (value2 == null) {
			this.value1 = Nulls.validate(value1);
		} else {
			throw new IllegalArgumentException(
					"Both of 'value1' and 'value2' are not null. One must be null.");
		}
	}

	public static <T, U> Union<T, U> of1(T value)
			throws IllegalArgumentException {
		return new Union<>(value, null);
	}

	public static <T, U> Union<T, U> of2(U value)
			throws IllegalArgumentException {
		return new Union<>(null, value);
	}

	public boolean is1() {
		return value1 != null;
	}

	public T get1() throws NoSuchElementException {
		if (!is1()) {
			throw new NoSuchElementException();
		}

		return value1;
	}

	public U get2() throws NoSuchElementException {
		if (is1()) {
			throw new NoSuchElementException();
		}

		return value2;
	}

	public Optional<T> get1OrEmpty() {
		return Optional.ofNullable(value1);
	}

	public Optional<U> get2OrEmpty() {
		return Optional.ofNullable(value2);
	}

	public Union<T, U> if1(Consumer<? super T> consumer)
			throws IllegalArgumentException {
		Nulls.validate(consumer);

		if (is1()) {
			consumer.accept(value1);
		}

		return this;
	}

	public Union<T, U> if2(Consumer<? super U> consumer) {
		Nulls.validate(consumer);

		if (!is1()) {
			consumer.accept(value2);
		}

		return this;
	}

	public <V, W> Union<V, W> map(Function<? super T, ? extends V> mapper1,
			Function<? super U, ? extends W> mapper2)
			throws IllegalArgumentException {
		Nulls.validate(mapper1);
		Nulls.validate(mapper2);

		if (is1()) {
			return of1((V) mapper1.apply(value1));
		} else {
			return of2((W) mapper2.apply(value2));
		}
	}

	public <V, W> Union<V, W> flatMap(Function<? super T, Union<V, W>> mapper1,
			Function<? super U, Union<V, W>> mapper2)
			throws IllegalArgumentException {
		Nulls.validate(mapper1);
		Nulls.validate(mapper2);

		if (is1()) {
			return Nulls.validate(mapper1.apply(value1));
		} else {
			return Nulls.validate(mapper2.apply(value2));
		}
	}

	public <V> V reduce(Function<? super T, ? extends V> mapper1,
			Function<? super U, ? extends V> mapper2) {
		Nulls.validate(mapper1);
		Nulls.validate(mapper2);

		if (is1()) {
			return mapper1.apply(value1);
		} else {
			return mapper2.apply(value2);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Union)) {
			return false;
		}

		Union<?, ?> union = (Union<?, ?>) o;
		if (is1()) {
			if (union.is1()) {
				return value1.equals(union.value1);
			} else {
				return value1.equals(union.value2);
			}
		} else {
			if (union.is1()) {
				return value2.equals(union.value1);
			} else {
				return value2.equals(union.value2);
			}
		}
	}

	@Override
	public int hashCode() {
		if (is1()) {
			return value1.hashCode();
		} else {
			return value2.hashCode();
		}
	}

	@Override
	public String toString() {
		if (is1()) {
			return value1.toString();
		} else {
			return value2.toString();
		}
	}
}