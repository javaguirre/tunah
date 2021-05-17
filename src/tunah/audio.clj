(ns tunah.audio
  (:import javax.sound.sampled.AudioFormat
           javax.sound.sampled.AudioSystem
           mikera.matrixx.algo.FFT))


(def audio-data (atom []))
(def powers-data (atom []))

(defn listen-audio [sample-rate buffer-size]
  (let [audio-format (AudioFormat. sample-rate 8 1 true false)
        line (AudioSystem/getTargetDataLine audio-format)]
    (.open line)
    (.start line)
    (while true
      (let [buffer (make-array (Byte/TYPE) buffer-size)
            fft (FFT. buffer-size)
            fft-buffer (double-array [])]
        (.read line buffer 0 buffer-size)
        (let [fft-buffer (double-array buffer)]
          (.realForward fft fft-buffer)
          (reset! audio-data (vec buffer))
          (reset! powers-data (vec fft-buffer)))))))


(defn open-mic [sample-rate buffer-size]
  (.start (Thread. #(listen-audio sample-rate buffer-size))))
