(ns cv.gate)

;; magic numbers
(def default-ratio 8/10)
(def default-max 0) ;; TODO: find avg max across channels
(def default-length 8)

;; fn
(defn gate
  "
  if the signal is above ratio of the channel's max for n frames
  we consider the gate high, otherwise low

  returns a map of whether the gate was high, and the total gate length
  "
  ([buffer]
   (gate {:max default-max} buffer default-length default-ratio))

  ([channel buffer]
   (gate channel buffer default-length default-ratio))

  ([channel buffer n]
   (gate channel buffer n default-ratio))

  ([channel buffer n ratio]
   (let [threshold (* ratio (:max channel))
         length  (reduce (fn [l x]
                           (if (or (> l n) (> x threshold))
                             (inc l)
                             0))
                         0 buffer)]
     {:gate (> length n) :length length})))

(defn trigger [buffer]
  "
  triggers are simpler, as we only need to know whether a trigger
  happened during an animation frame, and can merge multiple
  triggers together since we can only impact the effects system
  once per render
  "
  (:gate (gate buffer)))