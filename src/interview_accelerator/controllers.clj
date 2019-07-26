(ns interview-accelerator.controllers
  (:require [clojure.string :as str]))

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
