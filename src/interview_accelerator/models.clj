(ns interview-accelerator.models
  (:require [schema.core :as s]))

(s/defschema Interview
  {:id s/Str
   :title s/Str
   :questions [s/Str]})

(s/defschema CreateInterviewInput
  {:title s/Str
   :questions [s/Str]})

