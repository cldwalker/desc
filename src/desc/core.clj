(ns desc.core
  (:require table.core
            [clojure.java.io :as io]))

(declare db-file)

(defn desc
  ([query] (println "SEARCH"))
  ([item desc] (println "DESC")))

(defn- db-file []
  (io/file (System/getProperty "user.home") ".desc.clj"))

(def ^:private records
  (delay
    (if (.exists (db-file))
      (->>
        (slurp (db-file))
        read-string
        vec)
      [])))
