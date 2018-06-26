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

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events._
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange

package object models {

    private trait Ctx {
        def session: SlackSession
    }

    /*
    * Context model
    * */
    case class MessagePostedCtx(message: SlackMessagePosted, session: SlackSession)
    case class ReactionPostedCtx(message: ReactionAdded, session: SlackSession)
    case class UserJoinCtx(message: SlackChannelJoined, session: SlackSession)
    case class MessageUpdatedCtx(message: SlackMessageUpdated, session: SlackSession)
    case class ChannelLeftCtx(message: SlackChannelLeft, session: SlackSession)
    case class UserChangeCtx(message: SlackUserChange, session: SlackSession)
    case class ReactionRemovedCtx(message: ReactionRemoved, session: SlackSession)

    /*
    * Gateway models
    * */

    type Username = String
    type Message = String

    case class IrcMessage(username: Username, message: Message)
}
