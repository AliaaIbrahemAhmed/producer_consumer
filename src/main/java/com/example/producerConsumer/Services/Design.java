package com.example.producerConsumer.Services;

import com.example.producerConsumer.Configuration.Controller;
import com.example.producerConsumer.Model.Connection;
import com.example.producerConsumer.Model.DataBase.DataBase;
import com.example.producerConsumer.Model.Machine.Machine;
import com.example.producerConsumer.Model.Product.Product;
import com.example.producerConsumer.Model.QueueLine.QueueLine;
import com.example.producerConsumer.Model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Component
public class Design {

    public static long numOfProducts;
    public static long numOfQs;

    @Autowired
    WebSocketServices webSocketServices;

    public void setDesign(ArrayList<String> names, ArrayList<Connection> connections, ArrayList<String> products) {
        numOfProducts = products.size();
        numOfQs = countQs(names);
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

    public long countQs(ArrayList<String> names) {
        long counter = 0;
        for (String i : names) if (i.charAt(0) == 'Q') counter++;
        return counter;
    }

    public static boolean check() {
        QueueLine queueLine = DataBase.getQueueLines().get("Q" + (numOfQs - 1));
        if (queueLine != null && queueLine.getSize() == numOfProducts) {
            System.out.println(queueLine.getSize());
            return true;
        }
        return false;
    }
//    private static boolean checkColors() {
//        for(Map.Entry<String,Machine> machineEntry : DataBase.getMachines().entrySet()){
//            if(machineEntry.getValue().getColor() != null) return false;
//        }
//        for(Map.Entry<String,Machine> machineEntry : DataBase.getMachines().entrySet()){
//            if(machineEntry.getValue().getColor() != null) return false;
//        }
//        return false;
//    }


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
