package com.example.cst2335lab2;

public class TodoItem {

    private long id;
    private String text;
    private boolean urgent;

    public TodoItem(long id, String text, boolean urgent) {
        this.id = id;
        this.text = text;
        this.urgent = urgent;
    }

    public TodoItem(String text, boolean urgent) {
        this(-1, text, urgent);
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return urgent;
    }
}