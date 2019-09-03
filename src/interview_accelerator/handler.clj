(ns interview-accelerator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [interview-accelerator.views :as views]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/"
    []
    (views/home-page))
  (GET "/interviews/create"
    []
    (views/add-interview-page))
  (POST "/interviews/create"
    {params :params}
    (views/add-interview-results-page params))
  (GET "/interviews"
    []
    (views/get-interviews-page))
  (GET "/interviews/:interview-id"
    [interview-id]
    (views/get-interview-page interview-id))
  (POST "/interviews/:interview-id/delete"
    [interview-id]
    (views/delete-interview-results-page interview-id))
  (GET "/interviews/:interview-id/update"
    [interview-id]
    (views/update-interview-page interview-id))
  (POST "/interviews/:interview-id/update"
    {params :params}
    (views/update-interview-results-page params))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
