package com.example.producerConsumer.Model;

import java.util.ArrayList;

public class DesignObject {
    public ArrayList<String> names;
    public ArrayList<Connection> connections;
    public ArrayList<String> products;
    public DesignObject(){}

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
