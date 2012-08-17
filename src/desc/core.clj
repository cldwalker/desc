(ns desc.core
  (:require table.core
            clojure.pprint
            clojure.string
            [clojure.java.io :as io]))

(declare db-file fetch-records check-records save-records update-or-add output search-records resolve-name)

(def records (atom nil))

(defn desc
  ([query] (check-records) (output (search-records query)))
  ([item desc]
   (check-records)
   (swap! records update-or-add {:name item :desc desc})
   (save-records)))

(defn- search-records [query]
  (let [matches? (if (instance? java.util.regex.Pattern query)
                   #(re-find query (str %))
                   #(.contains (str %) (str query)))
        fields [:name]]
  (filter #(some matches? ((apply juxt fields) %)) @records)))

(defn- output [recs]
  (case (count recs)
    1 (println (format "Name: %s\nDesc: %s" (:name (first recs)) (:desc (first recs))))
    0 (println "No records found.")
    (table.core/table recs)))

(defn- update-or-add [recs new-rec]
  (let [resolved-name (resolve-name (:name new-rec))
        new-rec (assoc new-rec :name resolved-name)]
    (if (some #(= (:name %) resolved-name) recs)
      (do
        (println "Updated record.")
        (assoc recs (.indexOf (map :name recs) resolved-name) new-rec))
      (do
        (println "Added record.")
        (conj recs new-rec)))))

(defn resolve-name [name]
 (if-let [sym (resolve (symbol name))]
   (clojure.string/replace-first (str sym) #"#'" "")
   name))

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
