package GenerativePosters
import FilmGrain
import archives.LoadedArticle
import archives.localArchive
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.color.rgba
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.events.Event
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.ChromaticAberration
import org.openrndr.extra.fx.color.LumaMap
import org.openrndr.extra.fx.edges.LumaSobel
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.math.Vector2
import org.openrndr.math.mod
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer
import tools.dynamicText2
import tools.dynamicText
import java.lang.Math.PI
import java.lang.Math.cos
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
        var posterTime = 0.0;
        val settings = @Description("Settings") object {
            @ActionParameter("Next article")
            fun nextArticle() {
                article = archive.next()
                onNextArticle.trigger(article)
            }
        }
        val composite = compose {
            var background = ColorRGBa.BLACK
            onNextArticle.listen {
                posterTime = seconds
                background = rgb(Math.random(), Math.random(), Math.random())
            }
            layer {
                // -- image layer
                /*layer {
                    draw {
                        var image = loadImage("data/images/bg2.jpg")
                        if (article.images.isNotEmpty()) {
                            drawer.imageFit(image, 0.0, 0.0, width * 1.0, height * 1.0)
                        }
                    }
                    post(ChromaticAberration()) {
                        this.aberrationFactor = cos(seconds * 0.5 * PI * 0.8) * 5.0
                    }
                }*/
                // -- text layer
                layer {
                    layer {
                        val font = loadFont("data/fonts/HelveticaNow.otf", 24.0, contentScale = 1.5)
                        val highlight = loadFont("data/fonts/HelveticaNow.otf", 24.0, contentScale = 1.5)
                        val specialWords =
                            setOf("abusing", "hitting", "hit", "hurd", "physically", "punch", "face", "physical", "crying", "aggresive","verbally","fights","injuries","abuses","angry","bad","chokes","spits","slaps","punches")

                        draw {
                            drawer.fontMap = font
                            drawer.fill = ColorRGBa.WHITE
                            val coordinates = mutableListOf<Vector2>()
                            if (article.texts.isNotEmpty()) {
                                writer {
                                    ellipsis = null
                                    box = Rectangle(
                                        width / 15.0,
                                        width / 15.0,
                                        width - (width / 15.0 * 2.0),
                                        width - (width / 15.0 * 2.0)
                                    )
                                    newLine()
                                    dynamicText2(
                                        article.texts[0]
                                    ) {
                                        if (it in specialWords) {
                                            drawer.fontMap = highlight
                                            drawer.fill = ColorRGBa.RED
                                            coordinates.add(Vector2(cursor.x, cursor.y))
                                            it.toUpperCase()
                                        } else if (it in specialWords) {
                                            it
                                        } else {
                                            drawer.fill = ColorRGBa.WHITE.opacify(1.0 - (seconds-posterTime)/50)
                                            it
                                        }
                                    }
                                }
                                drawer.fill = null
                                drawer.strokeWeight = 1.3
                                drawer.stroke = ColorRGBa.WHITE.opacify(0.0 + (seconds-posterTime)/50)
                                val lineSegments = mutableListOf<Vector2>()
                                for (coordinate1 in coordinates) {
                                    //for(coordinate2 in coordinates) {
                                        lineSegments.add(coordinate1)
                                        //lineSegments.add(coordinate2)
                                    //}
                                }
                                drawer.lineSegments(lineSegments)
                                drawer.stroke = ColorRGBa.WHITE
                                drawer.rectangle(35.0,45.0,530.0,800.0)
                            }
                        }
                    }

                    //post(ApproximateGaussianBlur()) {
                    //    this.sigma = Math.cos(seconds * Math.PI * 0.8) * 10.0 + 10.1
                    //}
                }
                layer {
                    layer {
                        val image = loadImage("data/images/eyes.png")
                        draw {
                            if (article.images.isNotEmpty()) {
                                drawer.image(
                                    article.images[0],
                                    (width / 15.0) * 2,
                                    width / 15.0 + ((width - (width / 15.0 * 4.0)) / 3),
                                    width - ((width / 15.0) * 4),
                                    (width / 15.0) + ((width - (width / 15.0 * 2.0)) / 3)
                                )
                            }
                        }
                        post(LumaSobel()) {
                            //radius = seconds.toInt();
                            edgeColor = ColorRGBa.RED
                            backgroundOpacity -= (seconds - posterTime) / 3000;
                            edgeOpacity += (seconds - posterTime) / 500;
                            println("stats")
                            println((seconds - posterTime))
                            println(backgroundOpacity)
                            println(edgeOpacity)
                            //edgeOpacity = (10*(seconds - posterTime))/5;
                            //backgroundOpacity = 1- edgeOpacity/100;
                        }.addTo(gui)
                    }
                }.addTo(gui, "layer 2")
            }
            post(FilmGrain()) {
                this.grainStrength = Math.cos((seconds-posterTime) * Math.PI * 0.8) * 1.0;
            }
            /*layer {
                //val fontSmall = loadFont("data/fonts/HelveticaNow.otf", 15.0)
                val highlight = loadFont("data/fonts/IBMPlexMono-Bold.ttf", 40.0)
                val fontLarge = loadFont("data/fonts/HelveticaNow.otf", 65.0)
                val specialWords = setOf("STOP", "DOMESTIC","VIOLENCE")
                val otherWords = setOf("Say","To")
                draw {
                    //drawer.fontMap = fontSmall
                    writer {
                        box = Rectangle(40.0, 550.0, width - 80.0, height - 80.0)
                        //text(article.texts[0])
                        drawer.fontMap = highlight
                        newLine()
                        //text(article.texts[0])
                        dynamicText("Say STOP To DOMESTIC VIOLENCE")
                        {
                            if (it in otherWords) {
                                drawer.fontMap = fontLarge
                                drawer.fill = ColorRGBa.WHITE
                                drawer.translate(0.0, Math.sin(seconds) * 3.0)
                            } else if (it in specialWords) {
                                drawer.fill = ColorRGBa.RED
                                tracking = Math.cos(seconds) * 20.0 + 20.0
                            }
                        }
                    }
                }
                post(DropShadow()).addTo(gui, "2. Drop shadow")
            }*/
            layer {
                val helplines =
                    listOf("Austria: 0800 222 555", "Bulgaria: +359 2 981 76 86", "Croatia: +385800 55 44", "Cyprus: +3571440", "Turkey: +90 212 656 9696", "Denmark: +45 70 20 30 82", "Georgia: +995 309 903", "Finland: +358 800 02400", "France: +33800 05 95 95", "Germany: +498000 116 016","Greece: +30197","Hungary: +36 4 06 30 006","Ireland: +3531800 341 900","Albania: +355 422 3340","Macedonia: +389 15 315","Slovenia: +38680 11 55", "Netherlands: +31800 2000", "Romania: +40262 25 07 70", "Slovakia: +421800 212 212", "Sweden: +4620 1010")
                val font = loadFont("data/fonts/HelveticaNow.otf", 15.0)
                //drawer.fill = null
                draw {
                    var index = 0;
                    drawer.stroke = ColorRGBa.WHITE
                    drawer.fontMap = font
                    for(j in 0 until 20){
                        if(j <= 3){
                            writer {
                                box = Rectangle(37.0 + 150 * mod(j, 5), 785.0, 150.0, 20.0)
                                text(helplines[j])
                            }

                        }
                        else if (j > 3 && j <= 10 ){
                            drawer.isolated {
                                drawer.translate(width/2.0, height/2.0)
                                drawer.rotate(90.0)
                                drawer.translate(-width/2.0, -height/2.0)
                                writer{
                                    if(j != 9){
                                        box = Rectangle(-75.0 + 150 * mod(j - 4, 7), 687.0, 150.0, 20.0)
                                        text(helplines[j])
                                    }
                                }
                            }

                        }
                        else if (j > 10 && j <= 14){
                            writer{
                                box = Rectangle(27.0 + 150 * mod(j - 11, 7), 20.0, 150.0, 20.0)
                                text(helplines[j])
                            }
                        }
                        else{
                            drawer.isolated {
                                drawer.translate(width/2.0, height/2.0)
                                drawer.rotate(-90.0)
                                drawer.translate(-width/2.0, -height/2.0)
                                writer{
                                    box = Rectangle(-45.0 + 150 * mod(j - 15, 7), 687.0, 150.0, 20.0)
                                    text(helplines[j])
                                }
                            }
                        }
                        //index += 1
                    //}
                   }
                    drawer.fill = null
                    drawer.rectangle(3.0,625.0,595.0,100.0)
                }
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