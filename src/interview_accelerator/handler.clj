(ns interview-accelerator.handler
  (:require [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [interview-accelerator.models :as models]
            [interview-accelerator.controllers :as controllers]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"}}

    (context "/api" []

      (GET "/interviews/:interview-id" []
        :return {:interview models/Interview}
        :path-params [interview-id]
        :summary "Retrieves an interview"
        (ok {:interview (controllers/get-interview interview-id)}))

      (GET "/interviews" []
        :return {:interviews [models/Interview]}
        :summary "Retrieve interviews"
        (ok {:interviews (controllers/get-interviews)}))

      (DELETE "/interviews/:interview-id" []
        :return {:interviewId s/Str}
        :summary "Delete an interview"
        :path-params [interview-id]
        (ok {:interviewId (controllers/delete-interview interview-id)}))
      
      (POST "/interviews" []
        :return {:interview models/Interview}
        :summary "Create an interview"
        :body [interview models/CreateInterviewInput]
        (ok {:interview (controllers/create-interview interview)}))
      
      (PUT "/interviews/:interview-id" []
        :return {:interview models/Interview}
        :summary "Update an interview"
        :path-params [interview-id]
        :body [interview models/Interview]
        (ok {:interview (controllers/update-interview 
                          interview-id 
                          interview)})))))
