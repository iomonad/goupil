<img align="right" src="https://sameroom.io/blog/content/images/2015/06/Slack-IRC.png" height="200px" style="padding-left: 20px"/>

# Goupil

An Akka based gateway between your slack workspaces and IRC channels, with logging and bot features. 

## Getting Started

### Adding tokens and informations
Edit  `src/main/resources/application.conf`

Config sample:
```
akka {
  loglevel = "DEBUG"
  stdout-loglevel = "DEBUG"
  actor {
    default-dispatcher {
      throughput = 10
    }
  }
  remote {
    # The port clients should connect to. Default is 2552.
    netty.tcp.port = 4711
  }
}
irc {
  server = "irc.freenode.net"
  port = 6667
  nick = "smokedsalmon"
  user = "smokedsalmon"
  channel = "##42born2code"
  mentor = "trosa"
}
slack {
  token = "xoxp-3329266loeuoeucoeuc-ouoetuoe9883t2-foobar"
}
```

### Building with SBT
```bash
sbt test
sbt assembly
```
### Running artifact
```bash
java -jar target/$SCALA_VERSION/goupil-latest.jar
```

### Prerequisites
Java platform should be pre-installed on your computer:

```
bash
sudo emerge --ask dev-java/icedtea:7
```
## Deployment

```
bash
sbt assembly
docker build -t goupil:latest .

docker run --name goupil -d -e SLACK_TOKEN=$SLACK_TOKEN goupil:latest
```

## Built With

* [Akka](http://akka.io) - The JVM actor model framework
* [SimpleSlackApi](https://github.com/Ullink/simple-slack-api) - Simple Slack RTM client

## Authors

* **Clement Trosa** - *Initial work* - [iomonad](https://github.com/iomonad)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
