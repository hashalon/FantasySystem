package memory

import java.lang.StringBuilder

const val TILE_SIZE = 8


/*
*
* To avoid using too much RAM,
* picture data for tiles is stored as two 'long'
* that contains 64 bits each for a total of 128 bits.
*
* */

@ExperimentalUnsignedTypes
class Tile {

    // with two long we can store all binary data
    private var half1 : ULong = 0u
    private var half2 : ULong = 0u

    fun assign (h1 : ULong, h2 : ULong) {
        half1 = h1
        half2 = h2
    }

    operator fun get (index : Int) : Int {
        val i = 64 - (index and MASK_64)
        // select in which 'long' we will have to look for pixel data
        if (index < 32) return (half1 shr ( i       * 2)).toInt() and MASK_4
        else            return (half2 shr ((i - 32) * 2)).toInt() and MASK_4
    }

    operator fun get (x : Int, y : Int) : Int {
        return get(x + TILE_SIZE * y)
    }

    operator fun set (index : Int, value : Int) {
        val sh = 64 - (index and MASK_32) * 2 - 2
        val v : ULong = (value and MASK_4).toULong() shl sh
        val m : ULong = (0b11uL shl sh).inv()
        if (index < 32) half1 = (half1 and m) or v
        else            half2 = (half2 and m) or v
    }

    operator fun set (x : Int, y : Int, value : Int) {
        set(x + TILE_SIZE * y, value)
    }

    // array where each byte represent a four pixels
    fun loadBinaryData (data : ByteArray) {
        half1 = 0u // reset
        for (i in  0..7) { // fill half1
            val sh = 64 - (i + 1) * 8
            half1 = half1 or (data[i].toULong() shl sh)
        }
        half2 = 0u // reset
        for (i in 8..15) { // fill half2
            val sh = 64 - (i - 7) * 8
            half2 = half2 or (data[i].toULong() shl sh)
        }
    }

    override fun toString(): String {
        val str1 = half1.toString(4).padStart(32, '0').chunked(8)
        val str2 = half2.toString(4).padStart(32, '0').chunked(8)
        val strB = StringBuilder()
        for (str in str1) {
            strB.append(str)
            strB.append('\n')
        }
        for (str in str2) {
            strB.append(str)
            strB.append('\n')
        }
        return strB.toString()
    }
}