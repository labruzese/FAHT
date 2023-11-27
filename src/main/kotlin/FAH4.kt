import java.io.*
import java.math.BigInteger
import java.util.*

object FAH4 {
    private const val DEFAULT_SIZE = 128 //DO NOT CHANGE unless you know what you're doing
    private const val TABLE_SIZE_BITS = 16 //also DON'T CHANGE this; must be even so that we can split into x and y total size is this squared
    private const val TABLE_DIMENSION_BITS = TABLE_SIZE_BITS/2
    private val file = File("src/table")
    private val table: Array<Array<BigInteger>> by lazy { getTable(file) }

    fun hash(input: Any, hashLength: Int = DEFAULT_SIZE) : BigInteger {
        val data = getObjectBinary(input)

        var hash = BigInteger.ZERO
        var chunkDonor = data
        for(i in 0..data.bitLength()/TABLE_SIZE_BITS){
            val chunk =  chunkDonor and  ((BigInteger.ONE shl TABLE_SIZE_BITS)-BigInteger.ONE)

            val x = chunk and ((BigInteger.ONE shl TABLE_DIMENSION_BITS)-BigInteger.ONE)
            val y = chunk shr TABLE_DIMENSION_BITS

            hash = hash xor table[x.toInt()][y.toInt()]
            chunkDonor = chunkDonor shr TABLE_SIZE_BITS
        }
        return if(hashLength == DEFAULT_SIZE) hash else changeSize(hash, hashLength, DEFAULT_SIZE)
    }

    private fun changeSize(hash: BigInteger, newHashLength: Int, oldHashLength: Int) : BigInteger {
        var newHash = hash
        if(newHashLength > oldHashLength){
            repeat(newHashLength/oldHashLength){
                newHash = newHash shl oldHashLength
                newHash = newHash or hash(newHash shr oldHashLength)
            }
        }
        return newHash and (BigInteger.ONE shl newHashLength) - BigInteger.ONE
    }

    private fun serializeNewTable(f: File) {
        val table = Array(1 shl (TABLE_DIMENSION_BITS-1)) { getOuter() }

        val file = FileOutputStream(f)
        val out = ObjectOutputStream(file)

        out.writeObject(table)

        out.close()
        file.close()
    }

    private fun getOuter(): Array<BigInteger> = Array(1 shl (TABLE_DIMENSION_BITS-1)) { getInner() }
    private fun getInner(): BigInteger = BigInteger(DEFAULT_SIZE, Random())

    private fun getTable(f : File) : Array<Array<BigInteger>>{
        val file = FileInputStream(f)
        val `in` = ObjectInputStream(file)

        val table = `in`.readObject() as Array<Array<BigInteger>>

        `in`.close()
        file.close()

        return table
    }
}