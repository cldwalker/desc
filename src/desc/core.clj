(ns desc.core
  (:require table.core
            clojure.pprint
            [clojure.java.io :as io]))

(declare db-file fetch-records)

(defn desc
  ([query] (println "SEARCH"))
  ([item desc]
   (let [recs (fetch-records)
         new-recs (conj recs {:name item :desc desc})
         body (with-out-str (clojure.pprint/pprint new-recs))]
     (spit (str (db-file)) body))
   (println "Record added.")))

(defn- db-file []
  (io/file (System/getProperty "user.home") ".desc.clj"))

(defn- fetch-records []
  (if (.exists (db-file))
    (-> (slurp (db-file)) read-string vec)
    []))
