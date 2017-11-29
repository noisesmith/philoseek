(ns org.noisesmith.philoseek-test
  (:require [clojure.test :refer :all]
            [org.noisesmith.poirot :as p]
            [org.noisesmith.philoseek :refer :all]))

(deftest search-test
  (testing "basic functionality"
    (is (coll? (raw-search)))))
