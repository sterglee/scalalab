java --add-modules ALL-DEFAULT  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops   -XX:+DoEscapeAnalysis  -Djava.library.path=.:./lib:./libCUDA -Xss5m -Xms4000m -Xmx23500m -jar ScalaLab.jar   &
