package org.iq47.devops.rest;

public class MessageRequest {
    private String message;

    // Constructors, getters, setters
    public MessageRequest() {}

    public MessageRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
