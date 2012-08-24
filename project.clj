(defproject desc "0.1.0"
  :description "Describe what you want about clojure."
  :url "http://github.com/cldwalker/desc"
  :license {:name "The MIT LICENSE"
            :url "https://en.wikipedia.org/wiki/MIT_License"}
  :dependencies [[org.clojure/clojure "1.4.0"] [table "0.3.2"]]
  :profiles {:dev {:dependencies [[midje "1.4.0"]]
                    :plugins  [[lein-midje "2.0.0-SNAPSHOT"]]}})
