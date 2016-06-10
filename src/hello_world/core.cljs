(ns hello-world.core
  (:require-macros [cljs.repl :refer [doc]])
  (:require [reagent.core :as reagent :refer [atom]]
            [devtools.core :as devtools]
            [hello-world.ext.speech :as speech]))

(devtools/install!)
(enable-console-print!)

(println "This text is printed from src/hello-world/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(def TRACK-LENGTH 5)

(defonce app-state (atom {:racers []}))

(defn create-racer [name]
  {:name  name
   :score 0})

(defn create-racer! [name]
  (let [racer (create-racer name)]
    (swap! app-state update :racers conj racer)))

(defn reset-racers [racers]
  (mapv #(assoc % :score 0) racers))

(defn reset-racers! []
  (swap! app-state update :racers reset-racers))

(defn increment-racer! [index]
  (swap! app-state update-in [:racers index :score] inc))

(defn str-percent [ratio]
  (str (* 100 ratio) "%"))

(defn winner [racers]
  (first (filter #(>= (:score %) TRACK-LENGTH) racers)))

(defn check-winner [state]
  (when-let [winner (winner (:racers state))]
    (let [msg (str (:name winner) " has won the race!")]
      (speech/speak! msg)
      (js/alert msg))
    (reset-racers!)))

(add-watch app-state :check-winner
           (fn [key ref old-state new-state]
             (check-winner new-state)))

(defn track [index racer]
  (let [completed (str-percent (/ (:score racer) TRACK-LENGTH))]
    [:div {:key (str "track-" index)
           :style {:display :inline}}
     [:h5 (with-out-str (println "#" (inc index) (:name racer)))]
     [:button {:onClick #(increment-racer! index)} "Race!"]

     [:svg {:width "100%" :height "100%"}
      [:rect {:width "100%" :height "50%" :style {:fill :blue}}]
      [:rect {:width  completed
              :height "50%" :style {:fill :green}}]
      [:image {:href (str "dogs/" (:name racer) ".png")
               :x completed
               :y 0
               :height "50px"
               :width "50px"}]
      ]]))

(defn add-racer []
  [:form {:onSubmit (fn [e]
                      (.preventDefault e)
                      (let [element (-> e .-target .-elements .-playername)
                            name    (-> element .-value)]
                        (create-racer! name)
                        (aset element "value" "")))}
   [:input {:type        :text
            :name        "playername"
            :placeholder "Your Name"}]
   [:button {:type :submit} "Add Racer"]])

(defn hello-world []
  [:div
   [:h1 "Racers"]
   (map-indexed track (@app-state :racers))
   [add-racer]
   ])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
