package memory

import java.lang.Integer.min
import java.lang.Integer.max

// screen size in number of tiles
const val BG_SIZE = 32

// pass references to the tile sheet and the four background paletteSheet
@ExperimentalUnsignedTypes
class Background (
    private val tileSheet    : Array<Tile>,
    private val paletteSheet : Array<Palette>
) {

    // tells if the buffer associated with this background needs to be redrawn
    var redraw : Boolean = true
        get() {
            if (field) return true

            // return true if a palette used by this background needs to be redrawn
            for ((pal, count) in palSet) {
                if (count <= 0) continue
                if (pal.redraw) return true
            }
            return false
        }


    // data stored in this background
    private val indexTiles      = IntArray      (BG_SIZE * BG_SIZE) {i -> i % (SHEET_SIZE * SHEET_SIZE)}
    private val pointerTiles    = Array<Tile>   (BG_SIZE * BG_SIZE) {i -> tileSheet[i % (SHEET_SIZE * SHEET_SIZE)]}
    private val indexPalettes   = IntArray      (BG_SIZE * BG_SIZE) {i -> i % NB_PAL}
    private val pointerPalettes = Array<Palette>(BG_SIZE * BG_SIZE) {i -> paletteSheet[i % NB_PAL]}


    val tiles    : Array<Tile   > get() = pointerTiles
    val palettes : Array<Palette> get() = pointerPalettes


    // store palettes used in this background so that it is easier to check for redraws
    private val palSet : HashMap<Palette, Int> = HashMap()
    init {
        for (pal in paletteSheet) {
            palSet[pal] = 0
        }
        palSet[paletteSheet[0]] = BG_SIZE * BG_SIZE
    }


    fun setTileAndPalette (x : Int, y : Int, tile : Int, palette : Int) {
        val i = (x and MASK_32) + (y and MASK_32) * BG_SIZE
        val t = tile    and MASK_256
        val p = palette and MASK_8

        indexTiles     [i] = t
        pointerTiles   [i] = tileSheet[t]
        indexPalettes  [i] = p

        val old = pointerPalettes[i]
        val pal = paletteSheet[p]
        pointerPalettes[i] = pal

        // manage redraw
        palSet[old] = max(palSet[old]!! - 1, 0)
        palSet[pal] = palSet[pal]!! + 1
        redraw = true
    }


    fun setTilesHorizontally (x : Int, y : Int, newTiles : IntArray) {
        val start  = (x and MASK_32) + (y and MASK_32) * BG_SIZE
        val length = min(newTiles.size, indexTiles.size - start)

        // /!\ doing that doesn't apply the MASK_256
        newTiles.copyInto(indexTiles, start, 0, start + length)
        for (i in 0..(length - 1)) {
            pointerTiles[start + i] = tileSheet[newTiles[i] and MASK_256] // /!\
        }
        redraw = true
    }

    fun setPalettesHorizontally (x : Int, y : Int, newPalettes : IntArray) {
        val start  = (x and MASK_32) + (y and MASK_32) * BG_SIZE
        val length = min(newPalettes.size, indexTiles.size - start)

        // /!\ doing that doesn't apply the MASK_4
        newPalettes.copyInto(indexTiles, start, 0, start + length)
        for (i in 0..(length - 1)) {
            val old = pointerPalettes[start + i]
            val pal = paletteSheet[newPalettes[i] and MASK_8] // /!\
            pointerPalettes[start + i] = pal

            // manage redraw
            palSet[old] = max(palSet[old]!! - 1, 0)
            palSet[pal] = palSet[pal]!! + 1
        }
        redraw = true
    }

    fun setTilesVertically (x : Int, y : Int, newTiles : IntArray) {
        val start  = (x and MASK_32) + (y and MASK_32) * BG_SIZE
        var index  = start
        var column = 0
        for (i in newTiles) {
            indexTiles  [index] = i
            pointerTiles[index] = tileSheet[i and MASK_256] // /!\
            index += BG_SIZE                   // next row
            if (index >= indexTiles.size) {    // we have reached the bottom
                ++column                       // next column
                if (column >= BG_SIZE) return  // we cannot continue on the right, stop here
                index = start + column         // continue on the right
            }
        }
        redraw = true
    }

    fun setPalettesVertically (x : Int, y : Int, newPalettes : IntArray) {
        val start  = (x and MASK_32) + (y and MASK_32) * BG_SIZE
        var index  = start
        var column = 0
        for (i in newPalettes) {
            indexPalettes  [index] = i
            val old = pointerPalettes[index]
            val pal = paletteSheet[i and MASK_8] // /!\
            pointerPalettes[index] = pal

            // manage redraw
            palSet[old] = max(palSet[old]!! - 1, 0)
            palSet[pal] = palSet[pal]!! + 1

            index += BG_SIZE                   // next row
            if (index >= indexPalettes.size) { // we have reached the bottom
                ++column                       // next column
                if (column >= BG_SIZE) return  // we cannot continue on the right, stop here
                index = start + column         // continue on the right
            }
        }
        redraw = true
    }
}