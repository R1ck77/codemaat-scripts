(ns json-conversion.core
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.read :as read]
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

(defn common-files [codemaat-contents cloc-contents]
  (apply hash-set (map first (filter #(contains? cloc-contents (first %)) codemaat-contents))))

;;; FIXME this is too big!
(defn merge-data [codemaat-contents cloc-contents]
  (map #(apply str (interpose "," %)) 
       (reduce #(conj % [%2 
                         (str (:revisions (get codemaat-contents %2)))
                         (str (:code (get cloc-contents %2)))])
               [["module" "revisions" "code"]] 
               (common-files codemaat-contents cloc-contents))))

(defn merge [codemaat-file cloc-file]
  (merge-data (read/into-dictionary (read/read-codemaat-contents (slurp codemaat-file))) 
              (read/into-dictionary (read/read-cloc-contents (slurp cloc-file)))))

(defn -main [& args]
  (case (first args)
    "jsonify" (println (json/convert-hierarchy ["root" (hierarchy-from-file (first args) (Integer/valueOf (nth args 2)))]))
    "merge" (dorun (map println (merge (second args) (nth args 2))))))

