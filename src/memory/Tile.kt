package memory

const val TILE_SIZE = 8


/*
*
* To avoid using too much RAM,
* picture data for tiles is stored as two 'long'
* that contains 64 bits each for a total of 128 bits.
*
* */

class Tile {

    // with two long we can store all binary data
    private var half1 : Long = 0
    private var half2 : Long = 0


    fun assign (h1 : Long, h2 : Long) {
        half1 = h1
        half2 = h2
    }

    operator fun get (index : Int) : Int {
        // select in which 'long' we will have to look for pixel data
        if (index < 32) return (half1 ushr ( index       shl 2)).toInt() and MASK_4
        else            return (half2 ushr ((index - 32) shl 2)).toInt() and MASK_4
    }

    operator fun get (x : Int, y : Int) : Int {
        return get(x + TILE_SIZE * y)
    }

    operator fun set (index : Int, value : Int) {
        val i = index shl 2
        val v : Long = (value and MASK_4).toLong() shl i
        val m : Long = (0b11L shl i).inv()
        if (index < 32) half1 = (half1 and m) or v
        else            half2 = (half2 and m) or v
    }

    operator fun set (x : Int, y : Int, value : Int) {
        set(x + TILE_SIZE * y, value)
    }

    // array where each byte represent a four pixels
    fun loadBinaryData (data : ByteArray) {
        half1 = 0 // reset
        for (i in  0..7) { // fill half1
            val j = i * 8
            half1 = half1 and (data[i].toLong() shl j)
        }
        half2 = 0 // reset
        for (i in 8..15) { // fill half2
            val j = (i - 0x100) * 8
            half2 = half2 and (data[i].toLong() shl j)
        }
    }
}