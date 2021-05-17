(ns tunah.tone)

(def min-note {:frequency 440 :note "a4" :common_name "A"})
(def max-note {:frequency 987 :note "b5" :common_name "B"})
(def notes [min-note
            {:frequency 493 :note "b4" :common_name "B"}
            {:frequency 523 :note "c5" :common_name "C"}
            {:frequency 587 :note "d5" :common_name "D"}
            {:frequency 659 :note "e5" :common_name "E"}
            {:frequency 698 :note "f5" :common_name "F"}
            {:frequency 783 :note "g5" :common_name "G"}
            {:frequency 880 :note "a5" :common_name "A"}
            max-note])

(defn get-note-range
  [frequency note-range note]
  (if (and (< frequency (:frequency note))
           (< (:frequency note) (:frequency (:max-note note-range))))
    (merge note-range {:max-note note})
    (if (and (> frequency (:frequency note))
             (< (:frequency (:min-note note-range)) (:frequency note)))
      (merge note-range {:min-note note})
      note-range)))

(defn get-closer-note [frequency note-range]
  (if (< (Math/abs (- (:frequency (:max-note note-range)) frequency))
         (Math/abs (- (:frequency (:min-note note-range)) frequency)))
    (:max-note note-range)
    (:min-note note-range)))

(defn calculate-note [frequency]
  (loop [remaining-notes notes
         note-range {:min-note min-note :max-note max-note}]
    (if (empty? remaining-notes)
      note-range
      (let [[note & remaining] remaining-notes]
        (recur remaining
               (get-note-range frequency note-range note))))))
