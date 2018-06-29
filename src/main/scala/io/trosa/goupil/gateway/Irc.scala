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

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill}
import akka.io.Tcp._
import akka.util.ByteString
import com.typesafe.config.{Config, ConfigFactory}
import io.trosa.goupil.models.IrcMessage

import scala.language.postfixOps

class Irc extends Actor with ActorLogging {

    import akka.io.{IO, Tcp}
    import context.system

    /* Internals */
    private val config: Config = ConfigFactory.load

    /* References */
    private val server: String = config getString "irc.server"
    private val port: Int = config getInt "irc.port"

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
        log.info("Restarting actor, shutting down established connections")
    }

    override def postStop(): Unit = {
        log.info("Stopping IRC actor. Closing all connections.")
        auth = false
        manager ! Close
    }


    private def SaslAuth(config: Config): Unit = {
        lazy val nick = config getString "irc.nick"
        lazy val user = config getString "irc.user"
        lazy val chan = config getString "irc.channel"

        assert(nick != null && user != null && chan != null)
        manager ! Write(ByteString("NICK %s".format(nick)))
        manager ! Write(ByteString("USER %s 8 x : %s".format(user, user)))
        manager ! Write(ByteString("JOIN %s".format(chan)))
        auth = true
    }

    override def receive: Receive = {
        case x: IrcMessage => broadcast(x)
        case Connected(remote, local) =>
            val connection: ActorRef = sender

            connection ! Register(self)
            if (!auth) SaslAuth(config)
            context become {
                case  Received(data: ByteString) => handler(data)
                case x: IrcMessage => broadcast(x)
            }
        case _ => log.warning("Invalid irc message request")
    }

    private def handler(data: ByteString): Unit = {
        val index: Array[String] = data.utf8String split ' '
        log.info("IRC @@ " + data.utf8String stripLineEnd)
        val x = index(1)
        x match {
            case ":Closing" => context.self ! PoisonPill
            case "NOTICE" => log.info("IRC NOTICE from {}: {}", hostname, x)
            case _ => log.info("IRC TOKEN(%s) @@ ".format(x) + data.utf8String stripLineEnd)
        }
    }

    /* Broadcast mailbox message to IRC channel */
    private def broadcast(message: IrcMessage): Unit = {
        log.info("Broacasting message to {} - {}: {}", server,
            message.username, message.message)
    }
}
