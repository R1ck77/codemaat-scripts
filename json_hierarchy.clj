(ns script
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]))

(defn split-simple-csv-line
  [line]
  {:pre [(not (.contains line "\\,"))]}
  (cstring/split line #","))

(defn read-file [filename]
  (map split-simple-csv-line (drop 1 (split-lines (slurp filename))) ))

(defn split-module [module]
  (cstring/split module (re-pattern File/separator)))

(defn create-file-hierarchy [module revisions code]
  (let [inverted-path (reverse (split-module module))]
    (reduce (fn [acc value]
              {value [acc]})
            {
             :name (first inverted-path)
             :weight revisions
             :size code} (rest inverted-path))))

(defn merge-hierarchies [h1 h2]
  (merge-with  #(if (vector? %) (vec (concat % %2)) %)
               h1 h2))

(defn add-file-to-hierarchy [hierarchy file-fields]
  (merge-hierarchies hierarchy (apply create-file-hierarchy file-fields)))

(defn create-hierarchy [lines-as-fields]
  {:pre [(not (empty? lines-as-fields))]}
  (reduce add-file-to-hierarchy {} lines-as-fields))

(def jsonify-hierarchy nil)

(defn jsonify-file-node [{name :name weight :weight size :size}]
  (format
"{ 
    \"name\" : \"%s\",
    \"size\" : \"%s\",
    \"weight\" : \"%s\"
}", name, size, weight))

(defn jsonify-childrens [child-nodes]
  (apply str(interpose "," (map jsonify-hierarchy child-nodes))))

(defn jsonify-dir-node [dir-node]
  (let [key (first (keys dir-node))]
    (format "{
    \"name\" : \"%s\",
    \"children\" : [
        %s
    ]\n}",key, (jsonify-childrens (get dir-node key)))))

(defn jsonify-hierarchy [hierarchy]
  (if (contains? hierarchy :name)
    (jsonify-file-node hierarchy)
    (jsonify-dir-node hierarchy)))

