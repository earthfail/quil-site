(ns quil-site.examples.tailspin
  (:require [quil.core :as q :include-macros true]
            [quil-site.main :as main]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  (let [max-r (/ (q/width) 2)]
   {:dots (into [] (for [r (map #(* max-r %)
                                (range 0 1 0.05))]
                     [r 0]))}))

(defn move [dot]
  (let [[r a] dot]
    [r (+ a (* r 0.001))]))

(defn update [state]
  (update-in state [:dots] #(map move %)))

(defn dot->coord [[r a]]
  [(+ (/ (q/width) 2) (* r (q/cos a)))
   (+ (/ (q/height) 2) (* r (q/sin a)))])

(defn draw [state]
  (q/background 250)
  (q/fill 0)
  (let [dots (:dots state)]
    (loop [curr (first dots)
           tail (rest dots)
           prev (last dots)]
      (let [[x y] (dot->coord curr)]
        (q/ellipse x y 5 5)
        (when prev
          (let [[x2 y2] (dot->coord prev)]
            (q/line x y x2 y2))))
      (when (seq tail)
        (recur (first tail)
               (rest tail)
               curr)))))

(defn run-sketch [host size]
  (q/sketch
    :host host
    :size [size size]
    :setup setup
    :update update
    :draw draw
    :middleware [m/fun-mode]))

(main/register-example! "tailspin" "Erik Svedäng" run-sketch)