(ns interview-accelerator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [interview-accelerator.views :as views]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/"
    []
    (views/home-page))
  (GET "/add-interview"
    []
    (views/add-interview-page))
  (POST "/add-interview"
    {params :params}
    (views/add-interview-results-page params))
  (GET "/interviews"
    []
    (views/get-interviews-page))
  (GET "/interviews/:interview-id"
    [interview-id]
    (views/get-interview-page interview-id))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
