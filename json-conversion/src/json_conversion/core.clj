(ns json-conversion.core
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

(defn add-to-file-hierarchy [hierarchy [module revisions code :as fields]]
  (let [inverted-path (reverse (split-module module))]
    (reduce (fn [acc value]
              {value [acc]})
            {
             :name (first inverted-path)
             :weight revisions
             :size code} (rest inverted-path))))

(defn add-file-to-hierarchy [h [name revisions code :as fields]])

(defn create-hierarchy [lines-as-fields]
  {:pre [(not (empty? lines-as-fields))]}
  (reduce add-file-to-hierarchy {} lines-as-fields))

;;; This should go into a "jsonify" module, in a saner setup
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

