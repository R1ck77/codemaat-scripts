(ns json-conversion.jsonify-test
  (:require [clojure.test :refer :all]
            [json-conversion.jsonify :refer :all]))

(deftest test-to-json
  (testing "can convert a single file"
    (is (= "{
\"name\" : \"foo\",
\"weight\" : \"5\",
\"size\" : \"7\"
}"
           (to-json {"foo" {:weight 5, :size 7}}))))
  (testing "can convert a nested file"
    (is (= "{
\"name\" : \"foo\",
\"contains\" : [
{
\"name\" : \"bar\",
\"weight\" : \"3\",
\"size\" : \"2\"
}
]
}"
           (to-json {"foo" { "bar" { :weight 3, :size 2}}})))))


