(ns json-conversion.core
  (:import [java.io File])
  (:require [clojure.string :refer [split-lines] :as cstring]
            [json-conversion.read :as read]
            [json-conversion.merge :as merge]
            [json-conversion.jsonify :as json]
            [json-conversion.hierarchy :as hierarchy]))

(defn -main [& args]
  (case (first args)
    "jsonify" (println (json/convert-hierarchy ["root" (hierarchy/hierarchy-from-file (second args) (Integer/valueOf (nth args 2)))]))
    "merge" (dorun (map println (merge/merge (second args) (nth args 2))))))

