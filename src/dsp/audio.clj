(ns dsp.audio
  (:require [clojure.core.async :as a]
            [cfft.core :as cfft])
  (:import javax.sound.sampled.AudioFormat
           javax.sound.sampled.AudioSystem
           javax.sound.sampled.TargetDataLine
           javax.sound.sampled.DataLine$Info
           java.nio.ByteBuffer
           mikera.matrixx.algo.FFT
           java.lang.Math))


(def audio-data (atom []))
(def powers-data (atom []))
(def keep-running? (atom true))
(def fft-buffer (double-array []))

(defn listen-audio [sample-rate buffer-size]
  (let [audio-format (AudioFormat. sample-rate 16 1 true true)
        line (AudioSystem/getTargetDataLine audio-format)]
    (println "Opening line")
    (.open line)
    (println "Starting line")
    (.start line)
    (while true
      (let [buffer (make-array (Byte/TYPE) buffer-size)
            fft (FFT. buffer-size)
            fft-buffer (double-array [])]
        (println (.read line buffer 0 buffer-size))
        (let [fft-buffer (double-array buffer)]
          (.realForward fft fft-buffer)
          (reset! audio-data (vec buffer))
          (reset! powers-data (map #(Math/pow % 2) (vec fft-buffer))))))))


(defn open-mic [sample-rate buffer-size]
  (.start (Thread. #(listen-audio sample-rate buffer-size))))
