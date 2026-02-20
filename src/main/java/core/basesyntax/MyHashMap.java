package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Node[capacity];
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> head = table[index];

        if (head == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> current = head;

            while (true) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }

            current.next = new Node<>(key, value);
            size++;
        }
        if (size >= threshold) {
            resize();
        }
    }

    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);

        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> head : oldTable) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

}


