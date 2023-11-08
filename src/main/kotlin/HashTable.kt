import java.util.LinkedList

class HashTable<K : Any, V>(private val hashFunc: (input: Any) -> Int, items: Collection<Pair<K,V>> = emptyList(), maxInitialStorage: Int = 16) {
    private val MAX_PERCENT_FULL: Double = 0.5
    private val SIZE_INCREASE_MULTIPLIER = 4

    private var arr : Array<LinkedList<Pair<K, V>>?>  //17 is a very tasty number
    private val keys = mutableSetOf<K>()
    init {
        arr = Array(getArraySize(maxInitialStorage)) {null}
        for(item in items){
            this[item.first] = item.second
        }
    }

    private fun getArraySize(maxOccupancy: Int) : Int {
        return (maxOccupancy/MAX_PERCENT_FULL).toInt() //this will work unless you have a terrible hash function but is it really a hashing function at that point
    }

    operator fun get(key: K) : V? {
        val list = arr[getIndex(key)] ?: return null //should this really return null or should we have an empty spot object
        val collisionIndex = list.indexOfFirst { it.first == key }
        return if (collisionIndex != -1) list[collisionIndex].second else null
    }

    operator fun set(key: K, value: V) : Boolean {
        val arrIndex = getIndex(key)
        if (arr[arrIndex] == null)
            arr[arrIndex] = LinkedList<Pair<K, V>>()
        else {
            for(i in arr[arrIndex]!!.indices){
                if(arr[arrIndex]!![i].first == key) {
                    return false
                }
            }
        }
        arr[arrIndex]!!.addLast(Pair(key, value))
        if (keys.size >= MAX_PERCENT_FULL * arr.size) increaseSize()
        keys.add(key)
        return true
    }

    private fun getIndex(key: K) = hashFunc(key) % arr.size

    private fun increaseSize(){
        //Store all the keys and values of the old hashmap
        val keyValues = mutableSetOf<Pair<K, V>>()
        for(k in keys){
            keyValues.add(Pair(k, get(k)!!))
        }

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

    fun size() : Int {
        return keys.size
    }
}

fun main(args: Array<String>) {
    var x = HashTable<Char, Int>({0}, 25)
//    val kotlinHashMap
}