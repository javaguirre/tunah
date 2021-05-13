(ns tunah.core-test
  (:require [clojure.test :refer :all]
            [tunah.audio :refer :all]
            [tunah.core :refer :all]))

(deftest cicd-test
  (testing "Test CI/CD"
    (is (= 1 1))))
