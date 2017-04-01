FROM tomcat

LABEL maintainer "talalalrawajfeh@gmail.com"

EXPOSE 8080

COPY sales-manager-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
RUN chmod a+rwx $CATALINA_HOME/webapps/sales-manager-1.0-SNAPSHOT.war

COPY run.sh /
RUN chmod a+x /run.sh

ENTRYPOINT ["/run.sh"]
