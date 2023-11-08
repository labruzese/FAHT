import java.math.BigInteger

//Pre-condition: sizeInPow >= 4
fun FAH1(data: BigInteger, hashLength: Int) {
    val maxSize = 1 shl hashLength

    var hash = BigInteger("0")
    val primes = listOf<Int>(1) //placeholder
    for (p in primes){
        for (i in 0..data.toString(2).length / p) {
            val chunk = data shr i * 2
            hash = hash xor chunk
            hash = rotateLeft(hash, hashLength)
        }
    }
}

private fun rotateLeft(num : BigInteger, size: Int) : BigInteger{
    var newNum = num shl 1
    newNum += BigInteger.ZERO xor (num shr size)
    return newNum
}