(defproject nylon "0.0.1"
  :description "An instaparse-based Textile parser"
  :url "http://github.com/rcarmo/clj-nylon"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [instaparse "1.3.2"]]
  :scm {:name "git"
        :url "http://github.com/rcarmo/clj-nylon"}
  :main nylon.core)
