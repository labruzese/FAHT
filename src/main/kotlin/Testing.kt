import java.math.BigInteger
import java.util.BitSet


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

fun checkAvalanche(){
    val hashLength = 16

    val data: String = "1100101011001111010101010010101001".repeat(1)
    val orgHash: String = pad(FAH2(data.toBigInteger(2), hashLength), hashLength)
    val changedNums = IntArray(hashLength)

    println("Data: $data")
    print("Number of digits changed out of $hashLength: ")
    for(i in data.indices){
        val newData: String = if(data[i] == '0') data.replaceRange(i, i + 1, "1") else data.replaceRange(i, i + 1, "0")
        val newHash: String = pad(FAH2(newData.toBigInteger(2), hashLength), hashLength)
        print("" + compare(orgHash, newHash) + ",")
        for(j in orgHash.indices){
            if(orgHash[j] != newHash[j]){
                changedNums[j]++
            }
        }
    }
    println()
    for(i in changedNums.indices){
        println("$i digit is changed ${"%.2f".format(changedNums[i]/data.length.toDouble() * 100)}% of the time")
    }
}

fun checkCollisions(hashFunc: (Any) -> BigInteger): Double{
    val h = HashTable<Int, String>(hashFunc, initialStorage = 32768)
    val start = System.currentTimeMillis()
    for(i in 1..10000){
        val key = (Math.random() * 100000000).toInt()
        h[key] = "e"
    }
    val end = System.currentTimeMillis()
    print("Time: ${"%.2f".format((end - start)/1000.0)} seconds - ")
    /*
    println("Num collisions: ${h.numCollisions}")
    println("Num collisions proportion: ${h.getCollisionProportion()}")
    println("Num keys: ${h.size()}")
     */
    return h.getCollisionProportion()
}
fun main() {
    println("Object hashing:" + checkCollisions { input -> input.hashCode().toBigInteger() })
    println("FAH2 hashing:" + checkCollisions { input -> FAH2(getObjectBinary(input), 32) })
    println("FAH2m hashing:" + checkCollisions { input -> FAH2m(getObjectBinary(input), 32) })
    println("FAH3 hashing:" + checkCollisions { input -> FAH3(getObjectBinary(input), 32) })

    //print(getObjectBinary(HashTable<String, String>()).toString(2))
    //checkAvalanche()
    //checkCollisions()
}