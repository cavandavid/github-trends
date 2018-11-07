(ns gitlabtrends.core
  (:require [ring.adapter.jetty :as jetty]
            [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [yada.yada :as yada]
            [selmer.parser :as sp]
            [clojure.java.io :as io]
            [jutsu.core :as j]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defn trend-info
  "Extracts information of a single language in a descriptive manner"
  [trend]
  (let [url         (str "https://github.com"
                         (get-in trend [:attrs :href]))
        content-map (nth (:content trend) 1)
        count       (-> trend
                        (:content)
                        (nth 1)
                        (:content)
                        (first)
                        (clojure.string/replace #"," "")
                        (Integer.))
        language    (-> trend
                        (:content)
                        (nth 2)
                        (clojure.string/trim))]
    {:name  language
     :count count
     :url   url}))

(defn generate-trends
  []
  (let [now-time          (t/now)
        one-month-before  (t/minus now-time (t/months 1))
        stringified-time  (str (tc/to-local-date one-month-before))
        url               (str "https://github.com/search?q=created%3A%3E" stringified-time "&type=Repositories")
        body              (html/html-snippet
                           (:body @(http/get url {:insecure? true})))
        trends            (html/select body [:a.filter-item])
        trend-map         (doall
                           (map trend-info trends))]
    trend-map))

(def get-trends
  (yada/resource
   {:methods
    {:get
     {:response
      (fn [ctx]
        (generate-trends))}}
    :produces [{:media-type #{"application/json"}
                :charset    "UTF-8"}]}))

(def git-trends
  (yada/resource
   {:methods
    {:get
     {:response
      (let [data         (generate-trends)
            total        (reduce + (map #(:count %) data))
            indexed-data (map #(assoc % :rank %2
                                      :percentage-share (int (* (/ (:count %1) total) 100)))
                              data (range 1 (+ 1 (count data))))]
        (sp/render-file "html/home.html" {:data indexed-data}))}}
    :produces [{:media-type #{"text/html"}}]}))

(def routes
  [""
   [["/get-trends" get-trends]
    ["/gitlab-trends" git-trends]]])

(defn start-server
  "Start the server"
  []
  (let [listener (yada/listener
                  routes
                  {:port 1099})]))

(defn -main
  "Kickstart the project"
  []
  (println "Starting up..")
  (start-server))

(-main)
