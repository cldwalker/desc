(ns desc.core-test
  (:use midje.sweet
        desc.core)
  (:require [clojure.java.io :as io]))

(defn seed-search-data []
  (desc "binding" "only redefines dynamic vars")
  (desc "with-out-str" "capture *out*"))

; before-all: create temp test-dir
(def test-dir (-> (io/resource *file*) .getPath io/file .getParentFile (io/file "tmp")))
(.mkdir test-dir)
(System/setProperty "user.home" (str test-dir))

(background (before :facts (reset! records nil)))

(fact "nonexistent .desc.clj yields vector"
  (@#'desc.core/check-records)
    @records => [])

(fact "desc adds a record"
  (with-out-str (desc "comp" "handy!")) => "Added record.\n"
  (@#'desc.core/fetch-records) => [{:name "comp" :desc "handy!"}])

(fact "desc updates a record"
  (desc "comp" "handy!")
  (with-out-str (desc "comp" "just handy")) => "Updated record.\n"
  (@#'desc.core/fetch-records) => [{:name "comp" :desc "just handy"}])

(fact "desc finds no records"
  (with-out-str (desc "NONE")) => "No records found.\n")

(fact "desc finds one record"
  (seed-search-data)
  (with-out-str (desc "bind")) => "Name: binding\nDesc: only redefines dynamic vars\n"
  (with-out-str (desc #"bind")) => "Name: binding\nDesc: only redefines dynamic vars\n")

(fact "desc finds two records"
  (seed-search-data)
  (with-out-str (desc "i")) =>

"+--------------+-----------------------------+
| name         | desc                        |
+--------------+-----------------------------+
| binding      | only redefines dynamic vars |
| with-out-str | capture *out*               |
+--------------+-----------------------------+\n")

; after-all: cleanup test-dir
(.delete (io/file test-dir ".desc.clj"))
(.delete test-dir)
