(ns interview-accelerator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [interview-accelerator.views :as views]
            [interview-accelerator.paths :as paths]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/"
    []
    (views/home-page))

  (GET (paths/get-create-interview-path)
    []
    (views/add-interview-page))

  (POST (paths/get-create-interview-path)
    {params :params}
    (views/add-interview-results-page params))

  (GET (paths/get-interviews-base-path)
    []
    (views/get-interviews-page))

  (GET (paths/get-interview-base-path paths/interview-id-path-param)
    [interview-id]
    (views/get-interview-page interview-id))

  (POST (paths/get-delete-interview-path paths/interview-id-path-param)
    [interview-id]
    (views/delete-interview-results-page interview-id))

  (GET (paths/get-update-interview-path paths/interview-id-path-param)
    [interview-id]
    (views/update-interview-page interview-id))

  (POST (paths/get-update-interview-path paths/interview-id-path-param)
    {params :params}
    (views/update-interview-results-page params))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
