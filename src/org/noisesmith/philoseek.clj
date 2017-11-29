(ns org.noisesmith.philoseek
  (:require [org.noisesmith.poirot :as p]
            [clojure.string :as string]
            [clojure.xml :as x]))

(defn find-tag
  [& tags]
  (filter (comp (set tags) :tag)))

(def find-wiki
  "based on a bunch of repl exploration using the dumped data, this seems
   to be the thing that will predictably get us the right link"
  (filter (fn [link]
            (some-> link
                    (:attrs)
                    (:href)
                    (as-> uri
                      (and (string/starts-with? uri "/wiki/")
                           (not (string/starts-with? uri "/wiki/Wikipedia:"))))))))

(defn extract-link
  "function that exists for exploring our data in the repl"
   [body]
    (-> body
        (->> (tree-seq coll? seq)
             (sequence (comp (find-tag :a)
                             find-wiki)))
        first
        (:attrs)
        (:href)))

(defn raw-search
  "Tries to find the Philosophy page on wikipedia in a roundabout manner."
  ([] (raw-search "https://en.wikipedia.org/wiki/Special:Random"))
  ([url]
   (-> url
       (x/parse)
       (extract-link))))
