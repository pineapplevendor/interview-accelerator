(ns interview-accelerator.paths
  (:require [clojure.string :as str]))

(def interview-id-path-param ":interview-id")

(defn get-interviews-base-path
  []
  "/interviews")

(defn get-login-path
  ([]
   "/login")
  ([login-error]
   (str (get-login-path) "?" login-error "=true")))

(defn get-create-interview-path
  []
  (str (get-interviews-base-path) "/create"))

(defn get-interview-base-path
  [interview-id]
  (str (get-interviews-base-path) "/" interview-id))

(defn get-update-interview-path
  [interview-id]
  (str (get-interview-base-path interview-id) "/update"))

(defn get-delete-interview-path
  [interview-id]
  (str (get-interview-base-path interview-id) "/delete"))


