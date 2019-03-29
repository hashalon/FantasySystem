import memory.Tile
import view.Window
import javax.script.Invocable
import javax.script.ScriptEngineManager

/**
 *
 * Fantasy Console that aims at reproducing NES limitations with more fidelity
 * than PICO-8. More colors are available but we are limited to 8 palettes.
 * Drawing on the screen is handled by 2 tiled backgrounds and 64 sprites.
 * The program is coded in Kotlin and use Kotlin as the scripting language.
 *
 * The console will provide interfaces to write programs, draw picture data.
 * The should be a way to define levels.
 * Sounds are not yet defined.
 *
 * */

fun main (args : Array<String>) {

    println("Running Fantasy")

    /*
    val engine = ScriptEngineManager().getEngineByExtension("kts")
    engine.eval("fun test (p : String) { println(\"msg : \" + p) } ")

    for (i in 0..5)
        (engine as Invocable).invokeFunction("test", "hello $i") */

    //val window = Window()

    val tile = Tile()
    tile.assign(0b0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011L, 0b0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011_0001_1011L)

    // TODO: tile doesn't return the correct data for the specified index :(
    println(tile[0])
    println(tile[1])
    println(tile[2])
    println(tile[3])

    println(tile[4])
    println(tile[5])
    println(tile[6])
    println(tile[7])

    println(tile[8])
    println(tile[9])
    println(tile[10])
    println(tile[11])

    println(tile[12])
    println(tile[13])
    println(tile[14])
    println(tile[15])

}