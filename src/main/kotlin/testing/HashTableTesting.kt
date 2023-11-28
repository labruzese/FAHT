package testing
import production.HashTable

const val BRIGHT_BLUE = "\u001b[94;1m"
const val BRIGHT_MAGENTA = "\u001b[95;1m"
const val BRIGHT_CYAN = "\u001b[96;1m"
const val BRIGHT_WHITE = "\u001b[97;1m"
const val RESET = "\u001b[0m"

fun testHashTable(){
    val hashTable = HashTable(arrayListOf(Pair(497,"a"), Pair(712,"d"), Pair(111,"b"), Pair(732,"c")), 1)
    println("Start: ")
    printHashTable(hashTable)

    hashTable[263] = "hi"
    println("hashTable[263] = \"hi\": ")
    printHashTable(hashTable)

    hashTable[122] = "1"
    println("hashTable[122] = \"1\": ")
    printHashTable(hashTable)

    hashTable[263] = "bye"
    println("hashTable[263] = \"bye\": ")
    printHashTable(hashTable)

    println("hashTable[0] = \"set\": ${hashTable.set(0, "set")}")
    printHashTable(hashTable)

    //println("Key 0: ${hashTable[0]}")
    //println("Key 12: ${hashTable[12]}")
    println()


    println("hashTable.remove(0): ")
    println("Removed: ${hashTable.remove(0)}")
    printHashTable(hashTable)
}

fun <K : Any,V : Any> printHashTable2(hashTable: HashTable<K,V>){
    println("------Hash Table: ${hashTable}, Size: ${hashTable.size}/${hashTable.arr.size}, Collision %: ${"%.2f".format(hashTable.getCollisionProportion())}, ")
    println()
}
fun <K : Any,V : Any> printHashTable(ht: HashTable<K,V>) {
    for(i in ht.arr.indices){
        if(ht.arr[i] != null){
            print(BRIGHT_WHITE)
            print("[${i.toString(2).padStart(ht.arr.size.toString(2).length-1,'0')}] ->")
            print(RESET)
            for(j in ht.arr[i]!!.indices){
                if(j % 2 == 0) print(BRIGHT_BLUE) else print(BRIGHT_MAGENTA)
                print(" ")
                print("{${ht.arr[i]!![j].key}, ${ht.arr[i]!![j].value}}".padEnd(8))
                print(RESET)
            }
            println()
        }
    }
}


fun main() {

}