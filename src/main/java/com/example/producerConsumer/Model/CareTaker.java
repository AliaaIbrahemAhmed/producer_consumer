package com.example.producerConsumer.Model;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<Memento> mementoList = new ArrayList<>();

    public void add(Memento state) {
        mementoList.add(state);
    }
    public Memento get(int index) {
        return mementoList.get(index);
    }
    public int getLength() {
        return mementoList.size();
    }
    public List<Memento> getList() {
        return mementoList;
    }
}
