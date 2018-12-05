xquery version "1.0-ml";

declare option xdmp:transaction-mode "query";

xdmp:spawn-function(function() {
xdmp:document-insert("/test.xml", element random {xdmp:random()}, <options xmlns="xdmp:document-insert">
  <collections>
    <collection>collection-one</collection>
    <collection>collection-two</collection>
    <collection>collection-three</collection>
    <collection>collection-four</collection>
  </collections>
</options>)
},
<options xmlns="xdmp:eval">
  <update>auto</update>
  <commit>auto</commit>
</options>)