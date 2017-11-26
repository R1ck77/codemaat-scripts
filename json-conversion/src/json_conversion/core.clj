(ns json-conversion.core
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.jsonify :as json]))

(defn split-simple-csv-line
  [line]
  {:pre [(not (.contains line "\\,"))]}
  (cstring/split line #","))

(defn convert-values [[s n1 n2]]
  [s (Integer/valueOf n1) (Integer/valueOf n2)])

(defn read-file [raw-contents]
  (map convert-values (map split-simple-csv-line (drop 1 (split-lines raw-contents)))))

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
          {}  (normalize-data (read-file (slurp filename)) commits)))

(defn -main [& args]
  (let [revisions (Integer/valueOf (second args))]
    (println (json/convert-hierarchy ["root" (hierarchy-from-file (first args)
                                                                  revisions)]))))

