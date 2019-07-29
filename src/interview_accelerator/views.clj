(ns interview-accelerator.views
  (:require [clojure.string :as str]
            [hiccup.page :as page]
            [interview-accelerator.controllers :as controllers]
            [ring.util.anti-forgery :as util]))

(defn home-page
  []
  (page/html5
    [:h1 "Welcome to the Interview Accelerator"]
    [:p [:a {:href "/add-interview"} "add new interview"]]
    [:p [:a {:href "/interviews"} "view interviews"]]
    (page/include-css "/css/styles.css")))

(defn create-new-question-input
  [cur-id has-next-question should-display]
  [:div {:id (str "new-question-" cur-id)
         :style (if should-display "" "display: none;")}
   [:p (str "Question " (inc cur-id))
    [:p [:textarea {:name (str "question-" cur-id)}]]]
   (if has-next-question
     [:p [:button {:id (str "add-question-" cur-id) :type "button"}
          "add question"]])])

(defn add-interview-page
  []
  (page/html5
   [:h1 "Create an Interview"]
   [:form {:action "/add-interview" :method "POST"}
    (util/anti-forgery-field)
    [:p "Interview Title"
      [:input {:type "text" :name "interview-title"}]]
    (create-new-question-input 0 true true)
    (map #(create-new-question-input % true false) (range 1 100))
    (create-new-question-input 100 false false)
    [:p [:input {:type "submit" :value "create interview"}]]]
   (page/include-js "/js/add_interview_page.js")
   (page/include-css "/css/styles.css")))

(defn display-interview-question
  [question]
  [:p question])

(defn display-interview-link
  [interview-info]
  [:p [:a {:href (:path interview-info)} (:title interview-info)]])

(defn get-interviews-page
  []
  (page/html5
   [:h1 "Interviews"]
   (map #(display-interview-link %) (controllers/get-interviews)) 
   (page/include-css "/css/styles.css")))

(defn display-interview
  [interview]
  (page/html5
   [:h1 (str "Interview: " (:title interview))]
   (map #(display-interview-question %) (:questions interview))
   (page/include-css "/css/styles.css")))

(defn add-interview-results-page
  [form-data]
  (display-interview (controllers/create-interview form-data)))

(defn get-interview-page
  [interview-id]
  (display-interview (controllers/get-interview interview-id)))

  

