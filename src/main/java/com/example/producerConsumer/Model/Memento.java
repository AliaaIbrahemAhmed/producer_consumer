package com.example.producerConsumer.Model;

public class Memento {
    private String name;
    private String colorOrNumber;
    private long time;

    public Memento(String name, String colorOrNumber, long time) {
        this.name = name;
        this.colorOrNumber = colorOrNumber;
        this.time = time;
    }

    public String getName() {
        return name;
    }
    public String getColorOrNumber() {
        return colorOrNumber;
    }
    public long getTime() {
        return time;
    }
}
