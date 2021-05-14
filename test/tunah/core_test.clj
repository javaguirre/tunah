(ns tunah.core-test
  (:require [clojure.test :refer :all]
            [tunah.tone :refer
             [calculate-note, get-note-range get-closer-note]]))

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

(deftest get-closer-note-max-note-chosen
  (testing "Max note gets chosen when a closer note is selected"
    (let [frequency 600
          max-note {:frequency 603 :note "XX" :common_name "X"}
          note-range {:max-note max-note
                      :min-note {:frequency 400 :note "XX" :common_name "X"}}]
      (is (= (get-closer-note frequency note-range)
             max-note)))))

(deftest get-closer-note-min-note-chosen
  (testing "Min note gets chosen when a closer note is selected"
    (let [frequency 430
          max-note {:frequency 603 :note "XX" :common_name "X"}
          min-note {:frequency 400 :note "XX" :common_name "X"}
          note-range {:max-note max-note
                      :min-note min-note}]
      (is (= (get-closer-note frequency note-range)
             min-note)))))
