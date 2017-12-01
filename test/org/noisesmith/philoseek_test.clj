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

(def file-error-dump-file "./test/data/uses-file-url.transit.json")

(deftest valid-url-test
  (testing "we skip wikipedia resources and just use articles"
    (let [extracted (extract-link (p/restore file-error-dump-file))]
      (is (string? extracted)
          "we got the right kind of data, at least")
      (is (string/starts-with? extracted "/wiki/")
          "this is probably a link to another wikipedia page")
      (is (not= "/wiki/File:Question_book-new.svg" extracted)
          "we don't use this resource which is not a page")
      (is (= "/wiki/Riihim%C3%A4ki" extracted)
          "this is probably the link we want"))))
