package com.example.cache;


public class Node<Key> {
    private Key key;
    private int frequency;

    Node(Key key) {
        this.key = key;
        this.frequency = 0;
    }

    public Key getKey() {
        return this.key;
    }

    public Integer getFrequency() {
        return this.frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

}
