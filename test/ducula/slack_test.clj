(ns ducula.slack-test
  (:require [clojure.test :refer :all]
            [ducula.slack :refer :all]))

(deftest connection-test
  (testing "Connection"
    (is (= 1 1))))
