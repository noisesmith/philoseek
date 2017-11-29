(ns org.noisesmith.philoseek-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [org.noisesmith.poirot :as p]
            [org.noisesmith.philoseek :refer :all]))

(def basic-dump-file "test/data/parsed-wiki-data.transit.json")

(deftest search-test
  (testing "basic functionality"
    (let [extracted (extract-link (p/restore basic-dump-file))]
      (is (string? extracted)
          "we got the right kind of data, at least")
      (is (string/starts-with? extracted "/wiki/")
          "this is probably a link to another wikipedia page"))))
