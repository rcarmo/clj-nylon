(ns nylon.core
  (:use [hiccup.core])
  (:require [hiccup.page :as page]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [instaparse.core :as insta]))

(def parse-textile
  (insta/parser
    "<Blocks> = Blankline* (Oneliner | Paragraph | Header | List | Ordered | Code | Rule)*
    Header = Line Headerline Blankline+
    <Headerline> = h1 | h2
    h1 = '='+
    h2 = '-'+
    List = Listline+ Blankline+
    Listline = Listmarker Whitespace+ Word (Whitespace Word)* EOL
    <Listmarker> = <'+' | '*' | '-'>
    Ordered = Orderedline+ Blankline+
    Orderedline = Orderedmarker Whitespace* Word (Whitespace Word)* EOL
    <Orderedmarker> = <#'[0-9]+\\.'>
    Code = Codeline+ Blankline+
    Codeline = <Space Space Space Space> (Whitespace | Word)* EOL
    Rule = Ruleline Blankline+
    <Ruleline> = <'+'+ | '*'+ | '-'+>
    Paragraph = Line+ Blankline+
    <Blankline> = Whitespace* EOL
    <Line> = Whitespace* Word* Whitespace* EOL
    <Whitespace> = (Space | Tab)+
    <Space> = ' '
    <Tab> = <'\\t'>
    <Word> = #'\\S+'
    Oneliner = Line
    <EOL> = <'\\r'> | <'\\n'> | <'\\r\\n'>"))

(def span-elems
  [[#"@(\S+)@"             (fn [s] [:code s])]
   [#"\*\*(\S+)\*\*"       (fn [s] [:b s])]
   [#"__(\S+)__"           (fn [s] [:i s])]
   [#"\*(\S+)\*"           (fn [s] [:strong s])]
   [#"_(\S+)_"             (fn [s] [:em s])]])

(defn- parse-span [s]
  (let [res (first (filter (complement nil?)
                           (for [[regex func] span-elems]
                             (let [groups (re-matches regex s)]
                               (if groups (func (drop 1 groups)))))))]
    (if (nil? res) s res)))

(defn- output-hiccup [blocks]
  (for [b blocks]
    (case (first b)
      :List      [:ul (for [li (drop 1 b)] [:li (map parse-span (drop 1 li))])]
      :Ordered   [:ol (for [li (drop 1 b)] [:li (map parse-span (drop 1 li))])]
      :Header    [(first (last b)) (apply str (map parse-span (take (- (count b) 2) (drop 1 b))))]
      :Code      [:pre [:code (interpose "<br />" (for [line (drop 1 b)] (drop 1 line)))]]
      :Rule      [:hr]
      :Paragraph [:p (map parse-span (drop 1 b))]
      :Oneliner  [:p (map parse-span (drop 1 b))])))

(def textile->hiccup  (comp output-hiccup parse-textile))

(defn textile [buffer] (html (textile->hiccup (str buffer "\n"))))

(defn -main [path & args]
  (println "Nothing here"))
