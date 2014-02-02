(def ^:private twitter (require :twitter))

(def ^:private ks {:consumer_key "jLoBEo5gcE7I9gTUJJnDoA",
  :consumer_secret "qEgqj9boQevqHFuLG6doVBpnBLeVhlWvPyiYi1yB3c",
  :access_token_key "349098849-aaRAy3HKGch6ExrbYn1pRwxWrrmf4sFyp6Et81dF",
  :access_token_secret "s1azqjj9QUGBwzvZzxM3AQnuvXMsUks03EKxQflc7Iwam"})

(def track "I think")
(def twit (twitter. ks))

(def ^:private i-think-filter (fn [x]
  (def ^:private y ((.-split ((.-toLowerCase x))) " " ))
  (def ^:private z ((.-index-of y) "i"))
  (def ^:private previousword (get y (- z 1)))
  (def ^:private afterword (get y (+ z 1)))
  (def ^:private afterword2 (get y (+ z 2)))
  (def ^:private has-col (if (== previousword nil) true (> ((.-search previousword) ":" ) -1)))
  (def ^:private has-at (if (== previousword nil) true (> ((.-search previousword) "@" ) -1)))
  (def ^:private after-not-that (== (== afterword2 "that") false))
  (def ^:private z-is-one (== z 1))
  (cond
    (== (== afterword "think") false) 0
    (and (== z 0) (== afterword "think")) ((.-join y) " ")
    (== afterword2 "i")  ((.-join ((.-filter y) (fn[ax,i,a] (if (<= z i) ax)) )) " ")
    (and (and z-is-one after-not-that) (or has-at has-col)) ((.-join ((.-filter y) (fn[ax,i,a] (if (<= z i) ax)) )) " "))))

(twit.stream "statuses/filter" {:track track,:filter_level "medium"}
  (fn [stream]
    (stream.on "data" (fn[data]
      (def ^:private ret (i-think-filter data.text))
      (if (== (or (== ret nil) (== ret 0)) false)
        (.log console ret))
      ))))

