package testing
import production.FAH2c
import production.HashTable

fun testHashTable(){
    val hashTable = HashTable({input -> FAH2c(input, 30)}, arrayListOf(Pair(4,"a"), Pair(2,"d"), Pair(3,"b"), Pair(7,"c")), 8)
    println("Start: ")
    printHashTable(hashTable)

    hashTable[0] = "hi"
    println("hashTable[0] = \"hi\": ")
    printHashTable(hashTable)

    hashTable[0] = "bye"
    println("hashTable[0] = \"bye\": ")
    printHashTable(hashTable)
}
fun <K : Any,V : Any> printHashTable(hashTable: HashTable<K,V>){
    println("------Hash Table: ${hashTable.arr.toList()}, Size: ${hashTable.size}/${hashTable.arr.size}, Collision %: ${"%.2f".format(hashTable.getCollisionProportion())}, ")
}
fun main() {
    testHashTable()
}