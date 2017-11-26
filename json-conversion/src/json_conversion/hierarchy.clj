(ns json-conversion.hierarchy
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.read :as read]
            [json-conversion.merge :as merge]
            [json-conversion.jsonify :as json]))

(defn compute-normalization [xlines normalization]
  (if (pos? normalization) 
    normalization 
    (apply max (map second xlines))))

(defn normalize-line [norm-factor [file weight size]]
  [file (/ (double weight) norm-factor) size])

(defn normalize-data [xlines normalization]
  (map (partial normalize-line (compute-normalization xlines normalization)) 
       xlines))

(defn split-module [module]
  (cstring/split module (re-pattern File/separator)))

(defn convert-to-file-node [revisions size]
  {:weight revisions, :size size})

(defn add-file-to-hierarchy [hierarchy [module revisions lines :as fields]]
  (assoc-in hierarchy
            (split-module module)
            (convert-to-file-node revisions lines)))

(defn hierarchy-from-file [filename commits]
  (reduce add-file-to-hierarchy 
          {}  (normalize-data (read/read-merged-file-contents (slurp filename)) commits)))
