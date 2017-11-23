(ns json-conversion.core
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.jsonify :as json]))

(defn split-simple-csv-line
  [line]
  {:pre [(not (.contains line "\\,"))]}
  (cstring/split line #","))

(defn read-file [filename]
  (map split-simple-csv-line (drop 1 (split-lines (slurp filename))) ))

(defn split-module [module]
  (cstring/split module (re-pattern File/separator)))

(defn convert-to-file-node [module revisions size]
  {:name module, :weight revisions, :size size})

(defn convert-to-node [module revisions size]
  (let [inverted-path-components (split-module module)]
    
    ))

(defn add-file-to-hierarchy [hierarchy [module revisions code :as fields]]
  (conj hierarchy (apply convert-to-file-node fields)))

(defn add-file-to-hierarchy [h [name revisions code :as fields]])

(defn create-hierarchy [lines-as-fields]
  {:pre [(not (empty? lines-as-fields))]}
  (reduce add-file-to-hierarchy {} lines-as-fields))


