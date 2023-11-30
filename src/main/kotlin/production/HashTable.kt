package production
import java.math.BigInteger
import java.util.*
/*
GENERAL PROJECT DESCRIPTION
 */
class HashTable<K : Any, V: Any>(items: Collection<Pair<K,V>> = emptyList(),
                                 expectedOccupancy: Int = 64,
                                 private val hashFunc: (Any) -> BigInteger = {input -> FAH4.hash(input, 64) }) {
    companion object {
        private const val MAX_PERCENT_FULL: Double = 0.5 //>0
        private const val SIZE_INCREASE_MULTIPLIER = 1 shl 1 //>0 and power of 2
    }

    /* Custom object stored in the hashtable */
    data class Entry<K,V>(val key: K, val hash: BigInteger, val value: V)

    /* Underlying storage of our items */
    var arr : Array<LinkedList<Entry<K, V>>?> private set //Should be fully private in any actual implementation but is publicly visible for testing
    var size : Int = 0
        private set
    var collisions = 0
        private set

    /*
    Description: Initializes empty array of expected size and fill it with items
    */
    init {
        arr = Array(expectedArraySize(expectedOccupancy.coerceAtLeast(items.size))) {null}
        for(item in items){
            this[item.first] = item.second
        }
    }

    /*
    Parameters: key -> key for the output value
    Description: Hashes the key and searches for respective entry
    Return: value of the entry whose key matches parameter
    */
    operator fun get(key: K) : V? {
        val list = arr[getIndex(hashFunc(key))] ?: return null //does hash exist
        val collisionIndex = list.indexOfFirst { it.key == key } //where in collisions is this key
        return if (collisionIndex != -1) list[collisionIndex].value else null //is the key there? yes? return value
    }

    /*
    Parameters: key -> key to be added; value -> the key's corresponding value to be added
    Description: Adds the key and value, replaces the current value if the key is already in the hashmap
    Return: value that's been overwritten (null if key isn't already in table)
    */
    operator fun set(key: K, value: V) : V? {
        return set(Entry(key, hashFunc(key), value))
    }
    private fun set(entry: Entry<K, V>) : V? {
        //Find spot
        val arrIndex = getIndex(entry.hash)
        val list = arr[arrIndex]

        //Add a linked list if there isn't one there already
        if (list == null)
            arr[arrIndex] = LinkedList<Entry<K, V>>()
        else { //list exists
            //Make sure the key isn't already in the linked list
            for(i in list.indices){
                if(list[i].key == entry.key) {
                    //if it is, overwrite it and return old one
                    return list[i].value.also { list[i] = entry }
                }
            }
            //if we haven't overwritten anything we're adding a collision
            collisions++
        }
        //Add item to new spot
        arr[arrIndex]!!.addLast(entry); size++
        if (size > MAX_PERCENT_FULL * arr.size) increaseSize()
        return null //overwrote nothing
    }

    /*
    Parameters: key -> key with corresponding entry to be removed
    Description: Removes key-value from hashtable
    Returns: the value removed. (Null if key is not found).
    */
    fun remove(key: K): V? {
        //find linked-list or null
        val arrIndex = getIndex(hashFunc(key))
        val list = arr[arrIndex] ?: return null

        //find entry or null
        val listIndex = list.indexOfFirst { it.key == key }
        if(listIndex < 0) return null

        val entry = list[listIndex]

        //nuke list or remove from list
        if(list.size == 1) arr[arrIndex] = null
        else {
            list.removeAt(listIndex)
            collisions--
        }
        size--

        //give user back the value they removed
        return entry.value
    }

    private fun getIndex(hash: BigInteger) : Int {
        return (hash and (BigInteger.ONE shl Integer.bitCount(arr.size-1)) - BigInteger.ONE).toInt()
    } //this could be optimized with masks

    /*
    Description: Increases the underlying arr size by `SIZE_INCREASE_MULTIPLIER` by copying values
    from `arr` into a larger array and reassigning `arr` field
    */
    private fun increaseSize() {
        //Store all the entries of the old hashmap
        val entries = dumpEntries()

        //Clear and re-init fields
        clear(arr.size * SIZE_INCREASE_MULTIPLIER)

        //Put all the entries back
        for (entry in entries) {
            set(entry)
        }
    }

    /*
    Description: Clears all items in this Hash Table without changing its size
    */
    fun clear() = clear(arr.size)

    /*
    Parameter: arrSize -> the new size of the array, must be a positive power of 2
    Description: Clears all items in this Hash Table and re-initializes the array size to arrSize
    */
    private fun clear(arrSize: Int){
        size = 0
        collisions = 0
        arr = Array(arrSize) { null }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    /*
    Description: Find each key and value in this hashtable
    Return: A list of all key's with their corresponding values in this hashtable
    */
    fun dumpItems() : List<Pair<K, V>>{
        val keyValues = mutableListOf<Pair<K, V>>()
        for(entry in dumpEntries()){
            keyValues.add(Pair(entry.key, entry.value))
        }
        return keyValues
    }

    /*
    Description: Find each entry in this hashtable
    Return: A list of all entries in this hashtable
    */
    private fun dumpEntries() : List<Entry<K, V>>{
        val entries = mutableListOf<Entry<K, V>>()
        for(list in arr){
            if(list != null) entries.addAll(list)
        }
        return entries
    }

    /*
    Parameter: expectedOccupancy -> the maximum number of items that are expected to be stored in this hashtable
    Description: Calculate the minimum array size that will support `expectedOccupancy` items and meet the
    necessary requirement of being a power of 2
    Return: The minimum array size that will accommodate the `expectedOccupancy` and maintain `MAX_PERCENT_FULL`
    */
    private fun expectedArraySize(expectedOccupancy: Int) : Int = 1 shl Integer.bitCount((expectedOccupancy/MAX_PERCENT_FULL).toInt() - 1)

    /*
    Description: Calculate the proportion of items that have hashed to the same `arr` index
    Return: The proportion of items that have hashed to the same `arr` index
    */
    fun getCollisionProportion() : Double = collisions.toDouble()/size

    override fun toString(): String = dumpItems().toString()
}