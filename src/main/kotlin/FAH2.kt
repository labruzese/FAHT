import java.math.BigInteger
const val BRIGHT_BLUE = "\u001b[94;1m"
const val BRIGHT_MAGENTA = "\u001b[95;1m"
const val BRIGHT_CYAN = "\u001b[96;1m"
const val BRIGHT_WHITE = "\u001b[97;1m"
const val RESET = "\u001b[0m"

fun FAH2(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    var hash = BigInteger.ZERO
    val primes = listOf(2,3,5,7,11,13,17).filter { it < hashLength }
    for(k in 0..1) {
        for (p in primes) {
            val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) } //padding
            for (i in dataString.indices step p) {
                val chunk = dataString.substring(i, i + p).toBigInteger(2)
                hash = hash xor chunk
                hash = (hash * BigInteger("16777619")) and (BigInteger.ONE shl hash.bitLength()) - BigInteger.ONE
                hash = rotateLeft(hash, hashLength)
            }
        }
    }
    return hash
}

fun FAH2Annotated(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    println("hashLength: $hashLength")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23).filter { it <= hashLength }
    for (p in primes){
        println("$BRIGHT_CYAN--------------------- $p ---------------------$RESET")
        val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) }
        for (i in dataString.indices step p) {
            val chunk = dataString.substring(i, i + p).toBigInteger(2)
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

fun FAH3(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29).filter { it < hashLength }
    var operation = 0
    for(pass in 0..0){
        for (p in primes) {
            val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) } //padding
            for (i in dataString.indices step p) {
                val chunk = dataString.substring(i, i + p).toBigInteger(2)
                hash = when(operation){
                    0 -> hash or (chunk.toString(2) + "0".repeat((hashLength - chunk.bitLength()))).toBigInteger(2)
                    1 -> hash and (chunk.toString(2) + "1".repeat((hashLength - chunk.bitLength()))).toBigInteger(2)
                    else -> {hash xor chunk}
                }
                operation = (operation + 1) % 3
                hash = rotateLeft(hash, hashLength)
            }
        }
    }
    return hash
}

fun FAH3Annotated(data: BigInteger, hashLength: Int): BigInteger {
    val maxSize = 1 shl hashLength
    println("hashLength: $hashLength")

    var hash = BigInteger.ZERO
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29).filter { it < hashLength }
    var operation = 0
    for(pass in 0..0){
        println("$BRIGHT_WHITE--------------------- pass $pass ---------------------$RESET")
        for (p in primes) {
            println("$BRIGHT_CYAN------------------ $p ------------------$RESET")
            val dataString = data.toString(2).let { it + "0".repeat((p - it.length % p) % p) } //padding
            for (i in dataString.indices step p) {
                val chunk = dataString.substring(i, i + p).toBigInteger(2)
                print(pad(hash, hashLength))
                print(if(operation == 0) " or  " else if(operation == 1) " and " else " xor ")
                print(pad(chunk, p))
                hash = when(operation){
                    0 -> hash or (chunk.toString(2) + "0".repeat((hashLength - chunk.bitLength()))).toBigInteger(2)
                    1 -> hash and (chunk.toString(2) + "1".repeat((hashLength - chunk.bitLength()))).toBigInteger(2)
                    else -> {hash xor chunk}
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

private fun rotateLeft(num : BigInteger, size: Int) : BigInteger {
    val shifted = (num shl 1) and (BigInteger.ONE shl size) - BigInteger.ONE //Mask maintains size
    return shifted or (num shr size - 1)
}


fun main() {
//    val data = "11110101010101010"
//    println("Data: $data")
    println(FAH3Annotated("01110110".toBigInteger(), 8).toString(2))

//    FAH2Annotated(BigInteger(data, 2), 8).toString(2)
//    for(i in 0..100){
//        var d = ""
//        for(j in 0..15){
//            d += (Math.random() * 2).toInt()
//        }
//        println(FAH2(d.toBigInteger(2), 8))
//    }
}