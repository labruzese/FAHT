import java.io.*
import java.math.BigInteger
import java.util.*


const val BRIGHT_BLUE = "\u001b[94;1m"
const val BRIGHT_MAGENTA = "\u001b[95;1m"
const val BRIGHT_CYAN = "\u001b[96;1m"
const val BRIGHT_WHITE = "\u001b[97;1m"
const val RESET = "\u001b[0m"

fun FAH2(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    var hash = BigInteger.ZERO
    val primes = listOf(2,3,5,7, 11, 13, 17, 19, 23).filter { it < hashLength }
    for (p in primes) {
        var chunkDonor = data
        for (i in 0..(data.bitLength() - 1) / p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p
            hash = hash xor chunk
            hash = rotateLeft(hash, hashLength)
        }
    }

    return hash
}
fun FAH2Annotated(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    println("hashLength: $hashLength")
    println("data: ${data.toString(2)}")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        var chunkDonor = data
        println("$BRIGHT_CYAN--------------------- $p ---------------------$RESET")
        for (i in 0..(data.bitLength()-1)/p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p

            print(pad(hash, hashLength))
            print(" xor ${pad(chunk, p)}")
            hash = hash xor chunk
            print(" = ${pad(hash, hashLength)} --> ")
            hash = rotateLeft(hash, hashLength)
            println(pad(hash, hashLength))
        }
    }
    return hash
}
fun FAH2m(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        var chunkDonor = data
        for (i in 0..(data.bitLength()-1)/p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p
            hash = hash xor chunk
            hash = rotateLeft(hash, hashLength)
            hash = (hash * BigInteger("16777619")) and (BigInteger.ONE shl hash.bitLength()) - BigInteger.ONE
        }
    }
    return hash
}
fun FAH2mAnnotated(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    println("hashLength: $hashLength")
    println("data: ${data.toString(2)}")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        var chunkDonor = data
        println("$BRIGHT_CYAN--------------------- $p ---------------------$RESET")
        for (i in 0..(data.bitLength()-1)/p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p

            print(pad(hash, hashLength))
            print(" xor ${pad(chunk, p)}")
            hash = hash xor chunk
            print(" = ${pad(hash, hashLength)} --> ")
            hash = rotateLeft(hash, hashLength)
            print("${pad(hash, hashLength)} * 16888619 = ")
            hash = (hash * BigInteger("16777619")) and (BigInteger.ONE shl hash.bitLength()) - BigInteger.ONE
            println(pad(hash, hashLength))
        }
    }
    return hash
}
fun FAH2c(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        var chunkDonor = data
        for (i in 0..(data.bitLength()-1)/p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p
            hash = hash xor chunk
            hash = rotateLeft(hash, hashLength)
            hash = cut(hash, hashLength, chunk shr p - 1 == BigInteger.ONE)
        }
    }
    return hash
}
fun FAH2cAnnotated(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    println("hashLength: $hashLength")
    println("data: ${data.toString(2)}")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        var chunkDonor = data
        println("$BRIGHT_CYAN--------------------- $p ---------------------$RESET")
        for (i in 0..(data.bitLength()-1)/p) {
            val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
            chunkDonor = chunkDonor shr p

            print(pad(hash, hashLength))
            print(" xor ${pad(chunk, p)}")
            hash = hash xor chunk
            print(" = ${pad(hash, hashLength)} --> ")
            hash = rotateLeft(hash, hashLength)
            print("${pad(hash, hashLength)} \\${(chunk shr p - 1) + BigInteger.TWO}\\> ")
            hash = cut(hash, hashLength, chunk shr p - 1 == BigInteger.ONE)
            println(pad(hash, hashLength))
        }
    }
    return hash
}
fun FAH3(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29).filter { it < hashLength }
    var operation = 0
    for(pass in 0..0){
        for (p in primes) {
            var chunkDonor = data
            for (i in 0..(data.bitLength()-1)/p) {
                val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
                chunkDonor = chunkDonor shr p
                hash = when(operation){
                    0 -> hash or chunk
                    1 -> hash and (((1 shl p) - 1).toBigInteger() xor (chunk.inv()))
                    else -> hash xor chunk
                }
                operation = (operation + 1) % 3
                hash = rotateLeft(hash, hashLength)
            }
        }
    }
    return hash
}
fun FAH3Annotated(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    println("hashLength: $hashLength")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29).filter { it < hashLength }
    var operation = 0
    for(pass in 0..0){
        println("$BRIGHT_WHITE--------------------- pass $pass ---------------------$RESET")
        for (p in primes) {
            println("$BRIGHT_CYAN------------------ $p ------------------$RESET")
            var chunkDonor = data
            for (i in 0..(data.bitLength()-1)/p) {
                val chunk = chunkDonor and ((1 shl p) - 1).toBigInteger()
                chunkDonor = chunkDonor shr p
                print(pad(hash, hashLength))
                print(if(operation == 0) " or  " else if(operation == 1) " and " else " xor ")
                print(pad(chunk, p))
                hash = when(operation){
                    0 -> hash or chunk
                    1 -> hash and (((1 shl p) - 1).toBigInteger() xor (chunk.inv()))
                    else -> hash xor chunk
                }
                operation = (operation + 1) % 3
                print(" = ${pad(hash, hashLength)} --> ")
                hash = rotateLeft(hash, hashLength)
                println(pad(hash, hashLength))
            }
        }
    }
    return hash
}
fun modHash(input: Any, size: Int) : BigInteger {
    val data = getObjectBinary(input)
    return data and (BigInteger.ONE shl size) - BigInteger.ONE
}
fun randomHash(size: Int) : BigInteger = BigInteger(size, Random())

