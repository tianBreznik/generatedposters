package GenerativePosters


import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.blur.BoxBlur
import org.openrndr.extra.fx.color.LumaMap
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer
import tools.dynamicText
import tools.dynamicText2
import java.lang.Math.cos
import kotlin.text.Typography.ellipsis


fun main() = application {

    configure {
        width = 500
        height = 750
        windowResizable = true
    }

    program {
        extend(Screenshots()) {
            scale = 4.0
        }
        val gui = GUI()
        val c = compose {
            layer {
                layer {
                    val font = loadFont("data/fonts/HelveticaNow.otf", 15.0)
                    val highlight = loadFont("data/fonts/HelveticaNow.otf", 24.0)

                    val specialWords = setOf("abusing", "hitting", "hit", "hurd", "physically", "punch", "face", "physical")
                    draw {
                        drawer.fontMap = font
                        drawer.fill = ColorRGBa.WHITE
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
                                    "My husband and I got married very young I was 18 he was 19. He has been the only guy I have ever been with. He started physically abusing me shortly after marriage and it went on for 6 years so between 2006-2012. It was really bad he would sit on me and punch me in the face or stomach sometimes until I bled, drag me around the house by my hair.. etc. Once he said there was no use calling the cops because by the time they showed up every bone in my body would be broken. I wont go on as it is hard to think about but just know that the abuse was very extreme.Many ask me how the abuse stopped. Well one day in 2012 after an altercation I was finally starting to realize I was worth more then this abusive marriage. I told him it needed to stop. He gave me an ultimatum and said “then next time I’m angry you need to back off and give me space, or I’m going to leave and never come back”. Oh how I wish I chose the latter. Instead I just told myself its always been my fault and if I want to keep this man who I thought loved me I need to change, so I did.I’m the type that likes to solve a problem right away and talk it out, he is the type that needs space and to cool off. The problem is I just stopped caring. We stopped fighting because I stopped caring and let him do whatever and stopped expressing myself. To this day I can’t talk to him, I can’t explain my feelings. He is always trying to get me to talk to him and share things with him but I just can’t, I don’t care and I’m pretty sure I don’t love him anymore. I care about his well being and I know he has changed and learned a lot but I just cant get those feelings back. I don’t love him as a wife should.Times I have tried to talk to him he gets really manipulative and makes me feel like I’m the bad person and has even threatened to kill himself if I leave him. He will cry and make a huge scene which makes me feel bad and guilty. Guilt is what keeps me in marriage for sure.I day dream what it would be like to be with another man who treats me better. Even though my husband isn’t physically abusive anymore, he doesn’t do much for me. I do all the errands and chores and take care of our lives 100%. He never once comes home and says its his turn to make dinner, or do laundry etc. I don’t know what its like to have a man that does things for me, I do it all. He won’t admit it but he tends to have a sexist idea towards who should take care of what. There is so much I could continue to go on, and I’m sorry for this long rant but what I basically want to get at is that I’m pretty sure I need to leave him. I don’t think I could ever get those “in love” feelings back for him, and to be honest I’m not even sure why I fell out of love with him, it could be totally unrelated to our past, but it sure doesn’t help. I often think what if I’m just a bored shallow person who wants to be with other guys? But I know in my heart I’m a good person and that’s not what it is. My husband and I got married very young I was 18 he was 19. He has been the only guy I have ever been with. He started physically abusing me shortly after marriage and it went on for 6 years so between 2006-2012. It was really bad he would sit on me and punch me in the face or stomach sometimes until I bled, drag me around the house by my hair.. etc. Once he said there was no use calling the cops because by the time they showed up every bone in my body would be broken. I wont go on as it is hard to think about but just know that the abuse was very extreme.Many ask me how the abuse stopped. Well one day in 2012 after an altercation I was finally starting to realize I was worth more then this abusive marriage. I told him it needed to stop. He gave me an ultimatum and said “then next time I’m angry you need to back off and give me space, or I’m going to leave and never come back”. Oh how I wish I chose the latter. Instead I just told myself its always been my fault and if I want to keep this man who I thought loved me I need to change, so I did.I’m the type that likes to solve a problem right away and talk it out, he is the type that needs space and to cool off. The problem is I just stopped caring. We stopped fighting because I stopped caring and let him do whatever and stopped expressing myself. To this day I can’t talk to him, I can’t explain my feelings. He is always trying to get me to talk to him and share things with him but I just can’t, I don’t care and I’m pretty sure I don’t love him anymore. I care about his well being and I know he has changed and learned a lot but I just cant get those feelings back. I don’t love him as a wife should.Times I have tried to talk to him he gets really manipulative and makes me feel like I’m the bad person and has even threatened to kill himself if I leave him. He will cry and make a huge scene which makes me feel bad and guilty. Guilt is what keeps me in marriage for sure.I day dream what it would be like to be with another man who treats me better. Even though my husband isn’t physically abusive anymore, he doesn’t do much for me. I do all the errands and chores and take care of our lives 100%. He never once comes home and says its his turn to make dinner, or do laundry etc. I don’t know what its like to have a man that does things for me, I do it all. He won’t admit it but he tends to have a sexist idea towards who should take care of what. There is so much I could continue to go on, and I’m sorry for this long rant but what I basically want to get at is that I’m pretty sure I need to leave him. I don’t think I could ever get those “in love” feelings back for him, and to be honest I’m not even sure why I fell out of love with him, it could be totally unrelated to our past, but it sure doesn’t help. I often think what if I’m just a bored shallow person who wants to be with other guys? But I know in my heart I’m a good person and that’s not what it is."){
                                    if (it in specialWords) {
                                        drawer.fontMap = highlight
                                        drawer.fill = ColorRGBa.RED
                                         it.toUpperCase()
                                    } else if (it in specialWords) {
                                    it
                                    } else{
                                        it
                                    }
                            }
                        }
                    }
                }
                post(ApproximateGaussianBlur()){
                    this.sigma = cos(seconds * Math.PI) * 10.0 + 10.1
                }.addTo(gui)
            }.addTo(gui, "layer 1")
            layer {

                val image = loadImage("data/images/eyes.png")
                draw {
                    drawer.image(image, (width / 15.0)*2, width / 15.0+((width - (width / 15.0 * 4.0))/3), width - ((width / 15.0)*4), (width / 15.0)+((width - (width / 15.0 * 2.0))/3))
                }
                post(LumaMap()){
                    this.backgroundOpacity = 1.0
                    this.background = ColorRGBa.RED
                    this.foreground = ColorRGBa.BLUE
                }.addTo(gui)
            }.addTo(gui, "layer 2")
        }
        extend(gui)
        extend {
            c.draw(drawer)
        }
    }
}