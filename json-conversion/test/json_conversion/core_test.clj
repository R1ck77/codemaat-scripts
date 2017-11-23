(ns json-conversion.core-test
  (:require [clojure.test :refer :all]
            [json-conversion.core :refer :all]))

(deftest test-add-file-to-hierarchy
  (testing "can add a single file to an empty hierarchy"
    (is (= #{{:name "foo" :weight 5 :size 7}}
           (add-to-file-hierarchy #{}
                                  ["foo" 5 7]))))
  (testing "can add a single file to a hierarchy that contains another file"
    (is (= #{{:name "foo" :weight 5 :size 7}
             {:name "bar" :weight 2 :size 1}}
           (add-to-file-hierarchy #{{:name "bar" :weight 2 :size 1}}
                                  ["foo" 5 7]))))
  (testing "can add a file inside a directory into an empty hierarchy"
    (is (= #{{:name "foo"
              :contains #{
                          {
                           :name "bar"
                           :weight 3
                           :size 5}}}}
           (add-to-file-hierarchy #{} ["foo/bar" 3 5]))))
  (comment
    (testing "can add a file deep in directories into an empty hierarchy"
     (is (= #{{:name "foo"
               :contains #{
                           {
                            :name "bar"
                            :contains #{
                                        :name "baz"
                                        :weight 3
                                        :size 5}}}}}
            (add-to-file-hierarchy #{} ["foo/bar/baz" 3 5]))))))


