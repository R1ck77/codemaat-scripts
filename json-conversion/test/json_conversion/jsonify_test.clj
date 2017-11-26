(ns json-conversion.jsonify-test
  (:require [clojure.test :refer :all]
            [json-conversion.jsonify :refer :all]))

(deftest test-branch-to-json
  (testing "can convert a single file"
    (is (= "{
\"name\" : \"foo\",
\"weight\" : \"5\",
\"size\" : \"7\"
}
" (convert-hierarchy ["foo" {:weight 5, :size 7}]))))
  (testing "can convert a nested file"
    (is (= "{
\"name\" : \"foo\",
\"children\" : [
{
\"name\" : \"bar\",
\"weight\" : \"3\",
\"size\" : \"2\"
}
]
}
" (convert-hierarchy ["foo" { "bar" { :weight 3, :size 2}}]))))
  (testing "can do unimmaginable things"
    (is (= "{
\"name\" : \"foo\",
\"children\" : [
{
\"name\" : \"bar\",
\"weight\" : \"3\",
\"size\" : \"2\"
}
,{
\"name\" : \"baz\",
\"children\" : [
{
\"name\" : \"foobar\",
\"children\" : [
{
\"name\" : \"baz\",
\"weight\" : \"-1\",
\"size\" : \"-2\"
}
]
}
]
}
]
}
" (convert-hierarchy ["foo" {
                             "bar" { :weight 3, :size 2}
                             "baz" { "foobar" {"baz" {:weight -1, :size -2}}}}])))))


