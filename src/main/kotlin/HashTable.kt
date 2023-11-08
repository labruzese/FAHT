//TODO: Add iterator
import java.util.LinkedList

class HashTable<K : Any, V>(private val hashFunc: (input: Any) -> Int, items: Collection<Pair<K,V>> = emptyList(), maxInitialStorage: Int = 16) {
    private val MAX_PERCENT_FULL: Double = 0.5
    private val SIZE_INCREASE_MULTIPLIER = 4

    private var arr : Array<LinkedList<Pair<K, V>>?>
    private val keys = mutableSetOf<K>()
    init {
        arr = Array(getArraySize(maxInitialStorage)) {null}
        for(item in items){
            this[item.first] = item.second
        }
    }

    //Pre-condition: Your hashing function is not a simple mod hash
    private fun getArraySize(maxOccupancy: Int) : Int = (maxOccupancy/MAX_PERCENT_FULL).toInt()


    //Post-condition: Returns the value if it is found, null otherwise
    operator fun get(key: K) : V? {
        val list = arr[getIndex(key)] ?: return null //should this really return null or should we have an empty spot object
        val collisionIndex = list.indexOfFirst { it.first == key }
        return if (collisionIndex != -1) list[collisionIndex].second else null
    }

    //Post-condition: Returns true if successfully added, false if the key is already there
    operator fun set(key: K, value: V) : Boolean {
        //Find spot
        val arrIndex = getIndex(key)

        //Add a linked list if there isn't one there already
        if (arr[arrIndex] == null)
            arr[arrIndex] = LinkedList<Pair<K, V>>()

        //Make sure the key isn't already in the linked list
        else {
            for(i in arr[arrIndex]!!.indices){
                if(arr[arrIndex]!![i].first == key) {
                    return false
                }
            }
        }

        //Add item
        arr[arrIndex]!!.addLast(Pair(key, value))
        if (keys.size >= MAX_PERCENT_FULL * arr.size) increaseSize()
        keys.add(key)
        return true
    }

    private fun getIndex(key: K) = hashFunc(key) % arr.size

    private fun increaseSize(){
        //Store all the keys and values of the old hashmap
        val keyValues = getKVPairs()

        //Clear and re-init everything
        keys.clear()
        arr = Array(arr.size * SIZE_INCREASE_MULTIPLIER) {null}

        //Put all the key values back
        for(kv in keyValues){
            set(kv.first, kv.second)
        }
    }

    fun remove(key: K, value: V){
        TODO()
        keys.remove(key)
    }

    fun size() : Int = keys.size

    fun getKVPairs() : MutableSet<Pair<K, V>>{
        val keyValues = mutableSetOf<Pair<K, V>>()
        for(k in keys){
            keyValues.add(Pair(k, get(k)!!))
        }
        return keyValues
    }

    override fun toString(): String = getKVPairs().toString()
}

fun main(args: Array<String>) {
    val h = HashTable({0}, arrayListOf(Pair('a', 1)))
    h['b'] = 2
    println(h['a'])
    println(h['b'])
    println(h['c'])
    println()
    print(h)
//    val kotlinHashMap
}