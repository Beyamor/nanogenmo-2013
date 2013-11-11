(defproject genmo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.0-alpha2"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [jayq "2.5.0"]]
  :cljsbuild {
              :builds {
                       :main
                       {:source-paths ["src"]
                        :compiler {:output-to "app/main.js"}}}})
