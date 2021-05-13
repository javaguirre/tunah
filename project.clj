(defproject tunah "0.1.0-SNAPSHOT"
  :description "Guitar tuner"
  :url "http://github.com/javaguirre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [incanter "1.9.0"]
                 [seesaw "1.4.5"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cfft "0.1.0"]
                 [net.mikera/vectorz-clj "0.33.0"]]
  :main ^:skip-aot tunah.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
