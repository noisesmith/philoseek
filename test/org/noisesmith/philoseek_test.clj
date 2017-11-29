(ns org.noisesmith.philoseek-test
  (:require [clojure.test :refer :all]
            [org.noisesmith.poirot :as p]
            [org.noisesmith.philoseek :refer :all]))

(def basic-dump-file "test/data/parsed-wiki-data.transit.json")

(deftest search-test
  (testing "basic functionality"
    (is (string? (extract-link (p/restore basic-dump-file))))))
