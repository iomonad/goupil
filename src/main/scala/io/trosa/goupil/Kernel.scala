package io.trosa.goupil

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

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import io.trosa.goupil.actors.Listeners

object Kernel extends App {

  val token: String = sys.env.get("SLACK_TOKEN") match {
    case Some(x) => x
    case None => {
      println("Fatal: SLACK_TOKEN env variable is needed ! Exiting ...")
      sys.exit(1)
    }
  }
  val session   = SlackSessionFactory createWebSocketSlackSession token
  val system    = ActorSystem("goupil-system")
  val listeners = system.actorOf(Props[Listeners], "listeners-actor")

  session.connect
  listeners ! session
  system.terminate
}
