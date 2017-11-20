(ns script
  (:require [clojure.string :refer [split-lines] :as cstring]))

(defn split-simple-csv-line
  [line]
  {:pre [(not (.contains line "\\,"))]}
  (cstring/split line #","))

(defn read-file [filename]
  (map split-simple-csv-line (drop 1 (split-lines (slurp filename))) ))

(println "Hello, World!")
