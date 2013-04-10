myApp-bare
==========
test

Scheletro per web application che fornisce funzionalità di login e gestione utenti (beta)
il database è attualmente gestito in RAM tramite H2 (http://www.h2database.com/html/main.html)
volendo è possibile configurare l'accesso a un altro DB (mySQL ad esempio)
configurando su JBoss una datasource (http://www.mastertheboss.com/jboss-datasource/how-to-configure-a-datasource-with-jboss-7)
i log al momento sono reindirizzati di default sul log di Jboss, volendo posso essere spostati modificando java/resources/logback.xml

=== HOW TO ==

scaricare JBoss AS 7.1.1 da http://www.jboss.org/jbossas/downloads/
installarlo
    - scompattare lo zip/tar.gz
    - settare la variabile di ambiente JBOSS_HOME per puntare alla dir dove si è scompattato Jboss
    - ATTENZIONE : meglio non metterlo in un path che contiene spazi, io consiglio direttamente sotto c:\

scaricare Maven 3.x : http://maven.apache.org/download.cgi
installarlo e configurarlo (soprattutto se siete dietro un proxy) : http://maven.apache.org/run-maven/index.html#Quick_Start

da riga di comando DOS andare sotto la dir di progetto che contiene questo file e verificare la presenza
del file pom.xml (chiamiamo questa dir PRJ_ROOT)

eseguire il comando : mvn clean package

Maven esegue una lunga (molto lunga) serie di download e dovrebbe terminare con un BUILD SUCCESSFUL

sotto PRJ_ROOT/target dovrebbe essere stato creato un file myApp.war

startare JBoss
    - JBOSS_HOME/bin/standalone.bat

da PRJ_ROOT eseguire : mvn jboss-as:deploy

(altra serie di download...)

nella finestra di JBoss si dovrebbe vedere "myApp deployed"

andare all'URL : localhost:8080/myApp

se tutto ha funzionato correttamente l'applicazione dovrebbe essere online, le credenziali per loggarsi
sono admin/admin






