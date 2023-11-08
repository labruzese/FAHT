import kotlin.math.pow

//Pre-condition: sizeInPow >= 4
fun FAH1(data: Int, sizeInPow: Int){
    val maxSize = 2.0.pow(sizeInPow).toInt()

    var hash = 0
    for(i in 0..data.toString(2).length/2){
        val p =
        val chunk = data shr i*2
        hash = hash xor chunk

    }
    //start with 0000
    //in chunks of 2
        //xor with data in chunks of length
        //rotate-right by the nth prime's sqrt decimal to the nth digit
            //n is the chunk num

}