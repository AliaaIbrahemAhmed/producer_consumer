package com.example.producerConsumer.Services;

import com.example.producerConsumer.Configuration.Controller;
import com.example.producerConsumer.Model.*;
import com.example.producerConsumer.Model.DataBase.DataBase;
import com.example.producerConsumer.Model.Machine.Machine;
import com.example.producerConsumer.Model.Product.Product;
import com.example.producerConsumer.Model.QueueLine.QueueLine;
import com.example.producerConsumer.Model.ResponseObject;
import liquibase.pro.packaged.S;
import liquibase.pro.packaged.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.xml.crypto.Data;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class Design {

    public static long numOfProducts;
    public static long numOfQs;
    public static long startTime;
    public Originator originator = new Originator();
    public CareTaker careTaker = new CareTaker();
    static private ArrayList<String> leaves = new ArrayList<>();

    @Autowired
    WebSocketServices webSocketServices;

    public void setDesign(ArrayList<String> names, ArrayList<Connection> connections, ArrayList<String> products) {
        DataBase.clear();
        startTime = System.currentTimeMillis();
        numOfProducts = products.size();
        numOfQs = countQs(names);
        leaves = this.leaves(names, connections);
        for (String name : names) {
            initializeObjects(name);
        }
        for (Connection connection : connections) {
            initializeConnections(connection);
        }
        for (String product : products) {
            setProducts(product);
        }
    }

    public ArrayList<String> leaves(ArrayList<String> names, ArrayList<Connection> connections) {
        ArrayList<String> res = new ArrayList<>();
        res = (ArrayList<String>) names.clone();
        for (Connection connection : connections) {
            if (connection.getFirst().charAt(0) == 'Q') {
                res.remove(connection.getFirst());
                res.remove(connection.getSecond());
            } else {
                res.remove(connection.getFirst());
            }
        }
        int i = 0;
        while (i < res.size() - 1) {
            int j = i + 1;
            while (j < res.size()) {
                if (res.get(i).equals(res.get(j))) {
                    res.remove(j);
                    j--;
                }
                j++;
            }
            i++;
        }
        return res;
    }

    public void checkEnd() {
        for (Map.Entry<String, Machine> machineEntry : DataBase.getMachines().entrySet()) {
            if (machineEntry.getValue().isAlive()) {
                notifyFrontEnd(new ResponseObject("1", null));
            }
        }
    }

    public long countQs(ArrayList<String> names) {
        long counter = 0;
        for (String i : names) if (i.charAt(0) == 'Q') counter++;
        return counter;
    }

    public static boolean check() {
        long sum = 0;
        for (String leaf : leaves) {
            QueueLine queueLine = DataBase.getQueueLines().get(leaf);
            if (queueLine != null) {
                sum += queueLine.getSize();
            }
        }
        return sum == numOfProducts;
    }

    public void updateState(String name, String colorOrNumber, long time) {
        originator.setState(name, colorOrNumber, time);
        careTaker.add(originator.saveStateToMemento());
    }

    public void replay() {
        long replayStart = System.currentTimeMillis();
        long replayEnd;
        List<Memento> states = careTaker.getList();
        int counter = 0;
        while (counter < states.size()) {
            replayEnd = System.currentTimeMillis();
            while (counter < states.size() && states.get(counter).getTime() <= replayEnd - replayStart) {
                ResponseObject responseObject = new ResponseObject(states.get(counter).getName(), states.get(counter).getColorOrNumber());
                webSocketServices.sendMessage("messages", responseObject);
                counter++;
            }
        }
    }

    synchronized public void notifyFrontEnd(ResponseObject responseObject) {
        webSocketServices.sendMessage("messages", responseObject);
    }

    public void initializeObjects(String name) {
        if (name.charAt(0) == 'Q') {
            QueueLine queueLine = new QueueLine(name, this);
            DataBase.getQueueLines().put(name, queueLine);
        } else if (name.charAt(0) == 'M') {
            Machine machine = new Machine(name, (long) (Math.random() * 10000), this);
            DataBase.getMachines().put(name, machine);
            machine.start();
        }
    }

    public void terminate() {
        for (Map.Entry<String, Machine> machineEntry : DataBase.getMachines().entrySet()) {
            machineEntry.getValue().exit = true;
        }
    }

    public void initializeConnections(Connection connection) {
        if (connection.getFirst().charAt(0) == 'Q') {
            DataBase.getMachines().get(connection.getSecond()).addObserver(DataBase.getQueueLines().get(connection.getFirst()));
        } else {
            DataBase.getMachines().get(connection.getFirst()).setNextQ(connection.getSecond());
        }
    }

    public void setProducts(String color) {
        Product product = new Product(color);
        DataBase.getQueueLines().get("Q0").addProduct(product);
    }

}
