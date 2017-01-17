package com.xpminecraft.compiler.scanner;

import java.util.function.Consumer;

@FunctionalInterface
public interface Action<T> extends Consumer<T> {
}
