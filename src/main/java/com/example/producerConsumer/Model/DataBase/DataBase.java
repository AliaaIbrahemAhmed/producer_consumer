package com.example.producerConsumer.Model.DataBase;

import com.example.producerConsumer.Model.Machine.Machine;
import com.example.producerConsumer.Model.QueueLine.QueueLine;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    private static HashMap<String,QueueLine> queueLines = new HashMap<>();
    private static HashMap<String,Machine> machines = new HashMap<>();

    public static void clear() {
        queueLines.clear();
        machines.clear();
    }

    public static HashMap<String, QueueLine> getQueueLines() {
        return queueLines;
    }

    public static void setQueueLines(HashMap<String, QueueLine> queueLines) {
        DataBase.queueLines = queueLines;
    }

    public static HashMap<String, Machine> getMachines() {
        return machines;
    }

    public static void setMachines(HashMap<String, Machine> machines) {
        DataBase.machines = machines;
    }

}
