package GenerativePosters

import FilmGrain
import archives.LoadedArticle
import archives.localArchive
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.color.rgba
import org.openrndr.draw.*
import org.openrndr.events.Event
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.blur.BoxBlur
import org.openrndr.extra.fx.color.ChromaticAberration
import org.openrndr.extra.fx.color.ColorCorrection
import org.openrndr.extra.fx.color.LumaMap
import org.openrndr.extra.fx.edges.LumaSobel
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer
import tools.dynamicText2
//import org.openrndr.extra.jumpfill.fx.InnerGlow
//import org.openrndr.extra.jumpfill.fx.OuterGlow
import org.openrndr.math.mod
import tools.dynamicText
import java.lang.Math.*

fun main() = application {
    configure {
        width = 600
        height = 800
    }

    program {
        extend(Screenshots()) {
        }
        val effectSpeed = 20.0
        var posterTime = 0.0
        var numArticle = 1

        //val titleArchive = localArchive("archives/example-text").iterator()
        val archive = localArchive("archives/example-poetry").iterator()
        var article = archive.next()
        //var titleArticle = titleArchive.next()
        val gui = GUI()

        val onNextArticle = Event<LoadedArticle>()
        val settings = @Description("Settings") object {
            @ActionParameter("Next article")
            fun nextArticle() {
                article = archive.next()
                onNextArticle.trigger(article)
                numArticle += 1
            }
        }

        val composite = compose {
            var background = ColorRGBa(56.0, 56.0, 56.0, 1.0)
            onNextArticle.listen {
                posterTime = seconds
                background = rgb(Math.random(), Math.random(), Math.random())
            }
            layer {
                draw {
                    drawer.clear(rgb("#0F0F0F"))
                    drawer.fill = ColorRGBa.BLACK
                    //drawer.rectangle(width/15.0, width/15.0 , width/15.0*13.0, height-((width/15.0)*2.0) )
                }
            }
            // -- text layer
            layer {
                layer {
                    val font = loadFont("data/fonts/HelveticaNow.otf", 24.0, contentScale = 1.5)
                    val highlight = loadFont("data/fonts/HelveticaNow.otf", 24.0, contentScale = 1.5)
                    val specialWords =
                        setOf("abusing", "hitting", "hit", "hurd", "physically", "punch", "face", "physical", "crying", "aggresive","verbally","fights","injuries","abuses","angry","bad","chokes","spits","slaps","punches",
                        "baby", "marriage", "husband", "childbirth", "yelled", "pain", "hair", "help", "leaving", "love", "broken", "kiss", "push", "kick", "sorry", "blamed")
                    draw {
                        drawer.fontMap = font
                        drawer.fill = rgb("#F4F4F4")
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
                                        drawer.fill = ColorRGBa(0.8,0.003,0.0,1.0)
                                        coordinates.add(Vector2(cursor.x, cursor.y))
                                        it.toUpperCase()
                                    } else if (it in specialWords) {
                                        it
                                    } else {
                                        drawer.fill = ColorRGBa.WHITE.opacify(1.0 - (seconds-posterTime)/(0.5 * effectSpeed))
                                        it
                                    }
                                }
                            }
                            drawer.fill = null
                            drawer.strokeWeight = 1.3
                            drawer.stroke = ColorRGBa.WHITE.opacify(0.0 + (seconds-posterTime)/(0.5 * effectSpeed))
                            val lineSegments = mutableListOf<Vector2>()
                            for (coordinate1 in coordinates) {
                                //for(coordinate2 in coordinates) {
                                lineSegments.add(coordinate1)
                                //lineSegments.add(coordinate2)
                                //}
                            }
                            drawer.lineSegments(lineSegments)

                        }
                    }
                }
            }

            //eyes layer
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
                        edgeColor = ColorRGBa(0.8,0.003,0.0,1.0)
                        backgroundOpacity -= ((seconds - posterTime) / (30 * effectSpeed));
                        edgeOpacity += (seconds - posterTime) / (5 * effectSpeed);
                    }.addTo(gui)
                }
            }.addTo(gui, "Eyes Layer")

            //title fade layer
            layer {
                layer {
                    val highlight = loadFont("data/fonts/HelveticaNow.otf", 55.0)
                    val fontLarge = loadFont("data/fonts/HelveticaNow.otf", 65.0)
                    draw {
                        //drawer.fontMap = fontSmall
                        writer {
                            for (i in 0 until 100) {
                                drawer.drawStyle.colorMatrix = tint(ColorRGBa(0.246, 0.246, 0.247,1.0).shade(i/100.0))
                                box = Rectangle(
                                    width / 15.0,
                                    ((height / 3.0) * 2 + 20) + i * 4.0,
                                    width - (width / 15.0 * 2.0),
                                    (height / 3.0)
                                )
                                drawer.fontMap = highlight
                                newLine()
                                drawer.fill = ColorRGBa(0.8,0.0,0.0,1.0)
                                dynamicText(article.texts[1])
                                {

                                }
                                drawer.fill = rgb("#0F0F0F")
                                drawer.rectangle(width / 15.0,
                                    height - (width / 15.0)-(width / 15.0),
                                    width * 1.0,
                                    (width / 15.0)*2.0)
                                //drawer.imageFit(image, i * 4.0, i * 4.0, width = mouse.position.x, height = mouse.position.y)
                            }
                        }
                    }
                }
                post(BoxBlur()){
                    this.gain = 0.686
                    this.window = 2
                }.addTo(gui)
                /*post(OuterGlow()) {
                    this.color = ColorRGBa.RED
                    this.imageOpacity = 0.768
                    this.opacity = 0.281
                    this.shape = 0.751
                    this.width = 1.615
                }.addTo(gui)*/

            }.addTo(gui, "Title Fade")

            //title layer
            layer {
                layer {
                    val highlight = loadFont("data/fonts/HelveticaNow.otf", 55.0)
                    val fontLarge = loadFont("data/fonts/HelveticaNow.otf", 65.0)
                    draw {
                        //drawer.fontMap = fontSmall
                        writer {
                            box = Rectangle(
                                width / 15.0,
                                (height / 3.0 + 10.0) * 2,
                                width - (width / 15.0 * 2.0),
                                (height / 3.0)
                            )
                            // text(titleArticle.texts[0])
                            drawer.fontMap = highlight
                            newLine()
                            //text(titleArticle.texts[0])
                            drawer.fill = ColorRGBa(0.8,0.0,0.0,1.0)
                            dynamicText(article.texts[1])
                            {

                            }

                        }
                    }
                    //post(OuterGlow()).addTo(gui)
                }.addTo(gui)
            }.addTo(gui, "Title")


            layer {
                layer {
                    val smallText = loadFont("data/fonts/HelveticaNow.otf", 20.0)
                    val numberText = loadFont("data/fonts/HelveticaNow.otf", 30.0)
                    draw {
                        //drawer.fontMap = fontSmall
                        drawer.stroke = rgb("#F4F4F4")
                        drawer.strokeWeight = 2.0
                        drawer.lineCap = LineCap.BUTT
                        drawer.lineSegment((width / 15.0)+(width / 15.0)*2.0, height - (width / 15.0), (width / 15.0)+(width / 15.0)*4.0, height - (width / 15.0))
                        writer {
                            box = Rectangle(
                                width / 15.0,
                                height - (width / 15.0)-(width / 15.0),
                                (width / 15.0)*2.0,
                                (width / 15.0)*2.0
                            )
                            drawer.fontMap = smallText
                            newLine()
                            drawer.fill = rgb("#F4F4F4")
                            dynamicText("Case")
                            {
                            }
                            drawer.fontMap = numberText
                            newLine()
                            drawer.fill = rgb("#F4F4F4")
                            dynamicText("0"+numArticle.toString())
                            {
                            }
                        }
                    }
                }
            }.addTo(gui, "Case NO.")

            layer {
                val helplines =
                    listOf("Austria: 0800 222 555", "Bulgaria: +359 2 981 76 86", "Croatia: +385800 55 44", "Cyprus: +3571440", "Turkey: +90 212 656 9696", "Denmark: +45 70 20 30 82", "Georgia: +995 309 903", "Finland: +358 800 02400", "France: +33800 05 95 95", "Germany: +498000 116 016","Greece: +30197","Hungary: +36 4 06 30 006","Ireland: +3531800 341 900","Albania: +355 422 3340","Macedonia: +389 15 315","Slovenia: +38680 11 55", "Netherlands: +31800 2000", "Romania: +40262 25 07 70", "Slovakia: +421800 212 212", "Sweden: +4620 1010")
                val font = loadFont("data/fonts/HelveticaNow.otf", 15.0)
                //drawer.fill = null
                draw {
                    var index = 0;
                    drawer.fill = ColorRGBa.WHITE
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
                }
            }.addTo(gui, "Helplines")

            //Overlay
            layer {
                draw {
                    var image = loadImage("data/images/overlay2.png")
                    drawer.imageFit(image, 0.0, 0.0, width * 1.0, height * 1.0)
                }
                post(ColorCorrection()) {
                    this.opacity = 0.8
                    this.brightness = 0.8

                }
            }.addTo(gui, "Overlay")
        }


        onNextArticle.trigger(article)

        gui.add(settings)
        extend(gui)
        extend {
            composite.draw(drawer)
        }
    }
}