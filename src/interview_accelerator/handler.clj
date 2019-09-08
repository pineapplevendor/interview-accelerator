(ns interview-accelerator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer 
             [wrap-authentication wrap-authorization]]
            [interview-accelerator.views :as views]
            [interview-accelerator.users-controller :as users]
            [interview-accelerator.paths :as paths]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/"
    []
    (views/home-page))

  (GET (paths/get-login-path)
    {params :params}
    (views/login-page params))

  (POST (paths/get-login-path)
    {params :params session :session}
    (views/login-results-page params session))

  (GET (paths/get-create-interview-path)
    []
    (views/add-interview-page))

  (POST (paths/get-create-interview-path)
    {params :params user :user}
    (views/add-interview-results-page params user))

  (GET (paths/get-interviews-base-path)
    {user :user}
    (views/get-interviews-page user))

  (GET (paths/get-interview-base-path paths/interview-id-path-param)
    {params :params user :user}
    (views/get-interview-page (:interview-id params) user))

  (GET (paths/get-delete-interview-path paths/interview-id-path-param)
    {params :params user :user}
    (views/delete-interview-confirmation-page (:interview-id params) user))

  (POST (paths/get-delete-interview-path paths/interview-id-path-param)
    {params :params user :user}
    (views/delete-interview-results-page (:interview-id params) user))

  (GET (paths/get-update-interview-path paths/interview-id-path-param)
    {params :params user :user}
    (views/update-interview-page (:interview-id params) user))

  (POST (paths/get-update-interview-path paths/interview-id-path-param)
    {params :params user :user}
    (views/update-interview-results-page params user))

  (route/not-found "Not Found"))

(defn wrap-user
  [handler]
  (fn [{user-id :identity :as request}]
    (handler (assoc request :user (users/get-user-by-id user-id)))))

(def backend (session-backend))

(def app
  (-> #'app-routes
      (wrap-user)
      (wrap-authentication backend)
      (wrap-authorization backend)
      (wrap-defaults site-defaults)))
