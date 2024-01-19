package com.example.cache;

import java.util.LinkedList;

public class LRU<Key> implements EvictionPolicy<Key> {
    private LinkedList<Key> lruList;

    LRU() {
        lruList = new LinkedList<>();
    }

    @Override
    public void keyAccessed(Key key) {
        if (lruList.contains(key)) {
            // remove
            lruList.remove(key);

            // add
            lruList.addFirst(key);
        } else {
            lruList.addFirst(key);
        }
    }

    @Override
    public Key keyEvicted() {
        Key remove = lruList.pollLast();
        return remove;
    }
}
