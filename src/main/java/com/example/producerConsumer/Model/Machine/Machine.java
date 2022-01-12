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

    public Machine(String name, long wait, Design design) {
        this.name = name;
        this.wait = wait;
        observers = new ArrayList<>();
        System.out.println("Machine " + this.name + "'s wait time =" + this.wait);
        this.design = design;

    }

    @Override
    public void run() {
        while (!Design.check()) {
            notifyObservers();
            if (getProduct() != null) {
                updateColor();
                System.out.println("Machine " + name + " is working in product with color " + getProduct().getColor());
                System.out.println("Machine " + name + "'s color is" + getColor());
                try {
                    sleep(getWait());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }

    }


    public void notifyObservers() {
        for (QueueLine queueLine : observers) {
            queueLine.getNotified(this);
            if (product != null) break;
        }
    }

    synchronized public void finish() {
        DataBase.getQueueLines().get(nextQ).addProduct(this.product);
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
        this.nextQ = nextQ;
    }

    public void addObserver(QueueLine queueLine) {
        observers.add(queueLine);
    }


    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateColor() {
        if (this.product == null) this.color = null;
        else this.color = this.product.getColor();
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
