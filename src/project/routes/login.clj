(ns project.routes.login
  (:require [compojure.core :refer :all]))

;; Add routes
(defroutes login-routes
  (GET "/login" [] "Hey there, welcome to the login page!"))

