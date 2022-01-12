package com.example.producerConsumer.Services;

import com.example.producerConsumer.Model.ResponseObject;

import java.util.LinkedList;
import java.util.Queue;

public class ResponseService {
    private static Queue<ResponseObject> responses = new LinkedList<>();

    synchronized public static void addResponse(ResponseObject responseObject) {
        responses.add(responseObject);
    }

    synchronized public static ResponseObject pop() {
        return responses.poll();
    }
}
