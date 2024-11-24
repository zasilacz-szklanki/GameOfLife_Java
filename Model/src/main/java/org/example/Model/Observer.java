package org.example.Model;

public interface Observer<T> {
    void whenChanged(T x);
}
