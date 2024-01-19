package com.example.cache;


import com.example.cache.exception.NotFoundException;
import com.example.cache.exception.StorageFullException;

public class Cache<Key, Value> {
    private Storage<Key, Value> storage;
    private EvictionPolicy<Key> evictionPolicy;

    public Cache(EvictionPolicy<Key> evictionPolicy, Integer capacity) {
        this.evictionPolicy = evictionPolicy;
        this.storage = new HashMapBasedStorage<>(capacity);
    }

    void put(Key key, Value value) {
        try {
            this.storage.add(key, value);
            this.evictionPolicy.keyAccessed(key);
            System.out.println("added (k,v): " + key + " " + value);
        } catch (StorageFullException exception) {
            System.out.println("Got storage full. Will try to evict.");
            Key keyToRemove = evictionPolicy.keyEvicted();
            if (keyToRemove == null) {
                throw new RuntimeException("Unexpected State. Storage full and no key to evict.");
            }
            this.storage.remove(keyToRemove);

            System.out.println("Creating space by evicting item..." + keyToRemove);
            this.storage.add(key, value);
            this.evictionPolicy.keyAccessed(key);
        }
    }

    Value get(Key key) {
        try {
            Value value = this.storage.get(key);
            this.evictionPolicy.keyAccessed(key);
            if (value != null) {
                System.out.println("Got value of key " + key + ": " + value);
                return value;
            }
            return (Value) String.valueOf(-1);

        } catch (NotFoundException notFoundException) {
            System.out.println("Tried to access non-existing key: " + key);
            return null;
        }
    }
}
