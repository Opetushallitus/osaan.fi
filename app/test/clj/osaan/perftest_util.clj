(ns osaan.perftest-util
  (:require
    [cheshire.core :refer :all]
    [org.httpkit.client :as http]))

(defn async-http-requ [url basic-auth user-id context callback]
  (let [check-status (fn [{:keys [status]}] (callback (= 200 status)))]
    (http/get url {:basic-auth basic-auth}
              check-status)))

(defn async-http-json-requ [url basic-auth body user-id context callback]
  (let [check-status (fn [{:keys [status]}] (callback (= 200 status)))
        header-map (merge (:headers {:basic-auth basic-auth})
                          {"Content-Type" "application/json;charset=UTF-8"})]
    (http/post url {:headers header-map
                    :basic-auth basic-auth
                    :body body}
               check-status)))

(defn url->http-get-fn [basic-auth url name]
  (let [requ-fn (partial async-http-requ url basic-auth)
        requ {:name name :fn requ-fn}]
    requ))

(defn url->http-post-fn [basic-auth url name json]
  (let [requ-fn (partial async-http-json-requ url basic-auth json)
        requ {:name name :fn requ-fn}]
    requ))
