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

(defn convert-to-file-node [revisions size]
  {:weight revisions, :size size})

(defn add-file-to-hierarchy [hierarchy [module revisions lines :as fields]]
  (assoc-in hierarchy
            (split-module module)
            (convert-to-file-node revisions lines)))

(defn create-hierarchy [lines-as-fields]
  {:pre [(not (empty? lines-as-fields))]}
  (reduce add-file-to-hierarchy {} lines-as-fields))


