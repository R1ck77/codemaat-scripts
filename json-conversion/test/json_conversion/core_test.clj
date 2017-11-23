(ns json-conversion.core-test
  (:require [clojure.test :refer :all]
            [json-conversion.core :refer :all]))

(deftest test-add-file-to-hierarchy
  (testing "adding a single file to an empty hierarchy"
    (is (= [{:name "bar" :weight 1 :size 1}] (add-to-file-hierarchy [] ["foo" 1 1])))))


