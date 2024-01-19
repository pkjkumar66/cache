package com.example.cache;

import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

@AllArgsConstructor
public class LFU<Key> implements EvictionPolicy<Key> {
    public Map<Key, Node<Key>> m;
    public PriorityQueue<Node<Key>> queue;

    LFU() {
        m = new HashMap<>();
        queue = new PriorityQueue<>(Comparator.comparingInt(Node::getFrequency));
    }

    @Override
    public void keyAccessed(Key key) {
        if (m.containsKey(key)) {
            Node<Key> node = m.get(key);
            node.incrementFrequency();

            // remove
            queue.remove(node);
            m.remove(key);

            // add
            m.put(key, node);
            queue.add(node);
        } else {
            Node<Key> node = new Node<>(key);
            node.incrementFrequency();
            m.put(key, node);
            queue.add(node);
        }
    }

    @Override
    public Key keyEvicted() {
        Node<Key> poll = queue.poll();

        if (Objects.nonNull(poll)) {
            m.remove(poll.getKey());
            return poll.getKey();
        }

        return null;
    }
}
