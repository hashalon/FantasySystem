package view

import memory.DISPLAY_SIZE
import memory.Display
import java.awt.*
import javax.swing.JFrame
import javax.swing.JPanel

class Window() : JFrame("Fantasy System") {

    val display = Display()
    val screen  = Screen (display)

    init {
        // behaviour of the window
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val base = JPanel(GridBagLayout())
        base.add(screen, GridBagConstraints())
        base.background = Color.BLACK
        add(base)


        val minDim  = Dimension(DISPLAY_SIZE, DISPLAY_SIZE)
        minimumSize = minDim
        isVisible   = true
        background  = Color.BLACK

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

}