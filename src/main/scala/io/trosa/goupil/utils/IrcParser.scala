package io.trosa.goupil.utils

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

import io.trosa.goupil.models._

/*
* Custom parsing exception
* */

case class IrcParserException(cause: String) extends Exception {
    /* Redefine message internal */
    override def getMessage: String = {
        "Error while parsing raw message: %s".format(cause)
    }
}

/*
* Parsing interface
* */

abstract class Parser[-A] {
    def parse(raw: String): Either[Exception, IrcStream]
}


/* WARNING, impure interface for
* class building, in the future we will
* use cat's validator instead.
* TODO: Replace with cats validators
* */
trait IIrcInternal {
    var username: Username = new String
    var hostname: Hostname = new String
    var token: Token = new String
    var target: Target = new String
    var message: Message = new String
}

/*
* Porcelain object to retrieve easily informations in irc stream
* @return Either[IrcParserException, IrcStream]
*    ~> Compilation failure => Exception
*    ~> IrcStream => correct object
* */

object IrcParser extends Parser[String]
    with IIrcInternal {

    override def parse(raw: String): Either[IrcParserException, IrcStream] = {
        lazy val split = raw split " \r\n"

        if (split(0).isEmpty) {
            Left(IrcParserException("Invalid token"))
        } else {
            Right(IrcStream(username, hostname, token, target, message))
        }
    }
}
