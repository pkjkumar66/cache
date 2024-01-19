package com.example.cache;

public interface EvictionPolicy<Key> {
    void keyAccessed(Key key);
    Key keyEvicted();
}
