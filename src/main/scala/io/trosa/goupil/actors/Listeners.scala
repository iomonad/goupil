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

import akka.actor.{Actor, ActorLogging}
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events._
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange
import com.ullink.slack.simpleslackapi.listeners._

class Listeners extends Actor with ActorLogging {

    override def receive = {
        case x: SlackSession => applyListeners(x)
    }

    private def applyListeners(slackSession: SlackSession): Unit = {

        /*
        * Message listener
        * */
        slackSession addMessagePostedListener(
            (event: SlackMessagePosted, session: SlackSession) => {
                log.info("Got a message from: {} - {}", event.getSender.getUserName,
                    event.getMessageContent)
        })

        /*
        *  Join listener
        * */
        slackSession addChannelJoinedListener(
            (event: SlackChannelJoined, session: SlackSession) => {
                log.info("Joined {}", event.getSlackChannel.getName)
        })

        /*
        * Message update listener
        * */
        slackSession addMessageUpdatedListener(
            (event: SlackMessageUpdated, session: SlackSession) => {
                log.info("Message: {} updated to {}", event.getMessageTimestamp, event.getNewMessage)
        })

        /*
        * Left listener
        * */
        slackSession addChannelLeftListener(
            (event: SlackChannelLeft, session: SlackSession) => {
            log.debug("{}", event.toString)
        })

        /*
        * User metadata changes
        * */
        slackSession addSlackUserChangeListener(
            (event: SlackUserChange, session: SlackSession) => {
                log.debug("User Change: {}", event.getUser)
            })

        /*
        * Reaction addition listener
        * */
        slackSession addReactionAddedListener(
            (event: ReactionAdded, session: SlackSession) => {

        })
    }
}
