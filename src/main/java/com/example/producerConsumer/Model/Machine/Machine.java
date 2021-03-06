package com.example.producerConsumer.Model.Machine;

import com.example.producerConsumer.Configuration.Controller;
import com.example.producerConsumer.Model.DataBase.DataBase;
import com.example.producerConsumer.Model.Product.Product;
import com.example.producerConsumer.Model.QueueLine.QueueLine;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class Machine extends Thread {
    public String name;
    private Product product;
    private String color;
    private ArrayList<QueueLine> observers;
    private String nextQ;
    private long wait;
    Design design;
    public boolean exit = false;

    public Machine(String name, long wait, Design design) {
        this.name = name;
        this.wait = wait;
        observers = new ArrayList<>();
        System.out.println("Machine " + this.name + "'s wait time =" + this.wait);
        this.design = design;

    }

    @Override
    public void run() {
        notifyObservers();
        while (!Design.check() || this.color != null && !exit) {
            notifyObservers();
            updateColor();
            System.out.println("Machine " + name + "'s color is" + this.color + " " + System.currentTimeMillis());
            try {
                sleep(getWait());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
         design.checkEnd();
    }


    public void notifyObservers() {
        for (QueueLine queueLine : observers) {
            queueLine.getNotified(this);
            if (product != null) break;
        }
    }

    synchronized public void finish() {
        try {
            DataBase.getQueueLines().get(nextQ).addProduct(this.product);
        } catch (NullPointerException f) {
            design.terminate();
        }

        this.product = null;

    }

    public long getWait() {
        return wait;
    }

    public void setWait(long wait) {
        this.wait = wait;
    }


    public String getNextQ() {
        return nextQ;
    }

    public void setNextQ(String nextQ) {
        if (nextQ != null) {
            this.nextQ = nextQ;
        }
    }

    public void addObserver(QueueLine queueLine) {
        observers.add(queueLine);
    }


    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateColor() {
        long endTime = System.currentTimeMillis();
        if (this.product == null) {
            if (this.color != null) design.updateState(this.name, null, endTime - Design.startTime);
            this.color = null;
        } else {
            if (this.color != this.product.getColor())
                design.updateState(this.name, this.product.getColor(), endTime - Design.startTime);
            this.color = this.product.getColor();
        }
        design.notifyFrontEnd(new ResponseObject(this.name, this.getColor()));
    }

    public Product getProduct() {
        return product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<QueueLine> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<QueueLine> observers) {
        this.observers = observers;
    }


}
