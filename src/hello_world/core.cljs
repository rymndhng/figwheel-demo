(ns hello-world.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/hello-world/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:count 0}))

(defn update-counter []
  (swap! app-state update :count (fn [x] (+ x 3))))

(defn hello-world []
  [:div
   [:h3 {:class "counter-name"} "Count"]
   [:h3 {:class "counter-value"} (:count @app-state)]
   [:button {:onClick update-counter}
    "Increment Me!"
    ]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
