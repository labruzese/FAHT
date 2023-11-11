import java.math.BigInteger
fun FAH2(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it < hashLength }
    for (p in primes){
        val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) }
        for (i in dataString.indices step p) {
            val chunk = dataString.substring(i, i + p).toBigInteger(2)
            hash = hash xor chunk
            hash = rotateLeft(hash, hashLength)
        }
    }
    return hash
}

fun FAH2Annotated(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    println("hashLength: $hashLength")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it < hashLength }
    for (p in primes){
        println("--------------------- $p ---------------------")
        val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) }
        for (i in dataString.indices step p) {
            val chunk = dataString.substring(i, i + p).toBigInteger(2)
            print(hash.toString(2))
            print(" xor ${chunk.toString(2)}")
            hash = hash xor chunk
            print(" = ${hash.toString(2)} --> ")
            hash = rotateLeft(hash, hashLength)
            println(hash.toString(2))
        }
    }
    return hash
}

private fun rotateLeft(num : BigInteger, size: Int) : BigInteger {
    val shifted = (num shl 1) and (BigInteger.ONE shl size) - BigInteger.ONE //Mask maintains size
    return shifted or (num shr size - 1)
}


fun main() {
    val data = "11010101100111"
    println("Data: $data")
    FAH2(BigInteger(data, 2), 8).toString(2)
}