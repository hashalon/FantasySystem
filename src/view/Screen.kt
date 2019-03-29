package view

import memory.*
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.min

@ExperimentalUnsignedTypes
class Screen(val display: Display) : JPanel(true) {

    init {
        val dim = Dimension(DISPLAY_SIZE, DISPLAY_SIZE)
        minimumSize   = dim
        preferredSize = dim
        background    = Color.BLACK
    }

    // prepare buffers to speed up drawing to the screen
    var bgBuffer1 = BufferedImage(DISPLAY_SIZE, DISPLAY_SIZE, BufferedImage.TYPE_INT_RGB)
    var bgBuffer2 = BufferedImage(DISPLAY_SIZE, DISPLAY_SIZE, BufferedImage.TYPE_INT_RGB)

    val sprBuffers = Array<BufferedImage>(NB_SPR) { BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB) }

    init {
        // update the data once after having initialized everything
        update()
        repaint()
    }


    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        /* PAINT BACKGROUNDS */

        // find the top left corner of the screen
        val pixSize = pixelSize
        val dspSize = pixSize * DISPLAY_SIZE

        // start position for backgrounds
        val bgStartX = -display.scrollX * pixSize
        val bgStartY = -display.scrollY * pixSize

        // draw each background twice in a chequer board pattern
        g.drawImage(bgBuffer1, bgStartX + 0      , bgStartY + 0      , dspSize, dspSize, null)
        g.drawImage(bgBuffer2, bgStartX + dspSize, bgStartY + 0      , dspSize, dspSize, null)
        g.drawImage(bgBuffer2, bgStartX + 0      , bgStartY + dspSize, dspSize, dspSize, null)
        g.drawImage(bgBuffer1, bgStartX + dspSize, bgStartY + dspSize, dspSize, dspSize, null)

        /* PAINT SPRITES */

        // draw each sprite
        val tilSize = pixSize * TILE_SIZE
        for (i in 0..(NB_SPR - 1)) {
            val spr = display.sprites[i]
            if (!spr.visible) continue

            // place the sprite at the right location
            var x = spr.x * pixSize
            var y = spr.y * pixSize
            var w = tilSize
            var h = tilSize

            // apply flipping
            if (spr.flipHorizontal) {
                x += w
                w = -w
            }
            if (spr.flipVertical  ) {
                y += h
                h = -h
            }

            // draw the sprite
            g.drawImage(sprBuffers[i], x, y, w, h, null)
        }

        /* CROP SCREEN */

        // then crop the screen
        //g.color = Color.black
        //g.drawRect(0,0, startX, dim.height)
        //g.drawRect(0,0, dim.width , startY)
        //g.drawRect(startX + dspSize, 0, startX, dim.height)
        //g.drawRect(0, startY + dspSize, dim.width , startY)
    }

    // iterate through buffers and redraw what needs to be redrawn
    fun update () {
        // swap the backgrounds first if necessary
        if (display.bgSwapped) {
            val tmp   = bgBuffer1
            bgBuffer1 = bgBuffer2
            bgBuffer2 = tmp
        }
        display.bgSwapped = false

        // redraw the backgrounds if necessary
        if (display.background1.redraw) drawBackground(display.background1, bgBuffer1)
        if (display.background2.redraw) drawBackground(display.background2, bgBuffer2)

        // redraw the sprites if necessary
        for (i in 0..(NB_SPR - 1)) {
            val spr = display.sprites[i]
            if (spr.redraw) drawTile(spr.tile, spr.palette, sprBuffers[i])
        }

        // reset all redraw flags
        display.resetRedraw(false)
    }


    // draw background to buffer
    private fun drawBackground (bg : Background, buffer : BufferedImage) {
        var i = 0
        for (y in 0..(BG_SIZE - 1)) { // draw row
        for (x in 0..(BG_SIZE - 1)) { // draw tile
            drawTile(bg.tiles[i], bg.palettes[i], buffer, x * TILE_SIZE, y * TILE_SIZE)
            ++i
        }
        }
    }

    // draw the tile in the buffer
    private fun drawTile (tile : Tile, pal : Palette, buffer: BufferedImage, startX : Int = 0, startY : Int = 0) {
        var i = 0
        for (y in startY..(startY + TILE_SIZE - 1)) { // draw line
        for (x in startX..(startX + TILE_SIZE - 1)) { // draw pixel
            val pix = pal[tile[i]]
            buffer.setRGB(x, y, pix)
            ++i
        }
        }
    }

    // return the size of pixel displayed
    private val pixelSize : Int get() {
        val dim = size
        return min(dim.width, dim.height) / DISPLAY_SIZE
    }

    // adapt the size of the screen to surface available in the window
    fun changeSize (width : Int, height : Int) {
        val scale = min(width, height) / DISPLAY_SIZE
        val dim   = Dimension(DISPLAY_SIZE * scale, DISPLAY_SIZE * scale)
        minimumSize   = dim
        preferredSize = dim
    }
}