package com.example.producerConsumer.Model.QueueLine;

import com.example.producerConsumer.Model.Machine.Machine;
import com.example.producerConsumer.Model.Product.Product;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.Design;
import com.example.producerConsumer.Services.ResponseService;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class QueueLine {
    private String name;
    Queue<Product> queue;
    String color;
    private Design design;

    public QueueLine(String name, Design design) {
        this.name = name;
        this.queue = new LinkedList<Product>();
        this.design = design;
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
        if (product != null) {
            queue.add(product);
            updateColor();
        }
    }

    synchronized public void updateColor() {
        if (!queue.isEmpty() && queue.peek() != null) {
            this.color = queue.peek().getColor();
        } else this.color = null;
        design.notifyFrontEnd(new ResponseObject(this.getName(), this.getColor()));
    }

    synchronized public void getNotified(Machine machine) {
        if (!queue.isEmpty()) {
            Product peek = queue.poll();
            if (peek != null) {
                synchronized (peek) {
                    machine.setProduct(peek);
                    updateColor();
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
