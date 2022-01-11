package com.example.producerConsumer.Model.Machine;

import com.example.producerConsumer.Configuration.Controller;
import com.example.producerConsumer.Model.DataBase.DataBase;
import com.example.producerConsumer.Model.Product.Product;
import com.example.producerConsumer.Model.QueueLine.QueueLine;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.Design;
import com.example.producerConsumer.Services.ResponseService;
import com.example.producerConsumer.Services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class Machine extends Thread {
    public String name;
    private Product product;
    private String color;
    private ArrayList<QueueLine> observers;
    private String nextQ;
    private long wait;

    @Autowired
    private WebSocketService webSocketService;

    private void notifyFrontEnd(){
        webSocketService.sendMessage("change");
    }

    public Machine(String name, long wait) {
        this.name = name;
        this.wait = wait;
        observers = new ArrayList<>();
        System.out.println("Machine " + this.name + "'s wait time =" + this.wait);

    }

    @Override
    public void run() {
        while (!Design.check()) {
            notifyObservers();
            if (this.product != null) {
                updateColor();
                System.out.println("Machine " + this.name + " is working in product with color " + this.product.getColor());
                System.out.println("Machine " + this.name + "'s color is" + this.color);
                ResponseObject responseObject = new ResponseObject(this.name, this.color);
                ResponseService.addResponse(responseObject);
                notifyFrontEnd();
                try {
                    Controller.send(responseObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(this.wait);
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

    public void finish() {
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
        this.color = this.product.getColor();
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
