import memory.*

/*
*
* Expose functions available to the user
*
* */

class API (val display: Display) {

    /* SPRITE */

    fun spr (i : Int, v : Boolean) {
        val spr = display.sprites[i and MASK_64]
        spr.visible = v
    }
    fun spr (i : Int, x : Int, y : Int) {
        val spr = display.sprites[i and MASK_64]
        spr.api_spr(x, y)
        spr.visible = true
    }
    fun spr (i : Int, x : Int, y : Int, t : Int, fh : Boolean = false, fv : Boolean = false) {
        val spr = display.sprites[i and MASK_64]
        spr.api_spr(x, y)
        spr.api_spr(t, fh, fv)
        spr.visible = true
    }
    fun spr (i : Int, x : Int, y : Int, t : Int, p : Int, fh : Boolean = false, fv : Boolean = false) {
        val spr = display.sprites[i and MASK_64]
        spr.api_spr(x, y)
        spr.api_spr(t, fh, fv)
        spr.setPalette(p)
        spr.visible = true
    }


    /* BACKGROUND */

    fun swp () {
        display.swapBg()
    }
    fun bg (x : Int, y : Int, t : Int, p : Int, bg : Boolean = false) {
        val b = if (bg) display.background2 else display.background1
        b.setTileAndPalette(x, y, t, p)
    }
    fun bg (x : Int, y : Int, ts : IntArray, ps : IntArray, bg : Boolean = false, vt : Boolean = false) {
        val b = if (bg) display.background2 else display.background1
        if (vt) {
            b.setTilesVertically   (x, y, ts)
            b.setPalettesVertically(x, y, ps)
        } else {
            b.setTilesHorizontally   (x, y, ts)
            b.setPalettesHorizontally(x, y, ps)
        }
    }
    fun scl (x : Int, y : Int) {
        display.scrollX = x and MASK_256
        display.scrollY = y and MASK_256
    }


    /* PALETTE */

    fun pal (p : Int, i : Int, c : Int) {
        val pal = display.palettes[p and MASK_8]
        pal[i] = c
    }
}