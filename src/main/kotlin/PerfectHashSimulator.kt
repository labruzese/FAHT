import kotlin.math.pow

fun simulatePerfectHash(items: Int, spaces: Int): Double{
    val arr = Array(spaces){false}
    var numCollisions = 0
    for (i in 0..<items){
        val rand = (Math.random() * i).toInt()
        if(arr[rand]) numCollisions++ else arr[rand] = true
    }
    return numCollisions.toDouble()/spaces
}

fun main() {
    for(i in 0..10){
        println(simulatePerfectHash(2.0.pow(15).toInt(), 2.0.pow(16).toInt()))
    }
}