(ns ducula.workers
  (:require [clojure.tools.logging :as log]))

(defn session-listener []
  "Main runner thread"
  (log/info "Starting listener channel."))
