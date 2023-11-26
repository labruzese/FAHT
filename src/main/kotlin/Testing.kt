import java.math.BigInteger
import kotlin.math.pow

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

fun checkAvalanche(hashFunc: (Any) -> BigInteger, hashLength: Int, printing: Boolean = false): Double{
    val data: String = "11010100101011110100100101010011101101010001".repeat(1)
    val orgHash: String = pad(hashFunc(data), hashLength)
    val changedNums = IntArray(hashLength)

    if(printing) println("Data: $data")
    if(printing) print("Number of digits changed out of $hashLength: ")
    for(i in data.indices){
        val newData: String = if(data[i] == '0') data.replaceRange(i, i + 1, "1") else data.replaceRange(i, i + 1, "0")
        val newHash: String = pad(hashFunc(newData), hashLength)
//        println("Old: $orgHash, New: $newHash")
        if(printing) print("" + compare(orgHash, newHash) + ",")
        for(j in orgHash.indices){
            if(orgHash[j] != newHash[j]){
                changedNums[j]++
            }
        }
//        println("$newHash   --- digit changed: $i")
    }
    if(printing) {
        println()
        for (i in changedNums.indices) {
            println("$i digit is changed ${"%.2f".format(changedNums[i] / data.length.toDouble() * 100)}% of the time")
        }
    }
    var total = 0.0
    //println("Changed nums: ${changedNums.toList()}")
    for(i in changedNums){
        total += i
    }
    return (total / data.length.toDouble()) / hashLength
}

fun checkCollisions(hashFunc: (Any) -> BigInteger, hashPrinting: Boolean = false): Double{
    val h = HashTable<Int, String>(hashFunc, initialStorage = 2.0.pow(13).toInt())
    val start = System.currentTimeMillis()
    for(i in 1..2.0.pow(13).toInt()){
        val key = i*512
        h[key] = ""
    }
    val end = System.currentTimeMillis()

    print("Time: ${"%.2f".format((end - start)/1000.0)} seconds | ")
    print("Collision proportion: ${h.getCollisionProportion()} | ")
    print("# collisions: ${h.numCollisions} | ")
    println("# keys: ${h.size()}")

    if(hashPrinting){
        println("----------hash table----------")
        for(l in h.arr){
            println(l)
        }
        println("------------------------------")

    }
    return h.getCollisionProportion()
}
fun main() {

//    println(FAH2Annotated(getObjectBinary(arrayOf(1,2,3)), 32))
//    println(FAH2Annotated(getObjectBinary(arrayOf(2,3,4)), 32))

//    println(FAH4.hash(BigInteger.TWO))
//    println(FAH4.hash(BigInteger("3")))
//    println(FAH4.hash(BigInteger("4")))
//    println(FAH4.hash(BigInteger("5")))
//    println(FAH4.hash(BigInteger("6")))

    //println(FAH3Annotated(getObjectBinary(231234), 16))

//    print("Random hashing - ")
//    checkCollisions({ input -> randomHash(32) }, hashPrinting = false)
//    print("Object hashing - ")
//    checkCollisions({ input -> input.hashCode().toBigInteger() }, hashPrinting = false)
//    print("Mod hashing - ")
//    checkCollisions ({ input -> modHash(getObjectBinary(input), 32) }, hashPrinting = false)
//    print("FAH2 hashing - ")
//    checkCollisions ({ input -> FAH2(getObjectBinary(input), 32) }, hashPrinting = false)
//    print("FAH2c hashing - ")
//    checkCollisions ({ input -> FAH2c(getObjectBinary(input), 32) }, hashPrinting = false)
//    print("FAH3 hashing - ")
//    checkCollisions ({ input -> FAH3(getObjectBinary(input), 32) }, hashPrinting = false)
//    print("FAH4 hashing - ")
//    checkCollisions ({ input -> FAH4.hash(input) }, hashPrinting = false)

    println("Random hash: " + checkAvalanche({randomHash(32)}, 32, false))
    println("Object hashing: " + checkAvalanche({input -> input.hashCode().toBigInteger()}, 32, false))
    println("Mod hashing: " + checkAvalanche({input -> modHash(input, 32) }, 32, false))
    println("FAH2: " + checkAvalanche({input -> FAH2(input, 32) }, 32, false))
    println("FAH 2c: " + checkAvalanche({input -> FAH2c(input, 32) }, 32, false))
    println("FAH2m: " + checkAvalanche({input -> FAH2m(input, 32) }, 32, false))
    println("FAH 3: " + checkAvalanche({input -> FAH3(input, 32) }, 32, false))
    println("FAH 4: " + checkAvalanche({input -> FAH4.FAH4(input) }, 32, false))

}
