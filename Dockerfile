FROM tomcat:9.0
RUN rm -rf /usr/local/tomcat/webapps
COPY ./target/viewit /usr/local/tomcat/webapps/ROOT