(ns dsp.fft
  (:require [cfft.core :as cfft]))

(defn calc [time-series]
  (cfft/fft time-series))
