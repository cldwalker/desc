(ns desc.core
  (:require table.core
            clojure.pprint
            [clojure.java.io :as io]))

(declare db-file fetch-records check-records save-records)

(def records (atom nil))

(defn desc
  ([query] (println "SEARCH"))
  ([item desc]
   (check-records)
   (swap! records conj {:name item :desc desc})
   (save-records)
   (println "Record added.")))

(defn- save-records []
  (spit
    (str @db-file)
    (with-out-str (clojure.pprint/pprint @records))))

(defn- check-records []
  (if (nil? @records) (swap! records fetch-records)))

(def ^:private db-file
  (delay
    (io/file (System/getProperty "user.home") ".desc.clj")))

(defn- fetch-records [& args]
  (if (.exists @db-file)
    (-> (slurp @db-file) read-string vec)
    []))
