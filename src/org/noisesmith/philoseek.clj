(ns org.noisesmith.philoseek
  (:require [org.noisesmith.poirot :as p]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.xml :as x]))

(defn find-tag
  [& tags]
  (filter (comp (set tags) :tag)))

(defn not-internal-resource?
  [uri]
  (and
   (every? (fn [s] (not (string/starts-with? uri s)))
           ["/wiki/Wikipedia:"
            "/wiki/File:"
            "/wiki/Help:"
            "/wiki/Talk:"
            "/wiki/Template:"
            "/wiki/Category:"])
   (not (some (fn [s] (string/ends-with? uri s))
              ["(disambiguation)"]))))

(defn wiki-link?
  [uri]
  (string/starts-with? uri "/wiki/"))

(defn non-parenthetical-string?
  [s]
  (and (some? s)
       (not (re-matches #"\(.*\)" s))))

(defn not-parenthetical?
  [link]
  (some-> link
          (:content)
          (->> (tree-seq coll? seq)
               (filter string?))
          (first)
          (non-parenthetical-string?)))

(defn a-href
  [a]
  (get-in a [:attrs :href]))

(def find-wiki
  "based on a bunch of repl exploration using the dumped data, this seems
   to be the thing that will predictably get us the right link"
  (filter (fn [link]
            (let [uri (a-href link)]
              (and (some? uri)
                   (wiki-link? uri)
                   (not-internal-resource? uri)
                   (not-parenthetical? link))))))

(def debug (atom []))

(defn prune-italics
  [branch]
  (and (coll? branch)
       (swap! debug conj branch)
       (not (seq (set/intersection #{:style}
                              (set (keys (:attrs branch))))))))

(defn extract-link
  "function that exists for exploring our data in the repl"
  [body exclude]
  (-> body
      (->> (tree-seq prune-italics seq)
           (sequence (comp (find-tag :a)
                           find-wiki
                           (remove (comp exclude a-href)))))
      (first)
      (a-href)))

(def random-page "/wiki/Special:Random")

(defn raw-search
  "Tries to find the Philosophy page on wikipedia in a roundabout manner."
  ([] (raw-search random-page))
  ([url] (raw-search url #{}))
  ([url exclude]
   (-> url
       (->> (str "https://en.wikipedia.org"))
       (x/parse)
       (extract-link exclude))))

(defn search
  ([] (search random-page))
  ([page]
   (take-while identity (cons page (search page #{page}))))
  ([page seen]
   (when-not (contains? seen "/wiki/Philosophy")
     (let [next-page (raw-search page seen)]
       (lazy-seq
        (cons next-page
              (search next-page (conj seen next-page))))))))
