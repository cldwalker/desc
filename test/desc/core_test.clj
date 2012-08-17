(ns desc.core-test
  (:use midje.sweet
        desc.core)
  (:require [clojure.java.io :as io]))

(defn seed-search-data []
  (desc "binding" "only redefines dynamic vars")
  (desc "with-out-str" "capture *out*"))

(defn desc-str [& args]
  (with-out-str (apply desc args)))

; before-all: create temp test-dir
(def test-dir (-> (io/resource *file*) .getPath io/file .getParentFile (io/file "tmp")))
(.mkdir test-dir)
(System/setProperty "user.home" (str test-dir))

(background (before :facts (reset! records nil)))

(fact "nonexistent .desc.clj yields vector"
  (@#'desc.core/check-records)
    @records => [])

(fact "desc adds a record with bare name"
  (desc-str "comp" "handy!") => "Added record.\n"
  @records => [{:name "clojure.core/comp" :desc "handy!"}])

; TODO: fix background
;(fact "desc adds a record with full name"
;  (desc-str "clojure.set/join" "like SQL join for sets") => "Added record.\n"
;  @records => [{:name "clojure.set/join" :desc "like SQL join for sets"}])

(fact "desc updates record by bare name"
  (desc "comp" "handy!")
  (desc-str "comp" "just handy") => "Updated record.\n"
  @records => [{:name "clojure.core/comp" :desc "just handy"}])

(fact "desc updates record by full name"
  (desc "comp" "handy!")
  (desc-str "clojure.core/comp" "just handy") => "Updated record.\n"
  @records => [{:name "clojure.core/comp" :desc "just handy"}])

(fact "desc finds no records"
  (desc-str "NONE") => "No records found.\n")

(fact "desc finds one record"
  (seed-search-data)
  (desc-str "bind") => "Name: clojure.core/binding\nDesc: only redefines dynamic vars\n"
  (desc-str #"bind") => "Name: clojure.core/binding\nDesc: only redefines dynamic vars\n")

(fact "desc finds two records"
  (seed-search-data)
  (desc-str "i") =>

"+---------------------------+-----------------------------+
| name                      | desc                        |
+---------------------------+-----------------------------+
| clojure.core/binding      | only redefines dynamic vars |
| clojure.core/with-out-str | capture *out*               |
+---------------------------+-----------------------------+\n")

; after-all: cleanup test-dir
(.delete (io/file test-dir ".desc.clj"))
(.delete test-dir)
