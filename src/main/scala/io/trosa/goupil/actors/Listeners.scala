package io.trosa.goupil.actors

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
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events._
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange
import io.trosa.goupil.listeners._
import io.trosa.goupil.models._

class Listeners extends Actor with ActorLogging {

    /* Import system context */
    implicit val system: ActorSystem = ActorSystem()

    /* Define internal actors */
    private val messagePosted: ActorRef = system.actorOf(Props[MessagePostedActor])
    private val reactionAdded: ActorRef = system.actorOf(Props[ReactionAddedActor])
    private val userJoin: ActorRef = system.actorOf(Props[ReactionAddedActor])
    private val messageUpdated: ActorRef = system.actorOf(Props[MessageUpdatedActor])
    private val userLeft: ActorRef = system.actorOf(Props[UserLeftActor])
    private val userChange: ActorRef = system.actorOf(Props[UserChangeActor])
    private val reactionDel: ActorRef = system.actorOf(Props[ReactionRemovedActor])

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        log.info("Starting Slack Listener")
    }

    override def postStop(): Unit = {
        log.info("Shutting down connection")
    }

    override def receive: PartialFunction[Any, Unit] = {
        case x: SlackSession => applyListeners(x)
        case _ => log.warning("Invalid Slack session received")
    }

    private def applyListeners(slackSession: SlackSession): Unit = {

        /*
        * Message listener
        * */
        slackSession addMessagePostedListener(
            (event: SlackMessagePosted, session: SlackSession) =>
                messagePosted ! MessagePostedCtx(event, session))

        /*
        *  Join listener
        * */
        slackSession addChannelJoinedListener(
            (event: SlackChannelJoined, session: SlackSession) =>
                userJoin ! UserJoinCtx(event, session))

        /*
        * Message update listener
        * */
        slackSession addMessageUpdatedListener(
            (event: SlackMessageUpdated, session: SlackSession) =>
                messageUpdated ! MessageUpdatedCtx(event, session))

        /*
        * Left listener
        * */
        slackSession addChannelLeftListener(
            (event: SlackChannelLeft, session: SlackSession) =>
                userLeft ! ChannelLeftCtx(event, session))

        /*
        * User metadata changes
        * */
        slackSession addSlackUserChangeListener(
            (event: SlackUserChange, session: SlackSession) =>
                userChange ! UserChangeCtx(event, session))

        /*
        * Reaction addition listener
        * */
        slackSession addReactionAddedListener(
            (event: ReactionAdded, session: SlackSession) =>
                reactionAdded ! ReactionPostedCtx(event, session))

        /*
        * Reaction Remove Listener
        * */
        slackSession addReactionRemovedListener(
            (event: ReactionRemoved, session: SlackSession) =>
                reactionDel ! ReactionRemovedCtx(event, session))
    }
}
