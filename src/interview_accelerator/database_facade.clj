(ns interview-accelerator.database-facade
  (:require [clojure.string :as str]))

(def interviews (atom {:123 {:id "123"
                             :title "Pizza Interview"
                             :questions ["Do you like pizza?"
                                         "What are your favorite toppings?"
                                         "Do you like little caesars?"]}
                       :321 {:id "321"
                             :title "Fruit Interview"
                             :questions ["Do you like fruit?"
                                         "What are your favorite fruits?"
                                         "Do you like bananas?"]}}))

(defn get-interviews
  []
  @interviews)

(defn add-interview
  [title questions]
  (let [new-interview-id (str (rand-int 1000000))]
    (swap! interviews
           (fn [interview-state]
             (assoc interview-state
                    (keyword new-interview-id)
                    {:id new-interview-id
                     :title title
                     :questions questions})))
    new-interview-id))

(defn update-interview
  [interview]
  (swap! interviews
         (fn [interview-state]
           (assoc interview-state (keyword (:id interview)) interview)))
  interview)

(defn delete-interview
  [interview-id]
  (swap! interviews
         (fn [interview-state]
           (dissoc interview-state (keyword interview-id)))))


