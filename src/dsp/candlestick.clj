(ns dsp.candlestick
  (:require [seesaw.core :as ui]
            [seesaw.mig :refer [mig-panel]]
            [seesaw.bind :as bind]

            [incanter.core :refer [to-dataset view sin]]
            [incanter.charts :refer [function-plot xy-plot]]
            [dsp.data :refer [sample]]
            [dsp.audio :refer [audio-data listen-audio]])
  (:import org.jfree.chart.ChartPanel)
  (:gen-class))


(def input-value (atom "10"))
(def my-chart (ChartPanel. (function-plot sin -10 10 :title @input-value)))

(def keep-running? (atom true))

(defn random-number []
  (reset! input-value (str (rand-int 100))))

(defn randomizer [timeout callback]
  (.start (Thread.
           (fn [] (while @keep-running?
                    (do
                      (Thread/sleep timeout)
                      (callback)))))))

(defn stop-randomizer []
  (reset! keep-running? false)
  (reset! keep-running? true))

(def audio-chart (ChartPanel. (xy-plot (range 0 8000) [])))

(defn -main [& args]
  (ui/native!)
  (start-listening)
  (let [user-input (ui/text :columns 2)
        my-panel (mig-panel
                  :constraints []
                  :items [[user-input "wrap"]
                          [audio-chart]])]
    ;;(bind/bind user-input input-value)
    (bind/bind audio-data
               (bind/b-do*
                #(.setChart audio-chart (xy-plot (range 0 8000) @audio-data))))
    (ui/invoke-later
     (-> (ui/frame :title "Dynamic Frequencies"
                   :content my-panel
                   )
         ui/pack! ui/show!))))
