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

(defn add-file-to-hierarchy [commits hierarchy [module revisions lines :as fields]]
  (assoc-in hierarchy
            (split-module module)
            (convert-to-file-node (double (/ (Double/valueOf revisions) commits)) lines)))

(defn create-hierarchy [lines-as-fields]
  {:pre [(not (empty? lines-as-fields))]}
  (reduce add-file-to-hierarchy {} lines-as-fields))

(defn hierarchy-from-file [filename commits]
  (reduce (partial add-file-to-hierarchy commits)
          {}  (read-file filename)))

(defn -main [& args]
  (let [revisions (Integer/valueOf (second args))]
    (println (json/convert-hierarchy ["root" (hierarchy-from-file (first args)
                                                                  revisions)]))))

