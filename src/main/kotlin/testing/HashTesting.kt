package testing

import production.*
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.log10
import kotlin.math.pow

//Pre-condition: size >= num.bitLength()
fun pad(num: BigInteger, size: Int): String{
    return "0".repeat(size-num.toString(2).length) + num.toString(2)
}

//Pre-condition: s1 and s2 are the same length
private fun countDifferences(s1: String, s2: String): Int{
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
        if(printing) print("" + countDifferences(orgHash, newHash) + ",")
        for(j in orgHash.indices){
            if(orgHash[j] != newHash[j]){
                changedNums[j]++
            }
        }
    }
    if(printing) {
        println()
        for (i in changedNums.indices) {
            println("$i digit is changed ${"%.2f".format(changedNums[i] / data.length.toDouble() * 100)}% of the time")
        }
    }

    var total = 0.0
    for(i in changedNums){
        total += i
    }
    return (total / data.length.toDouble()) / hashLength
}
fun compareAvalanche(){
    println("Random hash: " + checkAvalanche({ randomHash(32) }, 32, false))
    println("Object hashing: " + checkAvalanche({input -> input.hashCode().toBigInteger()}, 32, false))
    println("Mod hashing: " + checkAvalanche({input -> modHash(input, 32) }, 32, false))
    println("FAH2: " + checkAvalanche({input -> FAH2(input, 32) }, 32, false))
    println("FAH 2c: " + checkAvalanche({input -> FAH2c(input, 32) }, 32, false))
    println("FAH 4: " + checkAvalanche({input -> FAH4.hash(input, 32) }, 32, false))
}


fun<K : Any> checkCollisions(hashFunc: (Any) -> BigInteger, generateKey: (index: Int) -> K, hashPrinting: Boolean = false): Double{
    val numItems = 2.0.pow(13).toInt()

    val h = HashTable<K, Boolean>(expectedOccupancy = 2, hashFunc = hashFunc)
    val start = System.currentTimeMillis()
    for(i in 0..<numItems){
        val key = generateKey(i)
        h[key] = true
    }
    val end = System.currentTimeMillis()

    print("Time: ${"%.2f".format((end - start)/1000.0)} seconds | ")
    print("Collision proportion: ${h.getCollisionProportion().toString().take(7).padEnd(7,' ')} | ")
    print("# collisions: ${h.collisions.toString().padStart(log10(numItems.toDouble()).toInt() + 1,' ')} | ")
    print("# keys: ${h.size.toString().padEnd(4)} | ")
    println("Table Size: ${h.arr.size}")

    if(hashPrinting){
        println("----------hash table----------")
        println(h.arr.joinToString("\n"))
        println("------------------------------")
    }
    return h.getCollisionProportion()
}

fun keysCollisionsSummary(hashFunc: (Any) -> BigInteger){
    for (i in 0..8) {
        keyGenerationPrinting(i)
        print(" - ")

        val keyFunc: (Int) -> Any = when (i) {
            0 -> { index -> index}
            1 -> { index -> index * 5 }
            2 -> { index -> index * 1024 }
            3 -> { index -> index % 64}
            4 -> { _ -> Math.random() }
            5 -> { _ -> (Math.random() * 100000000).toInt() }
            6 -> { _ -> (Math.random()).toString() }
            7 -> { _ -> (Math.random() * 100).toInt().toString().repeat((Math.random() * 100).toInt()) }
            8 -> { index -> getLine(index)}
            else -> throw IllegalArgumentException("Invalid iteration: $i")
        }
        checkCollisions(hashFunc, keyFunc)
    }
}
fun keyGenerationPrinting(index: Int){
    val name = (when (index) {
        0 -> "i"
        1 -> "i*5"
        2 -> "i*1024"
        3 -> "i%69"
        4 -> "randDouble"
        5 -> "randIntStr"
        6 -> "randDoubleStr"
        7 -> "randRepeatRand3DigitStr"
        8 -> "lines[index]"
        else -> "Invalid index"
    })
    print(name.padStart("randRepeatRand3DigitStr".length, ' '))
}
fun hashKeysCollisionSummary(){
    //println("--------------------Random Hashing--------------------")
    //keysCollisionsSummary{ randomHash(32) }
//    println("--------------------Object Hashing--------------------")
    keysCollisionsSummary{ input -> input.hashCode().toBigInteger() }
    println("--------------------Mod Hashing--------------------")
    keysCollisionsSummary{ input -> modHash(input, 32) }
//    println("--------------------FAH2 Hashing--------------------")
//    keysCollisionsSummary{ input -> FAH2(input, 32) }
    //println("--------------------FAH2c Hashing--------------------")
   // keysCollisionsSummary{ input -> FAH2c(input, 32) }
//    println("--------------------FAH4 Hashing--------------------")
//    keysCollisionsSummary{ input -> FAH4.hash(input, 32) }
    println("--------------------FAH4a Hashing--------------------")
    keysCollisionsSummary{ input -> FAH4.hashA(input, 32) }
}

fun getLine(index: Int) : String {
    return text[index].trim()
}
const val path = "src/main/kotlin/testing/shakespeare.txt"
val text: MutableList<String> = Files.readAllLines(Paths.get(path))
fun main() {
    hashKeysCollisionSummary()
}