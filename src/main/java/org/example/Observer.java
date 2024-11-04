package org.example;

public interface Observer<T> {
    void whenChanged(T x);
}
