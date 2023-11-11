fun main(args: Array<String>) {
    val h = HashTable(items = arrayListOf(Pair('a', 1)))
    h['b'] = 2
    println(h['a'])
    println(h.set('a', 3))
    println(h['b'])
    println(h['c'])
    println()
    print(h)
//    val kotlinHashMap
}