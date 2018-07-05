package io.trosa.goupil.gateway

/*
 * Copyright (c) 2017 Clement Trosa <me@trosa.io>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.net._

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.io.Tcp._
import akka.util.ByteString
import com.typesafe.config.{Config, ConfigFactory}
import io.trosa.goupil.models.IrcMessage
import javax.net.ssl.{SSLSocket, SSLSocketFactory}

import scala.language.postfixOps

class Irc extends Actor with ActorLogging {

    import akka.io.{IO, Tcp}
    import context.system

    /* Internals */
    private val config: Config = ConfigFactory.load

    /* References */
    private val server: String = config getString "irc.server"
    private val port: Int = config getInt "irc.port"
    private lazy val nick = config getString "irc.nick"
    private lazy val user = config getString "irc.user"
    private lazy val chan = config getString "irc.channel"

    /* Connection references */
    private val hostname = new InetSocketAddress(server, port)
    private val manager = IO(Tcp)
    private var auth: Boolean = false

    override def preStart(): Unit = {
        log.info("Starting IRC gateway")
        assert(server != null && port > 0)
        manager ! Connect(hostname)
    }

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        log.warning("Restarting actor, shutting down established connections")
    }

    override def postStop(): Unit = {
        log.warning("Stopping IRC actor. Closing all connections.")
        auth = false
        manager ! Close
    }


    private def SASLAuth(config: Config, sender: ActorRef): Unit = {
        assert(nick != null && user != null && chan != null)

        sender ! Write(ByteString("NICK %s\r\n".format(nick)))
        sender ! Write(ByteString("USER %s 8 x : %s\r\n".format(user, user)))
        sender ! Write(ByteString("JOIN %s\r\n".format(chan)))
        log.info("Joined {} channel", chan)
    }

    override def receive: Receive = {
        case x: IrcMessage => {
            val connection: ActorRef = sender

            connection ! Register(self)
            context become {
                case _ =>
                    broadcast(x, sender())
            }
        }
        case Connected(remote, local) =>

            val connection: ActorRef = sender

            connection ! Register(self)
            if (!auth) SASLAuth(config, sender())

            context become {
                case Received(data: ByteString) =>
                    if (data.startsWith("PING :")) {
                        log.info("@@ IRC @@ Responding PING request from {} to {}"
                            , local.getHostName
                            , remote.getHostName)
                        sender() ! Write(ByteString("PONG :active\r\n"))
                    } else handler(data, sender())
                case x: IrcMessage => broadcast(x, sender())
                case _ => log.error("Invalid internal stream request")
            }
        case _ => log.warning("Invalid irc message request")
    }

    private def handler(data: ByteString, sendex: ActorRef): Unit = {
        lazy val index: Array[String] = data.utf8String split ' '
        val x = index(1)

        log.info("@@@ IRC @@@ " + data.utf8String stripLineEnd)
        x match {
            case ":Closing" => context.self ! PoisonPill
            case "QUIT" => context.self ! PoisonPill
            case "443" => context.self ! Restart
            case "MODE" => auth = true
            case "PRIVMSG" => parsecommand(data, sendex)
            case _ => log.info("@@@ IRC TOKEN(%s) @@@ ".format(x) + data.utf8String stripLineEnd)
        }
    }

    /* Parse irc commands */
    private def parsecommand(data: ByteString, sendex: ActorRef): Unit = {
        lazy val str: String = data.utf8String
        lazy val input: String = str.dropRight(str.lastIndexOf(":"))

        if (input startsWith ":!") {
            lazy val index: Array[String] = input.split(" ")

            index(0) match {
                case _ => log.info("Invalid command {}", index(0))
            }
        }
    }

    /* Broadcast mailbox message to IRC channel */
    private def broadcast(message: IrcMessage, sendex: ActorRef): Unit = {
        log.info("Broacasting message to {} - {}: {}", server,
            message.username, message.message)

        sendex ! Write(ByteString("PRIVMSG %s: %s - %s\r\n".format(chan,
            message.username, message.username)))
    }
}