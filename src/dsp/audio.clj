(ns dsp.audio
  (:require [clojure.core.async :as a])
  (:import javax.sound.sampled.AudioFormat
           javax.sound.sampled.AudioSystem
           javax.sound.sampled.TargetDataLine
           javax.sound.sampled.DataLine$Info
           java.nio.ByteBuffer))


(def audio-data (atom []))
(def keep-running? (atom true))

;;(def audio-format (AudioFormat. 8000.0 16 1 true true))

;;(def line (AudioSystem/getTargetDataLine audio-format))

(defn listen-audio [sample-rate buffer-size]
  (let [audio-format (AudioFormat. sample-rate 16 1 true true)
        line (AudioSystem/getTargetDataLine audio-format)]
    (println "Opening line")
    (.open line)
    (println "Starting line")
    (.start line)
    (while true
      (let [buffer (make-array (Byte/TYPE) buffer-size)]
        ;;(println (last @audio-data))
        (println (.read line buffer 0 buffer-size))
        (reset! audio-data (vec buffer))))))

(defn open-mic [sample-rate buffer-size]
  (.start (Thread. #(listen-audio sample-rate buffer-size))))
