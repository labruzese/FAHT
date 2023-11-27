//TODO: Add iterator
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.math.BigInteger
import java.util.LinkedList


//Pre-condition: maxInitialStorage is a positive power of 2
class HashTable<K : Any, V: Any>(private val hashFunc: (Any) -> BigInteger = {input -> FAH2c(getObjectBinary(input), 32)},
                                 items: Collection<Pair<K,V>> = emptyList(),
                                 initialStorage: Int = 64) {
    private val MAX_PERCENT_FULL: Double = 0.5
    private val SIZE_INCREASE_MULTIPLIER = 2
    var numCollisions = 0
        private set

    //Should be fully private in any actual implementation but is publicly visible for testing
    var arr : Array<LinkedList<Pair<K, V>>?> private set
    private val keys = mutableSetOf<K>()

    init {
        require(initialStorage > 0 && initialStorage and (initialStorage - 1) == 0) //positive power of 2
        arr = Array(getArraySize(initialStorage)) {null}
        for(item in items){
            this[item.first] = item.second
        }
    }

    //Pre-condition: Your hashing function is not a simple mod hash
    private fun getArraySize(maxOccupancy: Int) : Int = (maxOccupancy/MAX_PERCENT_FULL).toInt()


    //Post-condition: Returns the value if it is found, null otherwise
    operator fun get(key: K) : V? {
        val list = arr[getIndex(key)] ?: return null
        val collisionIndex = list.indexOfFirst { it.first == key }
        return if (collisionIndex != -1) list[collisionIndex].second else null
    }

    //Post-condition: Adds the key and value, replaces the current value if the key is already in the hashmap
    //Returns null if key is not already found, otherwise returns the value replaced
    operator fun set(key: K, value: V) : V? {
        //Find spot
        val arrIndex = getIndex(key)

        //Add a linked list if there isn't one there already
        if (arr[arrIndex] == null) {
            arr[arrIndex] = LinkedList<Pair<K, V>>()
        }
        //Make sure the key isn't already in the linked list
        else {
            for(i in arr[arrIndex]!!.indices){
                if(arr[arrIndex]!![i].first == key) {
                    return arr[arrIndex]!![i].second.also{ arr[arrIndex]!![i] = Pair(key, value)}
                }
            }
            numCollisions++//If there is already a list and the key isn't in it, new collision
        }

        //Add item
        arr[arrIndex]!!.addLast(Pair(key, value))
        keys.add(key)
        if (keys.size > MAX_PERCENT_FULL * arr.size) increaseSize()
        return null
    }

    private fun getIndex(key: K) = (hashFunc(key).mod(arr.size.toBigInteger())).toInt() //this could be optimized with masks

    //Post-condition: Increases the size of the hashmap by a factor of SIZE_INCREASE_MULTIPLIER
    private fun increaseSize() {
        //Store all the keys and values of the old hashmap
        val keyValues = getKVPairs()

        //Clear and re-init everything
        keys.clear()
        numCollisions = 0//TODO: Delete
        arr = Array(arr.size * SIZE_INCREASE_MULTIPLIER) { null }

        //Put all the key values back
        for (kv in keyValues) {
            set(kv.first, kv.second)
        }
    }

    //Post-condition: Returns true if the key was removed, false if not found
    fun remove(key: K): Boolean{
        keys.remove(key)
        val list = arr[getIndex(key)] ?: return false
        val collisionIndex = list.indexOfFirst { it.first == key }
        return collisionIndex != -1 && list.removeAt(collisionIndex).run { true }
    }

    fun size() : Int = keys.size

    @Suppress("MemberVisibilityCanBePrivate")
    fun getKVPairs() : MutableSet<Pair<K, V>>{
        val keyValues = mutableSetOf<Pair<K, V>>()
        for(k in keys){
            keyValues.add(Pair(k, this[k]!!))
        }
        return keyValues
    }

    fun getCollisionProportion() : Double = numCollisions.toDouble()/keys.size
    override fun toString(): String = getKVPairs().toString()
}