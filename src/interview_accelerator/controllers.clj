(ns interview-accelerator.controllers
  (:require [clojure.string :as str]
            [interview-accelerator.database-facade :as db-facade]))

(defn get-interviews
  []
  (let [interviews (db-facade/get-interviews)]
    (map (fn [interview-id]
           {:id (name interview-id)
            :path (str "/interviews/" (name interview-id))
            :title (:title (interview-id interviews))})
         (keys interviews))))

(defn get-interview
  [interview-id]
  (get (db-facade/get-interviews) (keyword interview-id)))

(defn get-interview-title
  [add-interviews-form]
  (:interview-title add-interviews-form))
  
(defn get-question-number
  [question]
  (read-string
   (apply str (filter #(Character/isDigit %) (name (first question))))))

(defn get-valid-questions
  [add-interviews-form]
  (sort-by get-question-number
           (filter #(not (empty? (second %)))
                   (select-keys
                    add-interviews-form
                    (map #(keyword (str "question-" %)) (range 101))))))

(defn create-interview
  [add-interviews-form]
  (get-interview (db-facade/add-interview 
                   (get-interview-title add-interviews-form)
                   (get-valid-questions add-interviews-form))))

(defn delete-interview
  [interview-id]
  (db-facade/delete-interview interview-id))
