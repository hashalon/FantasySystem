package memory

class Sprite (
    private val tileSheet : Array<Tile>,
    private val palettes  : Array<Palette>
) {

    var redraw : Boolean = true
        get() {
            if (field) return true
            return pointerPalette.redraw
        }

    var x : Int = 0
    var y : Int = 0

    private var indexTile      : Int     = 0
    private var pointerTile    : Tile    = tileSheet[0]
    private var indexPalette   : Int     = 0
    private var pointerPalette : Palette = palettes [0]

    var flipHorizontal : Boolean = false
    var flipVertical   : Boolean = false
    var visible        : Boolean = false

    val tile    : Tile    get() = pointerTile
    val palette : Palette get() = pointerPalette

    fun setTile (index : Int) {
        val i : Int = index and MASK_256
        indexTile   = i
        pointerTile = tileSheet[i]
        redraw      = true
    }


    fun setPalette (index : Int) {
        val i : Int = index and MASK_8
        indexPalette   = i
        pointerPalette = palettes[i]
        redraw         = true
    }

    /** API **/

    fun api_spr (x : Int, y : Int) {
        this.x = x and MASK_256
        this.y = y and MASK_256
    }

    fun api_spr (t : Int, fh : Boolean, fv : Boolean) {
        setTile(t)
        flipHorizontal = fh
        flipVertical   = fv
    }
}