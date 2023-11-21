import java.math.BigInteger

//Pre-condition: size >= num.bitLength()
fun pad(num: BigInteger, size: Int): String{
    return "0".repeat(size-num.toString(2).length) + num.toString(2)
}

//Pre-condition: s1 and s2 are the same length
private fun compare(s1: String, s2: String): Int{
    var numCharsDifferent = 0
    for (c in s1.indices){
        if(s1[c] != s2[c]){
            numCharsDifferent++
        }
    }
    return numCharsDifferent
}

fun checkAvalanche(hashFunc: (Any) -> BigInteger, printing: Boolean = false): Double{
    val hashLength = 32

    val data: String = "1100101011001111010101010010101001".repeat(1)
    val orgHash: String = pad(hashFunc(data), hashLength)
    val changedNums = IntArray(hashLength)

    if(printing) println("Data: $data")
    if(printing) print("Number of digits changed out of $hashLength: ")
    for(i in data.indices){
        val newData: String = if(data[i] == '0') data.replaceRange(i, i + 1, "1") else data.replaceRange(i, i + 1, "0")
        val newHash: String = pad(hashFunc(newData), hashLength)
        if(printing) print("" + compare(orgHash, newHash) + ",")
        for(j in orgHash.indices){
            if(orgHash[j] != newHash[j]){
                changedNums[j]++
            }
        }
    }
    if(printing) {
        println()
        for (i in changedNums.indices) {
            println("$i digit is changed ${"%.2f".format(changedNums[i] / hashLength.toDouble() * 100)}% of the time")
        }
    }
    var total = 0.0
    for(i in changedNums.indices){
        total += if(i > hashLength) i - hashLength else i
    }
    return total/changedNums.size
}

fun checkCollisions(hashFunc: (Any) -> BigInteger, timePrinting: Boolean = false, collisionPrinting: Boolean = false): Double{
    val h = HashTable<String, String>(hashFunc, initialStorage = 32768)
    val start = System.currentTimeMillis()
    for(i in 1..16384){
        val key = (Math.random() * 100000000).toInt().toString(17)
        h[key] = "e"
    }
    val end = System.currentTimeMillis()
    if(timePrinting) print("Time: ${"%.2f".format((end - start)/1000.0)} seconds - ")
    if(collisionPrinting) {
        println("Num collisions: ${h.numCollisions}")
        println("Num collisions proportion: ${h.getCollisionProportion()}")
        println("Num keys: ${h.size()}")
    }
    return h.getCollisionProportion()
}
fun main() {

    println("Object hashing:" + checkCollisions({ input -> input.hashCode().toBigInteger() }, timePrinting = true))
    println("FAH2 hashing:" + checkCollisions ({ input -> FAH2(getObjectBinary(input), 32) }, timePrinting = true))
    println("FAH4 hashing:" + checkCollisions ({ input -> FAH4.hash(getObjectBinary(input)) }, timePrinting = true))
    //println("FAH2m hashing:" + checkCollisions { input -> FAH2m(getObjectBinary(input), 32) })
    //println("FAH3 hashing:" + checkCollisions { input -> FAH3(getObjectBinary(input), 32) })

    //print(getObjectBinary(HashTable<String, String>()).toString(2))
    checkAvalanche({input -> FAH2(getObjectBinary(input), 32) }, true)
    //checkCollisions()
}