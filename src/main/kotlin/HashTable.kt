import java.util.LinkedList

/*
- do you make every slot a linked list or just ones with collisions
- Should you dynamically determine size or let the user decide
 */
class HashTable<K, V>(hashFunc: (input: Any) -> Int, items: Collection<V> = emptyList()) {
    init {

    }

    private val ht = Array<LinkedList<Pair<K, V>>?>(17) {null} //17 is a very tasty number

    private var numItems = 0

    operator fun get(key: K) : V{
        TODO()
    }

    operator fun set(key: K, value: V) {
        numItems++

    }

    fun remove(key: K, value: V){
        numItems--

    }

    fun size() : Int {
        return numItems
    }
}

fun main(args: Array<String>) {
    var x = HashTable<Char, Int>({43}, 25)

}