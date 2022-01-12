package com.example.producerConsumer.Model;

public class Originator {
    private String name;
    private String color;
    private long time;

    public void setState(String name, String color, long time) {
        this.name = name;
        this.color = color;
        this.time = time;
    }
    public Memento saveStateToMemento() {
        return new Memento(name, color, time);
    }
}
