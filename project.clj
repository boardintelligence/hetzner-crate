(defproject boardintelligence/hetzner-crate "0.1.0-SNAPSHOT"
  :description "Pallet crate for working with dedicated machines at Hetzner data centre"
  :url "https://github.com/boardintelligence/hetzner-crate"
  :license {:name "MIT"
            :url "http://boardintelligence.mit-license.org"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.palletops/pallet "0.8.0-RC.1"]
                 [ch.qos.logback/logback-classic "1.0.7"]
                 [boardintelligence/pallet-nodelist-helpers "0.1.0-SNAPSHOT"]]

  :profiles {:dev {:plugins [[com.palletops/pallet-lein "0.8.0-alpha.1"]]}})
