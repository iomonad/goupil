(ns ducula.kernel
  "Main kernel entrypoint"
  (:gen-class)
  (:require [clojure.tools.logging :as log]))

(defn -main []
  "Program entry point"
  (log/info "Booting snat kernel"))
