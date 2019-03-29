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

@ExperimentalUnsignedTypes
fun main (args : Array<String>) {

    println("Running Fantasy")

    /*
    val engine = ScriptEngineManager().getEngineByExtension("kts")
    engine.eval("fun test (p : String) { println(\"msg : \" + p) } ")

    for (i in 0..5)
        (engine as Invocable).invokeFunction("test", "hello $i") */

    val window = Window()

    val api = API(window.display)

    for (i in 0..63) {
        api.spr(i,i * 8, i, i, i)
    }

    window.screen.update()
    window.screen.repaint()

}