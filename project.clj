(defproject gitlabtrends "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.7.0-RC2"]
                 [enlive "1.1.6"]
                 [selmer "1.12.3"]
                 [http-kit "2.1.18"]
                 [yada "1.2.11"]
                 [hswick/jutsu "0.1.2"]
                 [hiccup "1.0.5"]
                 [clj-time "0.14.3"]]
  :plugins [[lein-cljfmt "0.5.7"]]
  :main gitlabtrends.core)
