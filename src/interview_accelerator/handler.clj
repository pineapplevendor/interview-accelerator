(ns interview-accelerator.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [interview-accelerator.views :as views]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/add-interview" 
       [] 
       (views/add-interview-page))
  (POST "/add-interview" 
        {params :params}
        (views/add-interview-results-page params))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
