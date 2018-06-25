package io.trosa.goupil

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.{ReactionAdded, SlackMessagePosted}

package object models {

    case class MessagePostedCtx(message: SlackMessagePosted, session: SlackSession)
    case class ReactionPostedCtx(message: ReactionAdded, session: SlackSession)
}
