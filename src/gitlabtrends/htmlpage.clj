(ns gitlabtrends.htmlpage
  (:require [hiccup.page :refer [html5 include-css]]))

(defn get-graph
  []
  (html5
   [:svg {:width "960", :height "500"}]
   [:script {:src "https://d3js.org/d3.v4.min.js"}]
   [:script "var svg = d3.select(\"svg\"),\n    margin = {top: 20, right: 20, bottom: 30, left: 80},\n    width = +svg.attr(\"width\") - margin.left - margin.right,\n    height = +svg.attr(\"height\") - margin.top - margin.bottom;\n  \nvar tooltip = d3.select(\"body\").append(\"div\").attr(\"class\", \"toolTip\");\n  \nvar x = d3.scaleLinear().range(
 [0, width]);\nvar y = d3.scaleBand().range(
 [height, 0]);\n\nvar g = svg.append(\"g\")\n\t\t.attr(\"transform\", \"translate(\" + margin.left + \",\" + margin.top + \")\");\n  \nd3.json(\"data.json\", function(error, data) {\n  \tif (error) throw error;\n  \n  \tdata.sort(function(a, b) { return a.value - b.value; });\n  \n  \tx.domain(
 [0, d3.max(data, function(d) { return d.value; })]);\n    y.domain(data.map(function(d) { return d.area; })).padding(0.1);\n\n    g.append(\"g\")\n        .attr(\"class\", \"x axis\")\n       \t.attr(\"transform\", \"translate(0,\" + height + \")\")\n      \t.call(d3.axisBottom(x).ticks(5).tickFormat(function(d) { return parseInt(d / 1000); }).tickSizeInner(
 [-height]));\n\n    g.append(\"g\")\n        .attr(\"class\", \"y axis\")\n        .call(d3.axisLeft(y));\n\n    g.selectAll(\".bar\")\n        .data(data)\n      .enter().append(\"rect\")\n        .attr(\"class\", \"bar\")\n        .attr(\"x\", 0)\n        .attr(\"height\", y.bandwidth())\n        .attr(\"y\", function(d) { return y(d.area); })\n        .attr(\"width\", function(d) { return x(d.value); })\n        .on(\"mousemove\", function(d){\n            tooltip\n              .style(\"left\", d3.event.pageX - 50 + \"px\")\n              .style(\"top\", d3.event.pageY - 70 + \"px\")\n              .style(\"display\", \"inline-block\")\n              .html((d.area) + \"<br>\" + \"£\" + (d.value));\n        })\n    \t\t.on(\"mouseout\", function(d){ tooltip.style(\"display\", \"none\");});\n});"]))
