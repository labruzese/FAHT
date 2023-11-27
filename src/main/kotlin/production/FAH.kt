package production

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.math.BigInteger
import java.util.*


const val BRIGHT_BLUE = "\u001b[94;1m"
const val BRIGHT_MAGENTA = "\u001b[95;1m"
const val BRIGHT_CYAN = "\u001b[96;1m"
const val BRIGHT_WHITE = "\u001b[97;1m"
const val RESET = "\u001b[0m"

fun getObjectBinary(input: Any): BigInteger {
    val out = ByteArrayOutputStream()
    val reader = ObjectOutputStream(out)
    reader.writeObject(input)

    val data = out.toByteArray()
    out.close()
    reader.close()

    return BigInteger(1, data)
}

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

@Deprecated("This is worse than every other FAH in every way")
fun FAH3(input: Any, hashLength: Int): BigInteger {
    val data = getObjectBinary(input)
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it < hashLength }
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