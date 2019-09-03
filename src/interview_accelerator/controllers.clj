(ns interview-accelerator.controllers
  (:require [clojure.string :as str]
            [interview-accelerator.database-facade :as db-facade]))

(defn get-interviews
  []
  (vals (db-facade/get-interviews)))

(defn get-interview
  [interview-id]
  (get (db-facade/get-interviews) (keyword interview-id)))

(defn create-interview
  [interview]
  (get-interview (db-facade/add-interview
                  (:title interview)
                  (:questions interview))))

(defn update-interview
  [interview]
  (db-facade/update-interview interview))

(defn delete-interview
  [interview-id]
  (db-facade/delete-interview interview-id)
  interview-id)
