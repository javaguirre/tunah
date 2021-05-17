(ns tunah.chart
  (:require [incanter.charts :refer [xy-plot]]))
(:import java.lang.Math)

(def sample-rate 8000)
(def buffer-size 4096)

(def freq (atom nil))

(defn wave-plot [data samples frequency]
  (xy-plot (range 0 (/ buffer-size 2)) data :x-label frequency))

(defn pow2 [i]
  (Math/pow i 2))

(defn filters [data]
  (map pow2 data))

(defn calc-freq [data]
  (reset! freq (* (/ (/ sample-rate 2.0) buffer-size)
                  (first (apply max-key second (map-indexed vector data))))))
