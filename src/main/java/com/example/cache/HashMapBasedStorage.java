package com.example.cache;


import com.example.cache.exception.NotFoundException;
import com.example.cache.exception.StorageFullException;
import lombok.Data;

import java.util.Hashtable;

@Data
public class HashMapBasedStorage<Key, Value> implements Storage<Key, Value> {

    private Hashtable<Key, Value> storage;
    private Integer capacity;

    public HashMapBasedStorage(Integer capacity) {
        this.capacity = capacity;
        storage = new Hashtable<>(capacity);
    }

    @Override
    public void add(Key key, Value value) throws StorageFullException {
        // if value needs to update
        if (storage.containsKey(key)) {
            storage.put(key, value);
            return;
        }

        if (isStorageFull()) {
            throw new StorageFullException("Capacity Full.....");
        }

        storage.put(key, value);
    }

    @Override
    public void remove(Key key) throws NotFoundException {
        if (!storage.containsKey(key)) {
            throw new NotFoundException(key + "doesn't exist in cache.");
        }
        storage.remove(key);
    }

    @Override
    public Value get(Key key) throws NotFoundException {
        if (!storage.containsKey(key)) {
            throw new NotFoundException(key + "doesn't exist in cache.");
        }
        return storage.get(key);
    }

    private boolean isStorageFull() {
        return storage.size() == capacity;
    }
}
