package com.example.desensitise.util;

import java.util.TreeMap;

/**
 * 前缀树
 */
public class TrieTree<T> {

    // 数量
    private int size;
    // 根节点
    private Node<T> root;

    public TrieTree() {
        root = new Node<T>("root", null);
        size = 0;
    }

    public TrieTree(String key, Node<T> next) {
        this.root = new Node<T>(key, null);
        this.root.next.put(key, next);
    }

    public Node<T> getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    /**
     * 插入数据
     * @param key
     * @param data
     */
    public void insertAndUpdate(String key, T data) {
        String[] keys = key.split("\\.");
        Node<T> cur = root;
        for (String k : keys) {
            if (cur.next.get(k) == null) {
                // k 不存在，新建节点
                cur.next.put(k, new Node<T>(k,null));
            }
            // 移动到下一个节点
            cur = cur.next.get(k);
        }

        if (cur.data == null) {
            size ++;
        }
        // 更新前缀树节点（除了root节点，其它节点均可以更新）
        cur.data = data;
    }

    public class Node<T> {
        // 冗余存储，方便调试
        private String key;

        private T data;

        private TreeMap<String, Node<T>> next;

        public Node(String key, T data, TreeMap<String,Node<T>> next) {
            this.key = key;
            this.data = data;
            this.next = next;
        }
        public Node(String key, T data) {
            this(key, data, new TreeMap<String, Node<T>>());
        }

        public T getData() {
            return data;
        }

        /**
         * 根据路径查询数据
         * @param key
         * @return null 路径不存在，或者没有数据
         */
        public Node<T> getNode(String key) {
            String[] keys = key.split("\\.");
            Node<T> cur = this;
            for (String k : keys) {
                if (cur.next.get(k) == null) {
                    // k 不存在
                    return null;
                }
                // 移动到下一个节点
                cur = cur.next.get(k);
            }
            return cur;
        }
    }
}
