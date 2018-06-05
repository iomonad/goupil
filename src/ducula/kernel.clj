(ns ducula.kernel
  "Main kernel entrypoint"
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [ducula.workers :as w]))

(defn -main [& token]
  "Program entry point"
  (log/info "Booting ducula kernel"))