private fun rotateLeft(num : BigInteger, size: Int) : BigInteger {
    val shifted = (num shl 1) and (BigInteger.ONE shl size) - BigInteger.ONE //Mask maintains size
    return shifted or (num shr size - 1)
}
private fun cut(hash: BigInteger, size: Int, randomness: Boolean) : BigInteger {
    val shift = if(randomness) size/3 else size/2
    return (hash shl shift) + (hash shr size - shift) and (BigInteger.ONE shl size) - BigInteger.ONE
}

object FAH4 {
    private val file = File("src/table")
    private val table: Array<Array<BigInteger>> by lazy { getTable(file) }

    fun FAH4(input: Any) : BigInteger{
        val data = getObjectBinary(input)
        var hash = BigInteger.ZERO
        var chunkDonor = data
        for(i in 0..data.bitLength()){
            chunkDonor = chunkDonor shr 16
            val chunk =  chunkDonor and BigInteger("FFFF",16) //each f is 1111

            val x = chunk and BigInteger("FF", 16)
            val y = chunk shr 8
            //print("x:$x y:$y .. ")

            hash = hash xor table[x.toInt()][y.toInt()]
        }
        return hash
    }

    private fun serializeNewTable(f: File) {
        val table = Array(256) { getOuter() }

        val file = FileOutputStream(f)
        val out = ObjectOutputStream(file)

        out.writeObject(table)

        out.close()
        file.close()
    }

    private fun getOuter(): Array<BigInteger> = Array(256) { getInner() }
    private fun getInner(): BigInteger = BigInteger(128, Random())

    private fun getTable(f : File) : Array<Array<BigInteger>>{
        val file = FileInputStream(f)
        val `in` = ObjectInputStream(file)

        val table = `in`.readObject() as Array<Array<BigInteger>>

        `in`.close()
        file.close()

        return table
    }

    fun main() {
        println(FAH4.FAH4(BigInteger.ONE))
        println(FAH4.FAH4(BigInteger.TWO))
        println(FAH4.FAH4(BigInteger("1111111",2)))
        println(FAH4.FAH4(BigInteger("1111110",2)))
        println(FAH4.FAH4(BigInteger("1111101",2)))
        println(FAH4.FAH4(BigInteger("1111011",2)))
        println(FAH4.FAH4(BigInteger("1110111",2)))
        println(FAH4.FAH4(BigInteger("1101111",2)))
        println(FAH4.FAH4(BigInteger("1011111",2)))
        println(FAH4.FAH4(BigInteger("0111111",2)))
    }
}
fun getObjectBinary(input: Any): BigInteger {
    val out = ByteArrayOutputStream()
    val reader = ObjectOutputStream(out)
    reader.writeObject(input)

    val data = out.toByteArray()

    out.close()
    reader.close()

    return BigInteger(1, data)
}
fun main() {
    val hash = BigInteger("111101", 2)
    println(FAH2cAnnotated(getObjectBinary("penguins:)"), 16))
    /*
    //FAH4.main()
    //println(getObjectBinary("1111111").toString(2))
    println("1 " + FAH4.hash(getObjectBinary("1111111")))
    println("2 " + FAH4.hash(getObjectBinary("1111110")))
    println("3 " + FAH4.hash(getObjectBinary("1111101")))
    println("4 " + FAH4.hash(getObjectBinary("1111011")))
    //println(getObjectBinary("1110111").toString(2))

    println("5 " + FAH4.hash(getObjectBinary("1110111")))
    //println(getObjectBinary("1101111").toString(2))

    println("6 " + FAH4.hash(getObjectBinary("1101111")))
    println("7 " + FAH4.hash(getObjectBinary("1011111")))
    println("8 " + FAH4.hash(getObjectBinary("0111111")))

     */
}