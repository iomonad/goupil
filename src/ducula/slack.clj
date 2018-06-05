(ns ducula.slack
  "Wrapper of the slack API"
  (:import [com.ullink.slack.simpleslackapi
            SlackSession]
           [com.ullink.slack.simpleslackapi.impl
            SlackSessionFactory]))

(defn mk-connection [^String token]
  "Create an new connection instance"
  (let [co (.createWebSocketSlackSession
            SlackSessionFactory token)]
    (.connect co)))
