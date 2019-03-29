import javax.script.ScriptEngineManager
import javax.swing.JPanel

class Test {

    fun test () {

        /*val manager = ScriptEngineManager()
        for (eng in manager.engineFactories) {
            println(eng.engineName)
        }*/

        with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval("val x = 3")
            val res2 = eval("x + 2")
            println(res2)
        }

    }

}