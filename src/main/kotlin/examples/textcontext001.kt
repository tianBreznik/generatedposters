package examples

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer
import tools.dynamicText

/**
 * This demonstrates the most basic way of writing text
 * //edgeOpacity = (10*(seconds - posterTime))/5;
//backgroundOpacity = 1- edgeOpacity/100;
 */

fun main() = application {

    configure {
        width = 768
        height = 578
    }
    program {
        val font = loadFont("data/fonts/IBMPlexMono-Bold.ttf", 24.0)
        val highlight = loadFont("data/fonts/IBMPlexMono-BoldItalic.ttf", 24.0)

        extend {

            drawer.fontMap = font
            writer {
                box = Rectangle(40.0, 100.0, 300.0, 400.0)
                newLine()
                dynamicText("we can highlight the word the if we want") {
                    if (it == "the") {
                        drawer.fontMap = highlight
                        drawer.fill = ColorRGBa.PINK
                    }
                }
            }
        }
    }
}