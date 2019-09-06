(defproject interview-accelerator "0.1.0-SNAPSHOT"
  :description "Managing Interviews"
  :url "http://truecomputergenius.com"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.6.1"]
                 [buddy "2.0.0"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-cljfmt "0.6.4"]]
  :ring {:handler interview-accelerator.handler/app
         :auto-reload? true
         :auto-refresh? true}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
