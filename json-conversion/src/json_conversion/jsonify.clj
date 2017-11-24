(ns json-conversion.jsonify)

(def convert-hierarchy)

(defn leaf-to-json [name content]
  (str "{
\"name\" : \"" name "\",
\"weight\" : \"" (:weight content) "\",
\"size\" : \"" (:size content) "\"\n}\n"))

(defn branch-to-json [name hierarchy]
  (str "{
\"name\" : \"" name "\",
\"children\" : [\n" 
       (apply str (interpose "," (map convert-hierarchy hierarchy)))

       "]
}\n"))

(defn convert-hierarchy [[name content]]
  (if (contains? content :weight)    
    (leaf-to-json name content)
    (branch-to-json name content)))


