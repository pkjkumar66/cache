package com.example.cache;

import com.example.cache.exception.NotFoundException;
import com.example.cache.exception.StorageFullException;

public interface Storage<Key, Value> {
    void add(Key key, Value value) throws StorageFullException;

    void remove(Key key) throws NotFoundException;

    Value get(Key key) throws NotFoundException;
}
