package com.example.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);

        System.out.println("LRU Example: ");
        Cache<Integer, Integer> lruCache = new Cache<>(new LRU<>(), 2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.get(1);
        lruCache.put(3, 3);
        lruCache.get(2);
        lruCache.put(4, 4);
        lruCache.get(1);
        lruCache.get(3);
        lruCache.get(4);

        System.out.println();
        System.out.println();
        System.out.println("LFU Example: ");
        Cache<Integer, Integer> lfuCache = new Cache<>(new LFU<>(), 2);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.get(1);
        lfuCache.put(3, 3);
        lfuCache.get(2);
        lfuCache.get(3);
        lfuCache.put(4, 4);
        lfuCache.get(1);
        lfuCache.get(3);
        lfuCache.get(4);
    }

}
