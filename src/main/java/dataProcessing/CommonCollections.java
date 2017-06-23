package dataProcessing;


import org.apache.commons.collections4.*;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.LinkedMap;

/**
 * Commons-collections uses a design approach to synchronization similar to the standard Java collections.
 * The majority of the various implementations of collections, maps and bags are not thread safe
 * without additional synchronization. The appropriate synchronizeXXX method on Collections is
 * one way that these implementations can be synchronized for use in a multithreaded application.
 */
public class CommonCollections {
    public static void main(String[] args) {
        //map iteration:
        //The JDK Map interface always suffered from being difficult to iterate over.
        // API users are forced to either iterate over an EntrySet or over the KeySet.
        // Commons-Collections now provides a new interface - MapIterator that allows simple iteration over maps.
        IterableMap<Integer, String> map = new HashedMap<Integer, String>();
        map.put(1, "value1");
        map.put(2, "value2");
        map.put(3, "value3");
        map.put(4, "value4");
        map.put(5, "value5");
        MapIterator<Integer, String> iter = map.mapIterator();
        while (iter.hasNext()) {
            int key = iter.next();
            String value = iter.getValue();
            iter.setValue(value + "new");
            System.out.println(key);
        }

        //ordered Maps:
        //A new interface is provided for maps that have an order but are not sorted- OrderedMap. Two
        // implementations are provided - LinkedMap and ListOrderedMap(a decorator).This interface supports
        // the map iterator, and also allows iteration both forwards and backwards through the map.
        OrderedMap<String, String> map2 = new LinkedMap<String, String>();
        map2.put("first", "1");
        map2.put("second", "2");
        map2.put("third", "3");
        OrderedMapIterator<String, String> iter2 = map2.mapIterator();
        while (iter2.hasNext()) {
            String key = iter2.next();
            String value = iter2.getValue();
            System.out.println(key);
        }

        // Bidirectional Maps
        //A new interface hierarchy has been added to support bidirectional maps- BidiMap. These
        // represent maps where the key can lookup the value and the value can lookup the key with equal ease.
        BidiMap<Integer, Integer> map3 = new TreeBidiMap<>();
        map3.put(1, 2);
        map3.get(1); //returns 2
        map3.getKey(2); //returns key 1 where 2 is the value of that key
        //duplicated key or value is not allowed in bidiMap
        BidiMap inverseBidiMap = map3.inverseBidiMap(); //return a map with keys and values swapped
        inverseBidiMap.removeValue(2);//remove key/value pair from map where value is 2
         //Bags: represent collections where a certain number of copies of each element is held.
        Bag<String> bag = new HashBag<>();
        bag.add("value1");
        bag.add("value2", 3);
        bag.add("value3", 2); //add value2 two times into the bag
        System.out.println(bag.toString());
        System.out.println(bag.getCount("value2"));

        //map with fixed size:LRUMap, last recently used map, where size of the map is fixed
        //if map is full and we add item to the map, the last recently used element will be deleted
        //this is used for implementing caches.
        LRUMap<String, String> map4 = new LRUMap<>(2);
        map4.put("key1", "value1");
        map4.put("key2", "value2");
        map4.get("key1");// used key
        System.out.println(map4.toString());
        map4.put("key3", "value3");
        System.out.println(map4.toString());







    }
}
