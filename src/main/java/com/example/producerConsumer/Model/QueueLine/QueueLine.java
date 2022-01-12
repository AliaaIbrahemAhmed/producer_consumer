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
    private int number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addProduct(Product product) {
        if (product != null) {
            queue.add(product);
        }
    }

    synchronized public void updateNumber() {
        design.notifyFrontEnd(new ResponseObject(this.getName(), this.getNumber() + ""));
    }

    synchronized public void getNotified(Machine machine) {
        System.out.println("Machine " + machine.name + " is notifying queue " + this.name + System.currentTimeMillis());
        if (!queue.isEmpty()) {
            Product peek = queue.poll();
            machine.setProduct(peek);
            this.number--;
            updateNumber();
        }
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public long getSize() {
        return this.queue.size();
    }
}
