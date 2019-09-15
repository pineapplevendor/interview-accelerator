(ns interview-accelerator.database-facade
  (:require [clojure.string :as str]
            [buddy.hashers :as hashers]))

(defn get-uuid
  []
  (str (java.util.UUID/randomUUID)))

(def users (atom {:456 {:id "456"
                        :username "pineapple"
                        :password-hash (hashers/encrypt "p-h-1")}
                  :654 {:id "654"
                        :username "vendor"
                        :password-hash (hashers/encrypt "p-h-2")}}))

(defn add-user
  [username password]
  (let [user-id (get-uuid)]
    (swap! users
           (fn [users-state]
             (assoc users-state
                    (keyword user-id)
                    {:id user-id
                     :username username
                     :password-hash (hashers/encrypt password)})))))

(defn get-user-by-id
  [user-id]
  (first (filter (fn [user]
                   (= (:id user) user-id))
                   (vals @users))))

(defn get-user
  [username password]
  (first (filter (fn [user]
                   (and (= (:username user) username)
                        (hashers/check password (:password-hash user))))
                 (vals @users))))

(defn user-exists?
  [username]
  (first (filter (fn [user]
                   (= (:username user) username))
                 (vals @users))))

(def interviews (atom {:123 {:id "123"
                             :interviewer-id "456"
                             :title "Pizza Interview"
                             :questions ["Do you like pizza?"
                                         "What are your favorite toppings?"
                                         "Do you like little caesars?"]}
                       :321 {:id "321"
                             :interviewer-id "456"
                             :title "Fruit Interview"
                             :questions ["Do you like fruit?"
                                         "What are your favorite fruits?"
                                         "Do you like bananas?"]}}))

(defn get-interviews
  []
  @interviews)

(defn add-interview
  [title questions interviewer-id]
  (let [new-interview-id (get-uuid)]
    (swap! interviews
           (fn [interview-state]
             (assoc interview-state
                    (keyword new-interview-id)
                    {:id new-interview-id
                     :interviewer-id interviewer-id
                     :title title
                     :questions questions})))
    new-interview-id))

(defn update-interview
  [interview]
  (swap! interviews
         (fn [interview-state]
           (assoc interview-state (keyword (:id interview)) interview)))
  interview)

(defn delete-interview
  [interview-id]
  (swap! interviews
         (fn [interview-state]
           (dissoc interview-state (keyword interview-id)))))


