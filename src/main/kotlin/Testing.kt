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

fun checkAvalanche(hashFunc: (Any) -> BigInteger, printing: Boolean = false): Double{
    val hashLength = 128

    val data: String = "11111111".repeat(1)
    val orgHash: String = pad(hashFunc(data), hashLength)
    val changedNums = IntArray(hashLength)

    if(printing) println("Data: ${getObjectBinary(data)}")
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
        println("$newHash   --- digit changed: $i")
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

fun checkCollisions(hashFunc: (Any) -> BigInteger, hashPrinting: Boolean = false): Double{
    val h = HashTable<String, String>(hashFunc, initialStorage = 2.0.pow(13).toInt())
    val start = System.currentTimeMillis()
    for(i in 1..2.0.pow(13).toInt()){
        val key = "$i + penguin"
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

    print("Object hashing - ")
    checkCollisions({ input -> input.hashCode().toBigInteger() }, hashPrinting = false)
    print("FAH2 hashing - ")
    checkCollisions ({ input -> FAH2(getObjectBinary(input), 32) }, hashPrinting = false)
    print("FAH2c hashing - ")
    checkCollisions ({ input -> FAH2c(getObjectBinary(input), 32) }, hashPrinting = false)
//    print("FAH4 hashing - ")
//    checkCollisions ({ input -> FAH4.hash(input) }, hashPrinting = false)

    //checkAvalanche({input -> FAH2(getObjectBinary(input), 32) }, true)
}