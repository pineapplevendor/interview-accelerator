(ns interview-accelerator.users-controller
  (:require [clojure.string :as str]
            [interview-accelerator.database-facade :as db-facade]))

(defn get-user
  [username password]
  (db-facade/get-user username password))

(defn create-user
  [username password]
  (db-facade/add-user username password))

(defn is-username-taken?
  [username]
  (db-facade/user-exists? username))

(defn is-valid-password?
  [password]
  (>= (count password) 12))


