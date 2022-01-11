package com.example.producerConsumer.Model.QueueLine;

import com.example.producerConsumer.Model.Machine.Machine;
import com.example.producerConsumer.Model.Product.Product;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class QueueLine {
    private String name;
    Queue<Product> queue;
    String color;

    public QueueLine(String name) {
        this.name = name;
        this.queue = new LinkedList<Product>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addProduct(Product product) {
        queue.add(product);
        // updateColor();
    }

    public void updateColor(Machine machine) {
        if (!queue.isEmpty() && queue.peek() != null) {
            this.color = queue.peek().getColor();
        }
    }

    synchronized public void getNotified(Machine machine) {
        if (!queue.isEmpty()) {
            Product peek = queue.poll();
            if (peek != null) {
                synchronized (peek) {
                    machine.setProduct(peek);
                    updateColor(machine);

                }
            }
        }

    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public long getSize() {
        return this.queue.size();
    }
}
