(defproject interview-accelerator "0.1.0-SNAPSHOT"
  :description "Managing interviews"
  :url "http://truecomputergenius.com"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.9.1"]
                 [ring-cors "0.1.13"]
                 [metosin/compojure-api "1.1.11"]
                 [ring/ring-defaults "0.3.2"]]
  :ring {:handler interview-accelerator.handler/app
         :auto-reload? true
         :auto-refresh? true}
  :plugins [[lein-ring "0.12.5"]
            [lein-cljfmt "0.6.4"]]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}})
