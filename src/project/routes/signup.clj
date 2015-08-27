(ns project.routes.signup
  (:require [compojure.core :refer :all])
  (:require [selmer.parser :refer [render-file]])
  (:require [ring.util.response :as response]))

;; Define signup layout page
(defn signup-page []
  (render-file "signup.html" {}))

;; Handle submit button click
(defn signup-page-submit [user]
  #_(let [errors (signup/validate-signup user)]
  (if (empty? errors)
      (response/redirect "/signup-success")
      (layout/render "signup.html" (assoc user :errors errors)))))

;; Add routes
(defroutes signup-routes
  (GET "/signup" [] (signup-page))
  (POST "/signup" [& form] (str "nice job"))
  (GET "/signup-success" [] "Success!"))
