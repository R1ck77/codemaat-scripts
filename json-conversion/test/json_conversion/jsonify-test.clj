(ns json-conversion.jsonify-test
  (:require [clojure.test :refer :all]
            [json-conversion.jsonify :refer :all]))

(deftest test-to-json
  (testing "can convert a single file"
    (is (= "{\n\"name\" : \"foo\",\n\"weight\" : \"5\",\"size\" : \"7\"\n}"
           (to-json {"foo" {:weight 5, :size 7}})))))


