(ns json-conversion.core-test
  (:require [clojure.test :refer :all]
            [json-conversion.core :refer :all]))

(deftest test-add-file-to-hierarchy
  (testing "adding a single file to an empty hierarchy"
    (is (= [{:name "foo" :weight 5 :size 7}]
           (add-to-file-hierarchy [] ["foo" 5 7])))))


