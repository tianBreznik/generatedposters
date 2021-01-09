package skeletons

import archives.LoadedArticle
import archives.localArchive
import jdk.nashorn.internal.objects.NativeJava
import jdk.nashorn.internal.objects.NativeJava.extend
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.color.rgba
import org.openrndr.draw.loadFont
import org.openrndr.events.Event
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.color.ColorCorrection
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extra.fx.edges.LumaSobel
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer

fun main() = application {
    configure {
        width = 600
        height = 800
    }
    program {
        extend(Screenshots()) {
            scale = 4.0
        }
        val archive = localArchive("archives/example-poetry").iterator()
        var article = archive.next()
        val gui = GUI()

        val onNextArticle = Event<LoadedArticle>()
        val settings = @Description("Settings") object {
            @ActionParameter("Next article")
            fun nextArticle() {
                article = archive.next()
                onNextArticle.trigger(article)
            }
        }

        val composite = compose {
            var background = ColorRGBa.PINK
            onNextArticle.listen {
                background = rgb(Math.random(), Math.random(), Math.random())
            }


            // -- image layer
            layer {
                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }
                post(LumaSobel()){
                    //radius = seconds.toInt();
                    backgroundOpacity -= seconds/1000;
                    edgeOpacity += seconds/500;
                }
            }
            layer {
                draw {
                    drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                }
                post(ColorCorrection())
                {
                    opacity = 0.0;
                //    hue += seconds
                    //if(opacity > 0){
                    //    opacity -= seconds/1000.0;
                    //}
                }
            }


            // -- text layer
        }
        onNextArticle.trigger(article)

        gui.add(settings)
        extend(gui)
        extend {
            composite.draw(drawer)
        }
    }
}