package io.trosa.goupil.listeners

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

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import io.trosa.goupil.gateway.Irc
import io.trosa.goupil.models.{IrcMessage, MessagePostedCtx}

class MessagePostedActor extends Actor with ActorLogging {

    implicit val system: ActorSystem = ActorSystem()

    private val IrcBroadcastActor: ActorRef = system.actorOf(Props[Irc])

    override def receive: Receive = {
        case x: MessagePostedCtx => applyPosted(x)
        case _ => log.warning("Invalid actor request");
    }

    private def applyPosted(ctx: MessagePostedCtx): Unit = {
        log.info("Got a message from: {} on {} - {}",
            ctx.message.getSender.getUserName,
            ctx.message.getChannel.getName,
            ctx.message.getMessageContent)

        ctx.message.getChannel.getName match {
            case "annonces" =>
                IrcBroadcastActor ! IrcMessage(ctx.message.getSender.getUserName, ctx.message.getMessageContent)
            case "general" =>
                IrcBroadcastActor ! IrcMessage(ctx.message.getSender.getUserName, ctx.message.getMessageContent)
            case _ => log.info("Invalid message context for broadcasting message")
        }
    }
}
