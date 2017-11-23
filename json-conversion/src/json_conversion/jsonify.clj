(ns json-conversion.jsonify)

(def hierarchy-to-json nil)

(defn file-node-to-json [{name :name weight :weight size :size}]
  (format
"{ 
    \"name\" : \"%s\",
    \"size\" : \"%s\",
    \"weight\" : \"%s\"
}", name, size, weight))

(defn childs-to-json [child-nodes]
  (apply str (interpose "," (map hierarchy-to-json child-nodes))))

(defn dir-node-to-json [dir-node]
  (let [key (first (keys dir-node))]
    (format "{
    \"name\" : \"%s\",
    \"children\" : [
        %s
    ]\n}",key, (childs-to-json (get dir-node key)))))

(defn to-json [hierarchy]
  "{
\"name\" : \"foo\",
\"weight\" : \"5\",
\"size\" : \"7\"
}")

