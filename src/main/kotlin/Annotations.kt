import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.math.BigInteger

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
private val file = File("src/table")
private val table: Array<Array<BigInteger>> by lazy { getTable(file) }
private const val DEFAULT_SIZE = 128 //DO NOT CHANGE unless you know what you're doing
private const val TABLE_SIZE_BITS = 16 //also DON'T CHANGE this; must be even so that we can split into x and y total size is this squared
private const val TABLE_DIMENSION_BITS = TABLE_SIZE_BITS/2
fun FAH4Annotated(input: BigInteger, hashLength: Int = DEFAULT_SIZE) : BigInteger {
    val data = input
    println("hashLength: $hashLength")
    println("data: ${data.toString(2)}")

    var hash = BigInteger.ZERO
    var chunkDonor = data
    for(i in 0..data.bitLength()/ TABLE_SIZE_BITS){
        print("ChunkDonor: ${chunkDonor.toString(2)}  ||  ")
        val chunk =  chunkDonor and  ((BigInteger.ONE shl TABLE_SIZE_BITS)-BigInteger.ONE)
        print("Chunk: ${chunk.toString(2)} || ")

        val x = chunk and ((BigInteger.ONE shl TABLE_DIMENSION_BITS)-BigInteger.ONE)
        val y = chunk shr TABLE_DIMENSION_BITS
        print("[$x][$y]")
        hash = hash xor table[x.toInt()][y.toInt()]
        println()
        chunkDonor = chunkDonor shr TABLE_SIZE_BITS
    }
    val chopped = changeSize(hash, hashLength, DEFAULT_SIZE)
    println("Chop: $hash")
    println("--> $chopped")
    return chopped
}
private fun getTable(f : File) : Array<Array<BigInteger>>{
    val file = FileInputStream(f)
    val `in` = ObjectInputStream(file)

    val table = `in`.readObject() as Array<Array<BigInteger>>

    `in`.close()
    file.close()

    return table
}
fun changeSize(hash: BigInteger, newHashLength: Int, oldHashLength: Int) : BigInteger {
    var newHash = hash
    if(newHashLength > oldHashLength){
        repeat(newHashLength/oldHashLength){
            newHash = newHash shl oldHashLength
            newHash = newHash or FAH4.hash(newHash shr oldHashLength)
        }
    }
    return newHash and (BigInteger.ONE shl newHashLength) - BigInteger.ONE
}

private fun rotateLeft(num : BigInteger, size: Int) : BigInteger {
    val shifted = (num shl 1) and (BigInteger.ONE shl size) - BigInteger.ONE //Mask maintains size
    return shifted or (num shr size - 1)
}
private fun cut(hash: BigInteger, size: Int, randomness: Boolean) : BigInteger {
    val shift = if(randomness) size/3 else size/2
    return (hash shl shift) + (hash shr size - shift) and (BigInteger.ONE shl size) - BigInteger.ONE
}