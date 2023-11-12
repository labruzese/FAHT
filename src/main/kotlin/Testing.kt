import java.math.BigInteger
import kotlin.random.Random


fun main() {
    checkRandomness()
//    val h = HashTable(items = arrayListOf(Pair('a', 1)))
//    h['b'] = 2
//    println(h['a'])
//    println(h.set('a', 3))
//    println(h['b'])
//    println(h['c'])
//    println()
//    print(h)
////    val kotlinHashMap
}

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

fun checkRandomness(){
    val hashLength = 16

    val data = "1000101011111101010101010010101001".repeat(1)
    val orgHash = pad(FAH2(data.toBigInteger(2), hashLength), hashLength)
    val changedNums = IntArray(hashLength)
    for(i in 0..<data.length){
        val newData = if(data[i] == '0') data.replaceRange(i, i + 1, "1") else data.replaceRange(i, i + 1, "0")
        val newHash = pad(FAH2(newData.toBigInteger(2), hashLength), hashLength)
        println(compare(orgHash, newHash))
        for(j in orgHash.indices){
            if(orgHash[j] != newHash[j]){
                changedNums[j]++
            }
        }
    }
    for(i in changedNums.indices){
        println("$i digit is changed ${changedNums[i]/data.length.toDouble() * 100}% of the time")
    }
}
