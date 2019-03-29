package memory

import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.awt.image.DataBuffer
import java.lang.StringBuilder
import javax.imageio.ImageIO


const val DISPLAY_SIZE = 256

const val NB_COL = 64
const val NB_SPR = 64
const val NB_PAL =  8

const val SHEET_SIZE = 16

const val MASK_4    = 0b11           // nb colors per palette
const val MASK_8    = 0b111          // nb palettes
const val MASK_16   = 0b1111         //
const val MASK_32   = 0b1111_1       // tiles position
const val MASK_64   = 0b1111_11      // nb colors, nb sprites
const val MASK_128  = 0b1111_111     //
const val MASK_256  = 0b1111_1111    // positions on screen
const val MASK_512  = 0b1111_1111_1  //
const val MASK_1024 = 0b1111_1111_11 //

// while building the display, fill it with default values
class Display {

    // color space referenced by palettes
    val colorSpace = IntArray(NB_COL)
    init { /* LOAD DEFAULT PALETTE */
        // load palette from a text file that can be easily edited
        var i = 0
        // TODO: verify file before loading
        val palFile = javaClass.getResource("/palette.pal").readText()
        for (line in palFile.split("\n")) {
            if (!line.startsWith('#')) continue

            // insert the color into the array
            colorSpace[i] = line.substring(1, 7).toInt(16)
            ++i
        }
    }

    // sprite sheets
    val foregroundSheet = Array<Tile>(SHEET_SIZE * SHEET_SIZE) {Tile()}
    val backgroundSheet = Array<Tile>(SHEET_SIZE * SHEET_SIZE) {Tile()}
    init { /* LOAD DEFAULT SPRITE SHEETS */
        val sheetFile = javaClass.getResource("/spritesheet.png")
        val picture   = ImageIO.read(sheetFile)

        // picture needs to be indexed
        if (picture.type != BufferedImage.TYPE_BYTE_BINARY) {
            error("Sprite sheet is not an indexed PNG")
            //return
        }

        // TODO: verify dimensions of the image

        // load both tile sets
        loadPictureData(picture,0x000, false)
        loadPictureData(picture,0x100, true )
    }

    // we have two different palettes for sprites and backgrounds
    val palettes = Array<Palette>(NB_PAL) {Palette(colorSpace)}

    // declare background data
    var background1 = Background(backgroundSheet, palettes)
    var background2 = Background(backgroundSheet, palettes)

    // define scrolling of the background
    var scrollX : Int = 0
    var scrollY : Int = 0

    // declare sprites
    val sprites = Array<Sprite>(NB_SPR) {Sprite(foregroundSheet, palettes)}

    // tells when the backgrounds have been swapped
    var bgSwapped : Boolean = false

    // simply swap the two backgrounds to obtain a continuous scrolling
    fun swapBg () {
        val tmp     = background1
        background1 = background2
        background2 = tmp
        bgSwapped   = true
    }

    // reset all redraw
    fun resetRedraw (value : Boolean) {
        for (pal in palettes) pal.redraw = value
        for (spr in sprites ) spr.redraw = value
        background1.redraw = value
        background2.redraw = value
    }


    // load picture data into the specified sprite sheet
    // use start to start loading at a specific row
    fun loadPictureData (picture : BufferedImage, start : Int, bgLoad : Boolean) {
        val tileset = if (bgLoad) backgroundSheet else foregroundSheet

        val nb_tiles = SHEET_SIZE * SHEET_SIZE - 1
        val nb_pix   = TILE_SIZE  * TILE_SIZE  / 4 - 1

        for (i in 0..nb_tiles) { // load foreground data
            val x : Int =  i          % SHEET_SIZE
            val y : Int = (i + start) / SHEET_SIZE
            val rect    = Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE)
            val picData = picture.getData(rect).dataBuffer

            // load the tile data into an array of bytes
            val pix = ByteArray(TILE_SIZE * TILE_SIZE / 4)
            for (j in 0..nb_pix) {
                // we can only extract integers
                val data = picData.getElem(j)
                pix[j]   = data.toByte()
            }

            // finally set the picture data into the tile
            tileset[i].loadBinaryData(pix)
        }
    }

    override fun toString(): String {
        val strB = StringBuilder()
        strB.append("Color Space:\n")
        for (color in colorSpace) {
            strB.append(color.toString(16))
            strB.append('\n')
        }

        strB.append("\nPalettes:\n")
        for (palette in palettes) {
            strB.append(palette)
            strB.append('\n')
        }

        // TODO: background, tiles, sprites...

        return strB.toString()
    }

}