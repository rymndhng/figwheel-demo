(ns hello-world.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/hello-world/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:count 0
                          :interval nil}))

(def TICK-MAX 30)

(defn update-counter []
  (swap! app-state update :count
         (fn [x]
           (if (>= x TICK-MAX)
             0
             (inc x)))))

(defn to-str-percent [ratio]
  (str (* 100 ratio) "%"))

(defn progress-bar [ratio]
  [:svg {:width "100%" :height "100%"
         :style {:shape-rendering "auto"}}
   [:rect {:width "100%"
           :height "100%"
           :style {:fill "#FFD4DA"}}]
   [:rect {:width (to-str-percent ratio)
           :height "100%"
           :style {:fill "#99D2E4"}}]])

(defn hello-world []
  [:div
   [:h3 {:class "counter-name"} "Time is up"]
   [progress-bar (/ (:count @app-state) 20)]
   [progress-bar (/ (:count @app-state) 30)]
   [:div
    [:button {:onClick update-counter}
     "Click Me!!"]]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(when-not (:interval @app-state)
  (js/console.log "Started interval timer!")
  (swap! app-state assoc :interval
         (js/setInterval
           (fn []
             (update-counter))
           1000)))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
