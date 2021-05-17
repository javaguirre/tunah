(ns tunah.core
  (:require [seesaw.core :as ui]
            [seesaw.mig :refer [mig-panel]]
            [seesaw.bind :as bind]
            [tunah.audio :refer [open-mic audio-data powers-data]]
            [tunah.tone :refer [calculate-note]]
            [tunah.chart :refer
             [sample-rate buffer-size wave-plot pow2 filters calc-freq]]
            [clojure.tools.cli :refer [parse-opts]])
  (:import org.jfree.chart.ChartPanel)
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option (:default is applied first)
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :update-fn inc] ; Prior to 0.4.1, you would have to use:
                   ;; :assoc-fn (fn [m k _] (update-in m [k] inc))
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn -main [& args]
  (parse-opts args cli-options))
  (println cli-options)
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
            ui/pack! ui/show!))))
