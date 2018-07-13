FROM java:openjdk-8-alpine

ENV SCALA_VERSION scala-2.12
ENV TARGET goupil-latest.jar
ENV ARTIFACT target/${SCALA_VERSION}/${TARGET}

COPY ${ARTIFACT} /

WORKDIR /

ENTRYPOINT java -jar ${TARGET}
