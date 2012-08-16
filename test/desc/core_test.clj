(ns desc.core-test
  (:use midje.sweet
        desc.core)
  (:require [clojure.java.io :as io]))

; before-all: create temp test-dir
(def test-dir (-> (io/resource *file*) .getPath io/file .getParentFile (io/file "tmp")))
(.mkdir test-dir)
(System/setProperty "user.home" (str test-dir))

(background (before :facts (reset! records nil)))

(fact "nonexistent .desc.clj yields vector"
  (@#'desc.core/check-records)
    @records => [])

(fact "desc adds a record"
  (desc "comp" "handy!")
  (@#'desc.core/fetch-records) => [{:name "comp" :desc "handy!"}])

(fact "desc updates a record"
  (desc "comp" "handy!")
  (desc "comp" "just handy")
  (@#'desc.core/fetch-records) => [{:name "comp" :desc "just handy"}])

; after-all: cleanup test-dir
(.delete (io/file test-dir ".desc.clj"))
(.delete test-dir)
