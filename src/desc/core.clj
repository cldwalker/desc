(ns desc.core
  (:require table.core
            clojure.pprint
            [clojure.java.io :as io]))

(declare db-file fetch-records check-records save-records update-or-add)

(def records (atom nil))

(defn desc
  ([query] (println "SEARCH"))
  ([item desc]
   (check-records)
   (swap! records update-or-add {:name item :desc desc})
   (save-records)))

(defn- update-or-add [recs new-rec]
  (if (some #(= (:name %) (:name new-rec)) recs)
    (do
      (println "Updated record.")
      (assoc recs (.indexOf (map :name recs) (:name new-rec)) new-rec))
    (do
      (println "Added record.")
      (conj recs new-rec))))

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
