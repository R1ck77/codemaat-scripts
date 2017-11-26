(ns json-conversion.merge
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.read :as read]
            [json-conversion.jsonify :as json]))

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

