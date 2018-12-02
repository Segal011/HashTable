package performance;

import java.util.*;

public class HashTableWithDoubleHashing <K, V>{

    private static int DEFAULT_INITIAL_CAPACITY = 16;

    private static Entry[] entries = new Entry[DEFAULT_INITIAL_CAPACITY];

    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    ArrayList<Integer> id = new ArrayList<>();

    protected Entry<K, V>[] table;

    protected int size = 0;

    protected float loadFactor;

    protected int rehashesCounter = 0;

    protected int index = 0;

    public HashTableWithDoubleHashing(){}

    public HashTableWithDoubleHashing(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableWithDoubleHashing(float loadFactor) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor);
    }

    public HashTableWithDoubleHashing(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.table = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        rehashesCounter = 0;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException( "Key or value is null in put(Key key, Value value)" );
        }
        int index = getPosition( key );
        table[index] = new Entry<>(key, value);
        size++;

        if (size > table.length * loadFactor) {
            rehash(table[index]);
        }
        return value;

    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(Key key)");
        }
        if(table[ getPosition( key ) ] != null){
            return table[ getPosition( key ) ].value;
        }
        return null;
    }

    private int getPosition(K key){
        int index1 = getHash1( key );
        int index2 = index1;
        int i = 0;
        for(int j = 0; j < table.length; j++){
            if(table[index1] == null ){
                return index1;
            }
            if(table[index1].key.equals( key )){
                return index1;
            }
            i++;
            index1 =(index2 + i * getHash2( key )) % table.length;
        }
        return -1;
    }

    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(Key key)");
        }
        System.out.println(id.size());
        index = getHash1(key);

        if(table[ getHash1( key ) ]!= null) {
            V value = table[ getHash1( key ) ].value;
            table[ getPosition( key ) ] = new Entry<>( null, null );
            return value;
        }
        return null;
    }

    private void rehash(Entry<K, V> node) {
        HashTableWithDoubleHashing map = new HashTableWithDoubleHashing(table.length * 2, loadFactor);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                map.put(table[i].key, table[i].value);
            }
        }
        table = map.table;
        rehashesCounter++;
    }

    private int getHash1(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    private int getHash2(K key) {
        return 3- key.hashCode()%3;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<K, V> node : table) {
            if (node != null) {
                result.append(node.toString()).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    public String[] toArray() {
        String[] result = new String[table.length];
        int count = 0;
        for (Entry<K, V> n : table) {
            result[count] = n.toString();
            count++;
        }
        return result;
    }

    public int getRehashesCounter() {
        return rehashesCounter;
    }

    public int getTableCapacity() {
        return table.length;
    }

    public boolean containsValue(V value) {
        for (Entry<K, V> n : table) {
            while (n != null) {
                if (n.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<K> keySet(){
        Set<K> keys = new HashSet<>();
        for (Entry<K, V> n : table) {
            while (n != null) {
                keys.add(n.key);
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

    public List<V> values() {
        List<V> newList = new ArrayList<>();
        for (Entry<K, V> n : table) {
            while (n != null) {
                newList.add(n.value);
            }
        }
        return newList;
    }

    public int averageChainSize() {
        int sum = 0;
        int count = 0;
        for (Entry<K, V> n : table) {
            if (n != null) {
                count++;
                while (n != null) {
                    sum++;
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


    public static class Entry<K, V> {
        K key;
        V value;

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Entry(){}

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    public String[][] getModelList(String delimiter) {
        String[][] result = new String[table.length][];
        int count = 0;
        for (Entry<K, V> n : table) {
            List<String> list = new ArrayList();
            list.add( "[ " + count + " ]");
            if (n != null) {
                list.add("-->");
                list.add(n.toString());
            }
            result[count] = list.toArray(new String[0]);
            count++;
        }
        return result;
    }


}
