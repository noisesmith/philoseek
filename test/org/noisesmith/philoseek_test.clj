(ns org.noisesmith.philoseek-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [org.noisesmith.poirot :as p]
            [org.noisesmith.philoseek :refer :all]))

(deftest find-tag-test
  (let [finder (find-tag :a :b)]
  (is (empty? (sequence finder [{:tag :c} {:tag :d}])))
  (is (seq (sequence finder [{:tag :a}])))))

(deftest not-internal-resource?-test
  (is (not-internal-resource? "/wiki/Foo"))
  (is (not-internal-resource? "bar"))
  (is (not (not-internal-resource? "/wiki/Wikipedia:blah")))
  (is (not (not-internal-resource? "/wiki/bargle(disambiguation)"))))

(deftest wiki-link?-test
  (is (wiki-link? "/wiki/foo"))
  (is (not (wiki-link? "/foo"))))

(deftest non-parenthetical-string?-test
  (testing "we can reject parentheticals"
    (is (non-parenthetical-string? "foo"))
    (is (not (non-parenthetical-string? "(foo)")))
    (is (do (non-parenthetical-string? nil)
            :whatever))))

(deftest not-parenthetical?-test
  (is (not (not-parenthetical? nil)))
  (is (not-parenthetical? {:content ["foo"]}))
  (is (not (not-parenthetical? {:content ["(foo)"]}))))

(deftest find-wiki-test
  (let [input (p/restore "test/data/should-find-href.transit.json")]
    (is (seq (sequence find-wiki input)))))

(def basic-dump-file "test/data/parsed-wiki-data.transit.json")

(def file-error-dump-file "./test/data/uses-file-url.transit.json")

(deftest extract-link-test
  (testing "basic functionality"
    (let [extracted (extract-link (p/restore basic-dump-file))]
      (is (some? extracted)
          "we actually get non nil data back")
      (is (string? extracted)
          "we got the right kind of data, at least")
      (is (and (string? extracted)
               (string/starts-with? extracted "/wiki/"))
          "this is probably a link to another wikipedia page")))
  (testing "we skip wikipedia resources and just use articles"
    (let [extracted (extract-link (p/restore file-error-dump-file))]
      (is (some? extracted)
          "we actually get non nil data back")
      (is (string? extracted)
          "we got the right kind of data, at least")
      (is (and (string? extracted)
               (string/starts-with? extracted "/wiki/"))
          "this is probably a link to another wikipedia page")
      (is (not= "/wiki/File:Question_book-new.svg" extracted)
          "we don't use this resource which is not a page")
      (is (= "/wiki/Riihim%C3%A4ki" extracted)
          "this is probably the link we want"))))
