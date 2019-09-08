(ns interview-accelerator.interviews-controller
  (:require [clojure.string :as str]
            [interview-accelerator.database-facade :as db-facade]))

(defn get-interviews
  [user]
  (filter (fn [interview]
            (= (:interviewer-id interview) (:id user)))
            (vals (db-facade/get-interviews))))

(defn get-interview
  [interview-id user]
  ((keyword interview-id) (db-facade/get-interviews)))

(defn create-interview
  [interview user]
  (get-interview (db-facade/add-interview
                  (:title interview)
                  (:questions interview)
                  (:id user))
                 user))

(defn update-interview
  [interview user]
  (db-facade/update-interview (assoc interview :interviewer-id (:id user))))

(defn delete-interview
  [interview-id user]
  (db-facade/delete-interview interview-id)
  interview-id)
