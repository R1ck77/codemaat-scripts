(ns json-conversion.read
  (:require [clojure.string :refer [split-lines] :as cstring]))

(defn split-simple-csv-line
  [line]
  {:pre [(not (.contains line "\\,"))]}
  (cstring/split line #","))

(defn convert-3-values [[s n1 n2]]
  [s (Integer/valueOf n1) (Integer/valueOf n2)])

(defn convert-codemaat-values [[s n]]
  { 
   :file s
   :revisions (Integer/valueOf n)})

(defn to-int [s]
  (Integer/valueOf s))

(def cloc-field-names [:lang :file :blank :comment :code])
(def cloc-conversions [ identity identity to-int to-int to-int])

(defn convert-cloc-values [fields]
  (into {}  
        (map #(vector % %2) 
             cloc-field-names (map #(% %2) 
                                   cloc-conversions fields))))

(defn read-csv-contents 
  ([raw-contents] (read-csv-contents raw-contents identity))
  ([raw-contents transform-function]
   (map transform-function (map split-simple-csv-line (drop 1 (split-lines raw-contents))))))

(defn read-merged-file-contents [raw-contents]
  (read-csv-contents raw-contents convert-3-values))

(defn read-codemaat-contents [raw-contents]
  (read-csv-contents raw-contents convert-codemaat-values))

(defn read-cloc-contents [raw-contents]
  (read-csv-contents raw-contents convert-cloc-values))
