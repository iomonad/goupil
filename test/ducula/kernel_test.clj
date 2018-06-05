(ns ducula.kernel-test
  (:require [clojure.test :refer :all]
            [ducula.kernel :refer :all]))

(deftest trivial-test
  (testing "cc"
    (is (= 1 1))))
