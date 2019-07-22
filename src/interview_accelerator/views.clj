(ns interview-accelerator.views
  (:require [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]))

(defn create-new-question-input
  [cur-id has-next-question should-display]
  [:div {:id (str "new-question-" cur-id) 
         :style (if should-display "" "display: none;")}
   [:p "Question" 
    [:p [:textarea {:name (str "question-" cur-id)}]]]
    (if has-next-question 
      [:p [:button {:id (str "add-question-" cur-id) :type "button"} 
           "add question"]])])

(defn add-interview-page 
  []
  (page/html5
   [:form {:action "/add-interview" :method "POST"}
    (util/anti-forgery-field)
    (create-new-question-input 0 true true)
    (map #(create-new-question-input % true false) (range 1 100))
    (create-new-question-input 100 false false)
    [:p [:input {:type "submit" :value "create interview"}]]]
   (page/include-js "/js/add_interview_page.js")))

(defn get-questions
  [form-data]
  (filter #(not (empty? (second %)))
    (select-keys 
      form-data
      (map #(keyword (str "question-" %)) (range 101)))))

(defn add-interview-results-page
  [form-data]
  (page/html5
   [:h1 "Added an Interview"]
   (println (get-questions form-data))
   [:p (get-questions form-data)]))
    
