(ns nylon.core-test
  (:use clojure.test
        [nylon.core :only [textile]]))

(deftest empty-input
  (is (= "" (textile "")))
  (is (= "" (textile "\n\n\n")))
  (is (= "" (textile "\r\n\r\n")))
  (is (= "" (textile "\t\t\t"))))

(deftest basic-input
  (is (= "<p>a</p>" (textile "a")))
  (is (= "<p>a</p><p>b</p>" (textile "a\nb")))
  (is (= "<p>a</p><p>b</p>" (textile "a\n\nb"))))

(deftest basic-emphasis
  (is (= "<p><strong>a</strong></p>" (textile "*a*")))
  (is (= "<p><em>a</em></p>" (textile "_a_")))
  (is (= "<p><i>a</i></p>" (textile "__a__")))
  (is (= "<p><b>a</b></p>" (textile "**a**"))))

(deftest inline-emphasis
  (is (= "<p>a<strong>b</strong>c</p>" (textile "a*b*c"))))
