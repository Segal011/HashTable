package performance;

import java.util.*;

public class HashTable<K, V> {

    private static int DEFAULT_INITIAL_CAPACITY = 16;

    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected HashEntry<K, V>[] table;

    protected int size = 0;

    protected float loadFactor;

    protected int maxChainSize = 0;

    protected int rehashesCounter = 0;

    protected int lastUpdatedChain = 0;

    protected int chainsCounter = 0;

    protected int index = 0;

    public HashTable(){}

    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(float loadFactor) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor);
    }

    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.table = new HashEntry [initialCapacity];
        this.loadFactor = loadFactor;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() { return size; }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        lastUpdatedChain = 0;
        maxChainSize = 0;
        rehashesCounter = 0;
        chainsCounter = 0;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }
        index = getHash(key);
        if (table[index] == null) {
            chainsCounter++;
        }
        HashEntry<K, V> node = getInChain(key, table[index]);
        if (node == null) {
            table[index] = new HashEntry<>(key, value, table[index]);
            size++;

            if (size > table.length * loadFactor) {
                rehash(table[index]);
            } else {
                lastUpdatedChain = index;
            }
        } else {
            node.value = value;
            lastUpdatedChain = index;
        }
        return value;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(Key key)");
        }
        index = getHash(key);
        HashEntry<K, V> node = getInChain(key, table[index]);
        return (node != null) ? node.value : null;
    }

    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(Key key)");
        }
        index = getHash(key);
        HashEntry<K, V> previous = null;
        for (HashEntry<K, V> n = table[index]; n != null; n = n.next) {
            if ((n.key).equals(key)) {
                if (previous == null) {
                    table[index] = n.next;
                } else {
                    previous.next = n.next;
                }
                size--;
                if (table[index] == null) {
                    chainsCounter--;
                }
                return n.value;
            }
            previous = n;
        }
        return null;
    }

    private void rehash(HashEntry<K, V> node) {
        HashTable map = new HashTable(table.length * 2, loadFactor);
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                if (table[i].equals(node)) {
                    lastUpdatedChain = i;
                }
                map.put(table[i].key, table[i].value);
                table[i] = table[i].next;
            }
        }
        table = map.table;
        maxChainSize = map.maxChainSize;
        chainsCounter = map.chainsCounter;
        rehashesCounter++;
    }

    private HashEntry getInChain(K key, HashEntry node) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in getInChain(Key key, Node node)");
        }
        int chainSize = 0;
        for (HashEntry<K, V> n = node; n != null; n = n.next) {
            chainSize++;
            if ((n.key).equals(key)) {
                return n;
            }
        }
        maxChainSize = Math.max(maxChainSize, chainSize + 1);
        return null;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (HashEntry<K, V> node : table) {
            if (node != null) {
                for (HashEntry<K, V> n = node; n != null; n = n.next) {
                    result.append(n.toString()).append(System.lineSeparator());
                }
            }
        }
        return result.toString();
    }

    public String[][] toArray() {
        String[][] result = new String[table.length][];
        int count = 0;
        for (HashEntry<K, V> n : table) {
            String[] list = new String[getMaxChainSize()];
            int countLocal = 0;
            while (n != null) {
                list[countLocal++] = n.toString();
                n = n.next;
            }
            result[count] = list;
            count++;
        }
        return result;
    }

    public int getMaxChainSize() {
        return maxChainSize;
    }

    public int getRehashesCounter() {
        return rehashesCounter;
    }

    public int getTableCapacity() {
        return table.length;
    }

    public int getLastUpdatedChain() {
        return lastUpdatedChain;
    }

    public int getChainsCounter() {
        return chainsCounter;
    }

    public boolean containsValue(V value) {
        for (HashEntry<K, V> n : table) {
            while (n != null) {
                if (n.value.equals(value)) {
                    return true;
                }
                n = n.next;
            }
        }
        return false;
    }

    public Set<K> keySet(){
        Set<K> keys = new HashSet<>();
        for (HashEntry<K, V> n : table) {
            while (n != null) {
                keys.add(n.key);
                n = n.next;
            }
        }
        return keys;
    }

    public V putIfAbcent(K key, V value) {
        V v = get(key);
        if (v != null) {
            return v;
        }
        put(key, value);
        return v;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new IllegalArgumentException("Key or value is null in replace(Key key, Value value)");
        }
        index = getHash(key);
        System.out.println(index);
        HashEntry<K, V> node = getInChain(key, table[index]);
        while (node != null) {
            if (node.value.equals(oldValue)) {
                node.value = newValue;
                return true;
            }
            HashEntry<K, V> a = node.next;
            node = a;
        }
        return false;
    }

    public List<V> values() {
        List<V> newList = new ArrayList<>();
        for (HashEntry<K, V> n : table) {
            while (n != null) {
                newList.add(n.value);
                n = n.next;
            }
        }
        return newList;
    }


    public int averageChainSize() {
        int sum = 0;
        int count = 0;
        for (HashEntry<K, V> n : table) {
            if (n != null) {
                count++;
                while (n != null) {
                    sum++;
                    n = n.next;
                }
            }
        }
        return sum / count;
    }

    public int numberOfEmpties() {
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (null == table[i]) {
                count++;
            }
        }
        return count;
    }

    public static class HashEntry<K, V> {
        K key;
        V value;
        // Linked list of same hash entries.
        HashEntry next;

        public HashEntry(K key, V value, HashEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    public String[][] getModelList(String delimiter) {
        String[][] result = new String[table.length][];
        int count = 0;
        for (HashEntry<K, V> n : table) {
            List<String> list = new ArrayList();
            list.add( "[ " + count + " ]");
            while (n != null) {
                list.add("-->");
                list.add(split(n.toString(), delimiter));
                n = n.next;
            }
            result[count] = list.toArray(new String[0]);
            count++;
        }
        return result;
    }
    private String split(String s, String delimiter) {
        int k = s.indexOf(delimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }

}