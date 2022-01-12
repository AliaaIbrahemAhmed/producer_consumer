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


@Component
public class Design {

    public static long numOfProducts;

    @Autowired
    WebSocketServices webSocketServices;

    public void setDesign(ArrayList<String> names, ArrayList<Connection> connections, ArrayList<String> products) {
        numOfProducts = products.size();
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

    public static boolean check() {
        QueueLine queueLine = DataBase.getQueueLines().get("Q" + (numOfProducts - 1));
        if (queueLine != null && queueLine.getSize() == numOfProducts) {
            return true;
        }
        return false;
    }

    public void run(Machine machine){
        while (!Design.check()) {
            machine.notifyObservers();
            if (machine.getProduct() != null) {
                machine.updateColor();
                System.out.println("Machine " + machine.name + " is working in product with color " + machine.getProduct().getColor());
                System.out.println("Machine " + machine.name + "'s color is" + machine.getColor());
                ResponseObject responseObject = new ResponseObject(machine.name, machine.getColor());
                ResponseService.addResponse(responseObject);
                webSocketServices.sendMessage("messages", responseObject);
//                try {
//                    //Controller.send(responseObject);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                try {
                    machine.sleep(machine.getWait());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                machine.finish();
            }
        }
    }

    public void initializeObjects(String name) {
        if (name.charAt(0) == 'Q') {
            QueueLine queueLine = new QueueLine(name);
            DataBase.getQueueLines().put(name, queueLine);
        } else {
            Machine machine = new Machine(name, (long) (Math.random() * 1000),this);
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
