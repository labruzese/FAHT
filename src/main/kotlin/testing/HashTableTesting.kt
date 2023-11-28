package testing
import production.FAH2c
import production.HashTable

fun testHashTable(){
    val hashTable = HashTable({input -> FAH2c(input, 30)}, arrayListOf(Pair(4,"a"), Pair(2,"d"), Pair(3,"b"), Pair(7,"c")), 1)
    println("Start: ")
    printHashTable(hashTable)

    hashTable[0] = "hi"
    println("hashTable[0] = \"hi\": ")
    printHashTable(hashTable)

    hashTable[1] = "1"
    println("hashTable[1] = \"1\": ")
    printHashTable(hashTable)

    hashTable[0] = "bye"
    println("hashTable[0] = \"bye\": ")
    printHashTable(hashTable)

    println("hashTable[0] = \"set\": ${hashTable.set(0, "set")}")
    printHashTable(hashTable)

    println("Key 0: ${hashTable[0]}")
    println("Key 12: ${hashTable[12]}")
    println()


    println("hashTable.remove(0): ")
    println("Removed: ${hashTable.remove(0)}")
    printHashTable(hashTable)
}
fun <K : Any,V : Any> printHashTable(hashTable: HashTable<K,V>){
    println("------Hash Table: ${hashTable}, Size: ${hashTable.size}/${hashTable.arr.size}, Collision %: ${"%.2f".format(hashTable.getCollisionProportion())}, ")
    println()
}
fun main() {
    testHashTable()
}