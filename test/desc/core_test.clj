(ns desc.core-test
  (:use midje.sweet
        desc.core)
  (:require [clojure.java.io :as io]))

(def test-dir (-> (io/resource *file*) .getPath io/file .getParentFile (io/file "tmp")))
(.mkdir test-dir)
(System/setProperty "user.home" (str test-dir))

; TODO: reset delay
;(fact "nonexistent .desc.clj yields vector"
;  @@#'desc.core/records => [])

;(fact "simple .desc.clj yields a vector"
;  (spit (str (io/file test-dir ".desc.clj")) "[{:a 1}]")
;  @@#'desc.core/records => [{:a 1}])

(fact "desc adds a record"
  (desc "comp" "handy!")
  (@#'desc.core/fetch-records) => [{:name "comp" :desc "handy!"}])

; cleanup test-dir
(.delete (io/file test-dir ".desc.clj"))
(.delete test-dir)
