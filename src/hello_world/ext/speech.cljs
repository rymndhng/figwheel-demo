(ns hello-world.ext.speech)

(defonce voice
  (let [voices (js/speechSynthesis.getVoices)]
    (get voices 10)))

(defn speak! [text]
  (let [utterance (js/SpeechSynthesisUtterance. text)]
    (aset utterance "voice" voice)
    (js/speechSynthesis.speak utterance)))
