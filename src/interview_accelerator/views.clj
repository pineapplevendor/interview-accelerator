(ns interview-accelerator.views
  (:require [clojure.string :as str]
            [hiccup.page :as page]
            [interview-accelerator.controllers :as controllers]
            [interview-accelerator.paths :as paths]
            [ring.util.response :as response]
            [ring.util.anti-forgery :as util]))

(def max-questions 100)

(defn home-page
  []
  (page/html5
   [:h1 "Welcome to the Interview Accelerator"]
   [:p [:a {:href (paths/get-create-interview-path)} "add new interview"]]
   [:p [:a {:href (paths/get-interviews-base-path)} "view interviews"]]
   (page/include-css "/css/styles.css")))

(defn edit-question-input
  [cur-id has-next-question should-display existing-question]
  [:div {:id (str "new-question-" cur-id)
         :style (if should-display "" "display: none;")}
   [:p (str "Question " (inc cur-id))
    [:p [:textarea {:name (str "question-" cur-id)} existing-question]]]
   (if has-next-question
     [:p [:button {:id (str "add-question-" cur-id) :type "button"}
          "add question"]])])

(defn update-question-input
  [cur-id has-next-question existing-question]
  (edit-question-input cur-id has-next-question true existing-question))

(defn add-interview-page
  []
  (page/html5
   [:h1 "Create Interview"]
   [:form {:action (paths/get-create-interview-path) :method "POST"}
    (util/anti-forgery-field)
    [:p "Interview Title"
     [:input {:type "text" :name "interview-title"}]]
    (edit-question-input 0 true true "")
    (map #(edit-question-input % true false "") (range 1 max-questions))
    (edit-question-input max-questions false false "")
    [:p [:input {:type "submit" :value "create interview"}]]]
   (page/include-js "/js/add_interview_page.js")
   (page/include-css "/css/styles.css")))

(defn update-interview-page
  [interview-id]
  (let [interview (controllers/get-interview interview-id)]
    (page/html5
     [:h1 "Edit Interview"]
     [:form {:action (paths/get-update-interview-path interview-id)
             :method "POST"}
      (util/anti-forgery-field)
      [:p "Interview Title"
       [:input {:type "text"
                :name "interview-title"
                :value (:title interview)}]]
      (map-indexed (fn [idx question]
                     (update-question-input idx true question))
                   (:questions interview))
      (map #(edit-question-input % true false "")
           (range (count (:questions interview)) max-questions))
      (edit-question-input max-questions false false "")
      [:p [:input {:type "submit" :value "update interview"}]]]
     (page/include-js "/js/add_interview_page.js")
     (page/include-css "/css/styles.css"))))

(defn display-interview-question
  [question]
  [:p question])

(defn display-interview-links
  [interview]
  [:p
   [:a {:href (paths/get-interview-base-path (:id interview))}
    (:title interview)]
   [:ul
    [:li
     [:form {:action (paths/get-update-interview-path (:id interview))
             :method "GET"}
      [:input {:type "submit" :value "update"}]]]
    [:li
     [:form {:action (paths/get-delete-interview-path (:id interview))
             :method "POST"}
      (util/anti-forgery-field)
      [:input {:type "submit" :value "delete"}]]]]])

(defn get-interviews-page
  []
  (page/html5
   [:h1 "Interviews"]
   (map #(display-interview-links %) (controllers/get-interviews))
   (page/include-css "/css/styles.css")))

(defn display-interview
  [interview]
  (page/html5
   [:h1 (str "Interview: " (:title interview))]
   (map #(display-interview-question %) (:questions interview))
   (page/include-css "/css/styles.css")))

(defn get-interview-title
  [interview-form]
  (:interview-title interview-form))

(defn get-question-number
  [question]
  (read-string
   (apply str (filter #(Character/isDigit %) (name (first question))))))

(defn get-valid-questions
  [interview-form]
  (sort-by get-question-number
           (filter #(not (empty? (second %)))
                   (select-keys
                    interview-form
                    (map #(keyword (str "question-" %)) (range 101))))))

(defn get-interview-from-form
  [interview-form]
  {:id (:interview-id interview-form)
   :title (get-interview-title interview-form)
   :questions (get-valid-questions interview-form)})

(defn add-interview-results-page
  [interview-form]
  (display-interview (controllers/create-interview
                      (get-interview-from-form interview-form))))

(defn update-interview-results-page
  [interview-form]
  (display-interview (controllers/update-interview
                      (get-interview-from-form interview-form))))

(defn get-interview-page
  [interview-id]
  (display-interview (controllers/get-interview interview-id)))

(defn delete-interview-results-page
  [interview-id]
  (controllers/delete-interview interview-id)
  (response/redirect (paths/get-interviews-base-path)))

