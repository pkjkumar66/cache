package com.example.cache;


import com.example.cache.exception.NotFoundException;
import com.example.cache.exception.StorageFullException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cache<Key, Value> {
    private Storage<Key, Value> storage;
    private EvictionPolicy<Key> evictionPolicy;
    private final ConcurrentHashMap<Key, Lock> locks;

    public Cache(EvictionPolicy<Key> evictionPolicy, Integer capacity) {
        this.evictionPolicy = evictionPolicy;
        this.storage = new HashMapBasedStorage<>(capacity);
        this.locks = new ConcurrentHashMap<>();
    }

    void put(Key key, Value value) {
        Lock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }
    }

    Value get(Key key) {
        Lock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }
    }
}
