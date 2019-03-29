package memory

import java.awt.Color
import java.lang.StringBuilder

const val PAL_NB_COL = 4

class Palette (
    private val colorSpace: IntArray,
    index : Int
) {

    var redraw : Boolean = true

    private val indexes = IntArray (PAL_NB_COL) {i -> (index + i) and MASK_64}
    private val colors  = IntArray (PAL_NB_COL) {i -> colorSpace[(index + i) and MASK_64]}

    // get palette color
    operator fun get(index : Int) : Int = colors[index and MASK_4]

    // set palette color
    operator fun set(index : Int, color : Int) {
        val i : Int = index and MASK_4
        val c : Int = color and MASK_64
        indexes[i] = c
        colors [i] = colorSpace[c]
        redraw = true
    }

    override fun toString(): String {
        val strB = StringBuilder()
        for (color in colors) {
            strB.append(color.toString(16))
            strB.append('\n')
        }
        return strB.toString()
    }

}