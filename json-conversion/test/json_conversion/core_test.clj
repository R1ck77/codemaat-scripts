(ns json-conversion.core-test
  (:require [clojure.test :refer :all]
            [json-conversion.core :refer :all]))

(deftest test-add-file-to-hierarchy
  (testing "can add a single file to an empty hierarchy"
    (is (= {"foo" {:weight 5, :size 7}}
           (add-file-to-hierarchy {}
                                  ["foo" 5 7]))))
  (testing "can add a single file to a hierarchy that contains another file"
    (is (= {"foo" { :weight 5, :size 7}
            "bar" { :weight 2, :size 1}}
           (add-file-to-hierarchy {"bar" {:weight 2, :size 1}}
                                  ["foo" 5 7]))))
  (testing "can add a file inside a directory into an empty hierarchy"
    (is (= {"foo" {"bar" {:weight 3.0, :size 5}}}
           (add-file-to-hierarchy {} ["foo/bar" 3.0 5]))))
  (testing "can add a file deep in directories into an empty hierarchy"
    (is (= {"foo" {"bar" {"baz" { :weight 3.0, :size 5}}}}
           (add-file-to-hierarchy {} ["foo/bar/baz" 3.0 5]))))
  (testing "can add a nested file to a hierarchy with a plain file"
    (is (= {"foo" {"bar" {"baz" { :weight 3.0, :size 5}}},  "x" { :weight 0.0 :size -1}}
           (add-file-to-hierarchy {"x" { :weight 0.0 :size -1}} ["foo/bar/baz" 3.0 5]))))
  (testing "can add a nested file to a hierarchy with a partially overlapping tree"
    (is (= {"foo" {"x"  { :weight 0.0, :size -1}
                   "bar" {"baz" { :weight 3.0, :size 5}}}}
           (add-file-to-hierarchy {"foo" { "x" { :weight 0.0 :size -1}}} ["foo/bar/baz" 3.0 5])))))


