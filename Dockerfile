FROM centos

MAINTAINER ssgribanov@gmail.com

RUN yum install -y java-11-openjdk-devel
RUN yum install -y which

# Install Maven
ENV MAVEN_MAJOR 3
ENV MAVEN_VERSION 3.3.1
RUN curl -sSL http://archive.apache.org/dist/maven/maven-${MAVEN_MAJOR}/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-${MAVEN_VERSION} /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven

#Install Tomcat
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH ${CATALINA_HOME}/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR ${CATALINA_HOME}

ENV TOMCAT_MAJOR 9
ENV TOMCAT_VERSION 9.0.39
ENV TOMCAT_TGZ_URL https://www.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz

RUN set -x \
        && curl -fSL "$TOMCAT_TGZ_URL" -o tomcat.tar.gz \
        && curl -fSL "$TOMCAT_TGZ_URL.asc" -o tomcat.tar.gz.asc \
        && tar -xvf tomcat.tar.gz --strip-components=1 \
        && rm bin/*.bat \
        && rm tomcat.tar.gz* \
        && chmod -R 777 /usr/local/tomcat

#HTTP port
EXPOSE 8080

# Create a jersey artifact from Maven, package, and deploy the war on tomcat
RUN mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-webapp \
                -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
                -DgroupId=com.example -DartifactId=jerseyapp -Dpackage=com.example \
                -DarchetypeVersion=2.19 && \
    cd jerseyapp && mvn package -DskipTests && \
    mv target/jerseyapp.war /usr/local/tomcat/webapps && \
    rm -rf ~/jerseyapp

# Start Tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
