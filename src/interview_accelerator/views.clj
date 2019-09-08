(ns interview-accelerator.views
  (:require [clojure.string :as str]
            [hiccup.page :as page]
            [interview-accelerator.interviews-controller :as interviews]
            [interview-accelerator.users-controller :as users]
            [interview-accelerator.login-controller :as login]
            [interview-accelerator.paths :as paths]
            [ring.util.response :as response]
            [ring.util.anti-forgery :as util]))

(def max-questions 100)
(def base-styles "/css/styles.css")
(def edit-interview-js "/js/edit_interview_page.js")

(defn home-page
  []
  (page/html5
   [:h1 "Welcome to the Interview Accelerator"]
   [:p [:a {:href (paths/get-create-interview-path)} "add new interview"]]
   [:p [:a {:href (paths/get-interviews-base-path)} "view interviews"]]
   (page/include-css base-styles)))

(defn login-page
  [params]
  (page/html5
   [:h1 "Please log in"]
   [:form {:action (paths/get-login-path) :method "POST"}
    (util/anti-forgery-field)
    (cond (:previous-failed params)
          [:p "Incorrect username or password"])
    [:p
     [:label {:for "username"} "username"]
     [:input {:type "text" :name "username"}]]
    [:p
     [:label {:for "password"} "password"]
     [:input {:type "password" :name "password"}]]
    [:p [:input {:type "submit" :value "login"}]]]
   (page/include-css base-styles)))

(defn login-results-page
  [login-form session]
  (if-let [authenticated-session
           (login/get-session-with-identity login-form session)]
    (assoc (response/redirect (paths/get-interviews-base-path))
           :session authenticated-session)
    (response/redirect (paths/get-login-path "previous-failed"))))

(defn edit-question-input
  [cur-id should-display existing-question]
  [:div {:id (str "new-question-" cur-id)
         :style (if should-display "" "display: none;")}
   [:p (str "Question " (inc cur-id))
    [:p [:textarea {:name (str "question-" cur-id)} existing-question]]]
   (cond (< cur-id (- max-questions 1))
         [:p [:button {:id (str "add-question-" cur-id) :type "button"}
              "add question"]])])

(defn update-question-input
  [cur-id existing-question]
  (edit-question-input cur-id true existing-question))

(defn add-interview-page
  []
  (page/html5
   [:h1 "Create Interview"]
   [:form {:action (paths/get-create-interview-path) :method "POST"}
    (util/anti-forgery-field)
    [:p "Interview Title"
     [:input {:type "text" :name "interview-title"}]]
    (edit-question-input 0 true "")
    (map #(edit-question-input % false "") (range 1 max-questions))
    [:p [:input {:type "submit" :value "create interview"}]]]
   (page/include-js edit-interview-js)
   (page/include-css base-styles)))

(defn update-interview-page
  [interview-id user]
  (let [interview (interviews/get-interview interview-id user)]
    (page/html5
     [:p [:a {:href (paths/get-interviews-base-path)} "Back to interviews"]]
     [:h1 "Edit Interview"]
     [:form {:action (paths/get-update-interview-path interview-id)
             :method "POST"}
      (util/anti-forgery-field)
      [:p "Interview Title"
       [:input {:type "text"
                :name "interview-title"
                :value (:title interview)}]]
      (map-indexed (fn [idx question]
                     (update-question-input idx question))
                   (:questions interview))
      (map #(edit-question-input % false "")
           (range (count (:questions interview)) max-questions))
      [:p [:input {:type "submit" :value "update interview"}]]]
     (page/include-js edit-interview-js)
     (page/include-css base-styles))))

(defn get-update-and-delete-links
  [interview]
  [:span
   [:a {:href (paths/get-update-interview-path (:id interview))}
    "update"]
   [:a {:href (paths/get-delete-interview-path (:id interview))}
    "delete"]])

(defn display-interview-links
  [interview]
  [:li
   [:a {:href (paths/get-interview-base-path (:id interview))}
    (:title interview)]
   (get-update-and-delete-links interview)])

(defn get-interviews-page
  [user]
  (page/html5
   [:h1 "Interviews"]
   [:p [:a {:href (paths/get-create-interview-path)} "add new interview"]]
   [:ul
    (map #(display-interview-links %) (interviews/get-interviews user))]
   (page/include-css base-styles)))

(defn display-interview
  [interview]
  (page/html5
   [:p [:a {:href (paths/get-interviews-base-path)} "Back to interviews"]]
   [:h1 (str "Interview: " (:title interview))]
   (get-update-and-delete-links interview)
   [:h3 "Questions:"]
   [:ul
    (map (fn [question]
           [:li question])
         (:questions interview))]
   (page/include-css base-styles)))

(defn display-confirm-delete
  [interview]
  (page/html5
   [:p [:a {:href (paths/get-interviews-base-path)} "Back to interviews"]]
   [:h1 (str "Confirm delete " (:title interview))]
   [:form {:action (paths/get-delete-interview-path (:id interview))
           :method "POST"}
    (util/anti-forgery-field)
    [:input {:type "submit" :value "delete"}]]
   (page/include-css base-styles)))

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
  [interview-form user]
  (display-interview (interviews/create-interview
                      (get-interview-from-form interview-form) user)))

(defn update-interview-results-page
  [interview-form user]
  (display-interview (interviews/update-interview
                      (get-interview-from-form interview-form) user)))

(defn get-interview-page
  [interview-id user]
  (display-interview (interviews/get-interview interview-id user)))

(defn delete-interview-confirmation-page
  [interview-id user]
  (display-confirm-delete (interviews/get-interview interview-id user)))

(defn delete-interview-results-page
  [interview-id user]
  (interviews/delete-interview interview-id user)
  (response/redirect (paths/get-interviews-base-path)))

