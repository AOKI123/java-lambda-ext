package com.github.aoki123.lambda;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * String类的非空操作类, 作用和用法类似于{@link Optional},只不过判断空使用的是判断字符串非空的方法
 *
 * @author jiahan
 * @date 2019-01-15 16:52
 * @see Optional
 */
public final class OptionalString {

    /**
     * Common instance for {@code empty()}.
     */
    private static final OptionalString EMPTY = new OptionalString();

    /**
     * If non-null && not empty, the value; if null or empty, indicates no value is present
     */
    private final String value;

    private OptionalString() {
        this.value = null;
    }

    private OptionalString(String value) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException("value cannot be empty!");
        }
        this.value = value;
    }

    public static OptionalString empty() {
        return EMPTY;
    }

    public static OptionalString of(String value) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException("value cannot be empty!");
        }
        return new OptionalString(value);
    }

    public static OptionalString ofNullable(String value) {
        return isEmpty(value) ? empty() : of(value);
    }

    public String get() {
        if (isEmpty(value)) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return !isEmpty(value);
    }

    public void ifPresent(Consumer<String> consumer) {
        if (!isEmpty(value)) {
            consumer.accept(value);
        }
    }

    public OptionalString filter(Predicate<String> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        }
        return predicate.test(value) ? this : empty();
    }

    public <U> Optional<U> map(Function<String, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(value));
    }

    public OptionalString flatMap(Function<String, OptionalString> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        }
        return Objects.requireNonNull(mapper.apply(value));
    }

    public String orElse(String other) {
        return isPresent() ? value : other;
    }

    public String orElseGet(Supplier<String> other) {
        return isPresent() ? value : other.get();
    }

    public <X extends Throwable> String orElseThrow(
            Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    private static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }
}
