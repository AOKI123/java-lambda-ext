package com.github.aoki123.lambda;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Map类的非空操作类, 作用和用法类似于{@link Optional},只不过判断空使用的是判断Map非空的方法
 *
 * @author jiahan
 * @date 2019-07-09 16:01
 */
public final class OptionalMap<K, V> {

    /**
     * Common instance for {@code empty()}.
     */
    private static final OptionalMap<?, ?> EMPTY = new OptionalMap<>();

    /**
     * If non-null && not empty, the value; if null or empty, indicates no value is present
     */
    private final Map<K, V> value;

    private OptionalMap() {
        this.value = null;
    }

    private OptionalMap(Map<K, V> value) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException("value cannot be empty!");
        }
        this.value = value;
    }

    public static <K, V> OptionalMap<K, V> empty() {
        @SuppressWarnings("unchecked")
        OptionalMap<K, V> t = (OptionalMap<K, V>) EMPTY;
        return t;
    }

    public static <K, V> OptionalMap<K, V> of(Map<K, V> value) {
        return new OptionalMap<>(value);
    }

    public static <K, V> OptionalMap<K, V> ofNullable(Map<K, V> value) {
        return isEmpty(value) ? empty() : of(value);
    }

    public Map<K, V> get() {
        if (isEmpty(value)) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return !isEmpty(value);
    }

    public void ifPresent(Consumer<Map<K, V>> consumer) {
        if (!isEmpty(value)) {
            consumer.accept(value);
        }
    }

    public OptionalMap<K, V> filter(Predicate<Map<K, V>> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        }
        return predicate.test(value) ? this : empty();
    }

    public <U> Optional<U> map(Function<Map<K, V>, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(value));
    }

    public OptionalMap<K, V> flatMap(Function<Map<K, V>, OptionalMap<K, V>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        }
        return Objects.requireNonNull(mapper.apply(value));
    }

    public Map<K, V> orElse(Map<K, V> other) {
        return isPresent() ? value : other;
    }

    public Map<K, V> orElseGet(Supplier<Map<K, V>> other) {
        return isPresent() ? value : other.get();
    }

    public <X extends Throwable> Map<K, V> orElseThrow(
            Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    private static boolean isEmpty(Map<?, ?> value) {
        return value == null || value.isEmpty();
    }
}
