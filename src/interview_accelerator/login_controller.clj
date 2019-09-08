(ns interview-accelerator.login-controller
  (:require [interview-accelerator.users-controller :as users]))

(defn get-session-with-identity
 [login-form session]
 (if-let [user (users/get-user (:username login-form) (:password login-form))]
   (assoc session :identity (:id user))
   nil))

