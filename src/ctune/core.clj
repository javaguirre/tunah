(ns ctune.core
  (:require [incanter.charts :refer [xy-plot]]
            [seesaw.core :as ui]
            [seesaw.mig :refer [mig-panel]]
            [seesaw.bind :as bind]
            [ctune.audio :refer [open-mic audio-data powers-data]])
  (:import org.jfree.chart.ChartPanel
           java.lang.Math)
  (:gen-class))


(def sample-rate 8000)
(def buffer-size 4096)

(defn wave-plot [data samples frequency]
  (xy-plot (range 0 (/ buffer-size 2)) data :x-label frequency))

(def freq (atom nil))

(defn pow2 [i]
  (Math/pow i 2))

(defn filters [data]
  (map pow2 data))

(defn calc-freq [data]
  (reset! freq (* (/ (/ sample-rate 2.0) buffer-size)
                  (first (apply max-key second (map-indexed vector data))))))

(def notes [{:frequency 440 :note "a4" :common_name "A"}
            {:frequency 493 :note "b4" :common_name "B"}
            {:frequency 523 :note "c5" :common_name "C"}
            {:frequency 587 :note "d5" :common_name "D"}
            {:frequency 659 :note "e5" :common_name "E"}
            {:frequency 698 :note "f5" :common_name "F"}
            {:frequency 783 :note "g5" :common_name "G"}
            {:frequency 880 :note "a5" :common_name "A"}
            {:frequency 987 :note "b5" :common_name "B"}])

(defn calculate-note [frequency]
  (loop [remaining-notes notes note-range {:min 0 :max 1000 :min_note {} :max_note {}}]
    (println note-range)
    (if (empty? remaining-notes)
      note-range
      (let [[note & remaining] remaining-notes]
        (recur remaining
               (if (and (< frequency (:frequency note)) (< (:frequency note) (:max note-range)))
                 (merge note-range {:max_note note :max (:frequency note)})
                 (if (and (> frequency (:frequency note)) (< (:min note-range) (:frequency note)))
                   (merge note-range {:min_note note :min (:frequency note)})
                   note-range)
               ))))))

(defn -main []
  (do
    (ui/native!)
    (open-mic sample-rate buffer-size)
    (let [plot (wave-plot @audio-data sample-rate "0")
          signal-chart (ChartPanel. plot)
          frequency-plot (wave-plot @powers-data sample-rate "0")
          frequency-chart (ChartPanel. frequency-plot)
          app-panel (mig-panel
                     :constraints []
                     :items [[frequency-chart]])]
      (bind/bind powers-data
                 (bind/b-do* (fn [_] (let [data (filters @powers-data)
                                           freq (calc-freq data)]
                                       (println freq (calculate-note freq))
                                       (.setChart frequency-chart (wave-plot data sample-rate (str freq)))
                                       (calc-freq data)))))
      (ui/invoke-later
       (-> (ui/frame :title "Frequencies"
                     :content app-panel
                     :on-close :exit)
           ui/pack! ui/show!)))))
