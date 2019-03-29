package view

import memory.DISPLAY_SIZE
import memory.Display
import java.awt.*
import java.awt.event.ComponentAdapter
import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.event.ComponentEvent
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities


@ExperimentalUnsignedTypes
class Window : JFrame("Fantasy System") {

    val display = Display()
    val screen  = Screen (display)

    private val base = JPanel(GridBagLayout())

    init {
        // behaviour of the window
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        base.add(screen, GridBagConstraints())
        base.background  = Color.BLACK
        base.minimumSize = Dimension(DISPLAY_SIZE + 8, DISPLAY_SIZE + 8)
        add(base)
        pack()

        minimumSize = Dimension(size.width + 8, size.height + 8)
        background  = Color.BLACK
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val window = this
        addWindowStateListener { e : WindowEvent ->
            if ((e.newState and Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                screen.changeSize(base.size.width, base.size.height)

                SwingUtilities.updateComponentTreeUI(base)
            }
        }
        contentPane.addComponentListener(object : ComponentAdapter () {
            override fun componentResized(e: ComponentEvent?) {
                screen.changeSize(base.size.width, base.size.height)

                SwingUtilities.updateComponentTreeUI(base)
            }
        })

        isVisible = true
    }
}