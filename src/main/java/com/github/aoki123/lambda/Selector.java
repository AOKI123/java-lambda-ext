package com.github.aoki123.lambda;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * <pre>
 *     使用函数式编程来代替if else 语句
 *     example:
 *     {@code
 *     String value;
 *     if (...) {
 *         value = "1";
 *     } else {
 *         value = "2";
 *     }
 *     }
 *
 *     使用函数式如下:
 *     {@code String value = Selector.<String> ifTrue(...).get("1").orElse("2");}
 * </pre>
 *
 * @author jiahan
 * @date 2019-01-21 10:29
 */
public final class Selector<T> {

    private final boolean condition;

    private T value = null;

    private Selector(boolean condition) {
        this.condition = condition;
    }

    public static <T> Selector<T> ifTrue(boolean condition) {
        return new Selector<>(condition);
    }

    public Selector<T> get(T value) {
        if (condition) {
            this.value = value;
        }
        return this;
    }

    public Selector<T> get(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (condition) {
            this.value = supplier.get();
        }
        return this;
    }

    public T orElse(T other) {
        if (!condition) {
            return other;
        }
        return value;
    }

    public T orElseGet(Supplier<T> other) {
        if (!condition) {
            return other.get();
        }
        return value;
    }

}
