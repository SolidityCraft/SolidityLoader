package io.soliditycraft.solidityloader.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class SolUtils {

    @Contract("_ -> new")
    public static <T> @NotNull List<T> toList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> @Nullable T find(@NotNull List<T> list, Function<T, Boolean> function) {
        for (T t : list) {
            if (function.apply(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T, R> @NotNull List<R> map(@NotNull List<T> list, Function<T, R> function) {
        List<R> mappedList = new ArrayList<>();
        for (T t : list) {
            mappedList.add(function.apply(t));
        }
        return mappedList;
    }

    public static <T> @NotNull List<T> filter(@NotNull List<T> list, Function<T, Boolean> function) {
        List<T> mappedList = new ArrayList<>();
        for (T t : list) {
            if (function.apply(t)) {
                mappedList.add(t);
            }
        }
        return mappedList;
    }

    @Contract(value = " -> new", pure = true)
    public static <T> @NotNull List<T> createEmptyList() {
        return new ArrayList<>();
    }

    @Contract(value = " -> new", pure = true)
    public static <K, V> @NotNull Map<K, V> createEmptyMap() {
        return new HashMap<>();
    }
}
