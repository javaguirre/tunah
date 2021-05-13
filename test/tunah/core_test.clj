(ns tunah.core-test
  (:require [clojure.test :refer :all]
            [tunah.audio :refer :all]
            [tunah.core :refer :all]))

(deftest cicd-test
  (testing "Test CI/CD"
    (is (= 1 1))))

(deftest get-note-range-updates-max
  (testing "Max note range gets updated"
      (let [frequency 600
            note {:frequency 698 :note "f5" :common_name "F"}
            note-range {:max-note {:frequency 1000 :note "XX" :common_name "X"}
                        :min-note {:frequency 400 :note "XX" :common_name "X"}}]
        (is (not= (get-note-range frequency note-range note) note-range)))))
