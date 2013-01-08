(defproject boardintelligence/hetzner-crate "0.1.0-SNAPSHOT"
  :description "Pallet crate for working with dedicated machines at Hetzner data centre"
  :url "https://github.com/boardintelligence/hetzner-crate"
  :license {:name "MIT"
            :url "http://boardintelligence.mit-license.org"}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.cloudhoist/pallet "0.8.0-alpha.7" :exclusions [useful]]
                 [ch.qos.logback/logback-classic "1.0.7"]
                 [clj-time "0.4.4"]]

  :dev-dependencies [[org.cloudhoist/pallet "0.8.0-alpha.7" :type "test-jar"]
                     [org.cloudhoist/pallet-lein "0.5.2"]]

  :profiles {:dev
             {:dependencies [[org.cloudhoist/pallet "0.8.0-alpha.7" :classifier "tests"]]
              :plugins [[org.cloudhoist/pallet-lein "0.5.2"]]}}

  :local-repo-classpath true

  :repositories
  {"sonatype-snapshots" "https://oss.sonatype.org/content/repositories/snapshots"
   "sonatype" "https://oss.sonatype.org/content/repositories/releases/"})
