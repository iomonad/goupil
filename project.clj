(defproject snat "0.1.0"
  :description "Simple Slack Workspace ↔ IRC bridge."
  :url "https://github.com/iomonad/snat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot snat.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
