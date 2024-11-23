package org.example;

import java.util.ArrayList;
import java.util.List;

public class ObservatorsManagement<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    protected void alarmObservers(T x) {
        for (Observer<T> observer : observers) {
            observer.whenChanged(x);
        }
    }
}

