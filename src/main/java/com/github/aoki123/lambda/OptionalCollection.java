package com.github.aoki123.lambda;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Collection类的非空操作类, 作用和用法类似于{@link Optional},只不过判断空使用的是判断集合非空的方法
 *
 * @author jiahan
 * @date 2019-07-09 16:01
 */
public final class OptionalCollection<T> {

    /**
     * Common instance for {@code empty()}.
     */
    private static final OptionalCollection<?> EMPTY = new OptionalCollection<>();

    /**
     * If non-null && not empty, the value; if null or empty, indicates no value is present
     */
    private final Collection<T> value;

    private OptionalCollection() {
        this.value = null;
    }

    private OptionalCollection(Collection<T> value) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException("value cannot be empty!");
        }
        this.value = value;
    }

    public static <T> OptionalCollection<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalCollection<T> t = (OptionalCollection<T>) EMPTY;
        return t;
    }

    public static <T> OptionalCollection<T> of(Collection<T> value) {
        return new OptionalCollection<>(value);
    }

    public static <T> OptionalCollection<T> ofNullable(Collection<T> value) {
        return isEmpty(value) ? empty() : of(value);
    }

    public Collection<T> get() {
        if (isEmpty(value)) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return !isEmpty(value);
    }

    public void ifPresent(Consumer<Collection<T>> consumer) {
        if (!isEmpty(value)) {
            consumer.accept(value);
        }
    }

    public OptionalCollection<T> filter(Predicate<Collection<T>> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        }
        return predicate.test(value) ? this : empty();
    }

    public <U> Optional<U> map(Function<Collection<T>, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(value));
    }

    public OptionalCollection<T> flatMap(Function<Collection<T>, OptionalCollection<T>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        }
        return Objects.requireNonNull(mapper.apply(value));
    }

    public Collection<T> orElse(Collection<T> other) {
        return isPresent() ? value : other;
    }

    public Collection<T> orElseGet(Supplier<Collection<T>> other) {
        return isPresent() ? value : other.get();
    }

    public <X extends Throwable> Collection<T> orElseThrow(
            Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    private static boolean isEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }
}
