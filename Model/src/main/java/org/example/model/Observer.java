package org.example.model;

public interface Observer<T> {
    void whenChanged(T x);
}
