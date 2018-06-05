(defproject ducula "0.1.0"
  :description "Simple Slack Workspace ↔ IRC bridge."
  :url "https://github.com/iomonad/ducula"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["jitpack" "https://jitpack.io"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/tools.logging "0.4.1"]
                 [com.ullink.slack/simpleslackapi "1.2.0"]]
  :main ^:skip-aot ducula.kernel
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
