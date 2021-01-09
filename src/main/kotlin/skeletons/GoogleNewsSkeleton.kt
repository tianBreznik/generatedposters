package skeletons

import archives.GoogleNewsEndPoint
import archives.LoadedArticle
import archives.googleNewsSequence
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.openrndr.application
import org.openrndr.draw.loadFont
import org.openrndr.events.Event
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.launch
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer

/*
    Before you can use this you have to request an API key from  https://newsapi.org/s/google-news-api
    You will receive a key within minutes. That key should be placed in src/main/resources/googlenews.properties
 */

fun main() = application {
    configure {
        width = 600
        height = 800
    }
    program {

        // -- per country
        val archive = googleNewsSequence(GoogleNewsEndPoint.TopHeadlines, query = "domestic abuse", country = "us").iterator()

        // -- per query
        //val archive = googleNewsSequence(GoogleNewsEndPoint.Everything, query = "corona").iterator()
        var article = archive.next().load()
        val gui = GUI()

        val onNextArticle = Event<LoadedArticle>()
        val settings = @Description("Settings") object {
            @ActionParameter("Next article")
            fun nextArticle() {
                val next = GlobalScope.async {
                    archive.next()
                }

                launch {
                    var newArticle = next.await().load()
                    article.destroy()
                    article = newArticle
                    onNextArticle.trigger(newArticle)
                }
            }
        }

        val composite = compose {
            // -- image layer
            layer {
                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }
            }

            // -- text layer
            layer {
                val font = loadFont("data/fonts/IBMPlexMono-Bold.ttf", 32.0)
                draw {
                    if (article.texts.isNotEmpty()) {
                        drawer.fontMap = font
                        writer {
                            box = Rectangle(40.0, 40.0, width - 80.0, height - 80.0)
                            gaplessNewLine()
                            text(article.texts[0])
                        }
                    }
                }
                post(DropShadow()).addTo(gui, "2. Drop shadow")
            }
        }
        onNextArticle.trigger(article)


        gui.add(settings)
        extend(gui)
        extend {
            composite.draw(drawer)
        }
    }
}