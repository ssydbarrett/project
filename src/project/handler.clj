(ns project.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [project.layout :refer [error-page]]
            [project.routes.home :refer [home-routes]]
            [project.routes.login :refer [login-routes]]
            [project.routes.signup :refer [signup-routes]]
            [project.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]

            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     (if (env :dev) :trace :info)
     :appenders {:rotor (rotor/rotor-appender
                          {:path "logs/project.log"
                           :max-size (* 512 1024)
                           :backlog 10})}})

  (if (env :dev) (parser/cache-off!))
  (timbre/info (str
                 "\n-=[project started successfully"
                 (when (env :dev) " using the development profile")
                 "]=-")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "project is shutting down...")
  (timbre/info "shutdown complete!"))

;; Add all routes to app routes
(def app-routes
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (wrap-routes #'login-routes middleware/wrap-csrf)
    (wrap-routes #'signup-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
