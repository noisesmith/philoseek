(ns org.noisesmith.philoseek
  (:require [org.noisesmith.poirot :as p]
            [clojure.xml :as x]))

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
       (extract-link))))
