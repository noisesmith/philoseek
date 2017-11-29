(ns org.noisesmith.philoseek
  (:require [org.noisesmith.poirot :as p]
            [clojure.xml :as x]))

(defn find-tag
  [& tags]
  (filter (comp (set tags) :tag)))

(defn extract-link
  "finds the next link to follow from the body"
  [body]
  body)

(defn raw-search
  "Tries to find the Philosophy page on wikipedia in a roundabout manner."
  ([] (raw-search "https://en.wikipedia.org/wiki/Special:Random"))
  ([url]
   (-> url
       (x/parse)
       #_ (doto (p/dump "parsed-wiki-data"))
       (extract-link))))


(def basic-dump-file "test/data/parsed-wiki-data.transit.json")

(defn explore-links
  "function that exists for exploring our data in the repl"
   [n]
    (-> (p/restore basic-dump-file)
        (->> (tree-seq coll? seq)
             (sequence (find-tag :a)))
        (nth n)))

;; not the data we want: (explore-links 0) (explore-links 1) etc.

;; probably what we want? (explore-links 4)

;; why tree-seq: "I don't know much about web pages, web devs are always
;; doing all kinds of crazy stuff, but I do know that if I randomly jumble
;; through the data I'll probably find something with the properties that
;; let me know it's the thing I want, tree-seq is perfect for this kind of
;; ignorant searching"
