package testing
import production.FAH2c
import production.HashTable

fun testHashTable(){
    val hashTable = HashTable({input -> FAH2c(input, 30)}, arrayListOf(Pair(4,"a"), Pair(2,"Penguins"), Pair(3,":)"), Pair(7,"****")), 1)
    println("Hash Table: $hashTable, Size: ${hashTable.size}/${hashTable.arr.size}, Collision %: ${"%.2f".format(hashTable.getCollisionProportion())}, ")

    hashTable[0] = "hi"
    println("hashTable[0] = \"hi\": $hashTable")

    hashTable[0] = "bye"
    println("hashTable[0] = \"bye\": $hashTable")
}
fun main() {
    testHashTable()
}