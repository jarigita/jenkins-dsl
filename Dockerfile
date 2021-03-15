FROM jenkins/jenkins:alpine

ENV JENKINS_REF /usr/share/jenkins/ref

# install jenkins plugins
COPY jenkins-home/plugins.txt $JENKINS_REF/
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

ENV JAVA_OPTS -Dorg.eclipse.jetty.server.Request.maxFormContentSize=100000000 \
 			  -Dorg.apache.commons.jelly.tags.fmt.timeZone=Europe/Madrid \
 			  -Dhudson.diyChunking=false \
 			  -Djenkins.install.runSetupWizard=false

# copy scripts and ressource files
COPY jenkins-home/*.* $JENKINS_REF/
COPY jenkins-home/userContent $JENKINS_REF/userContent
COPY jenkins-home/jobs $JENKINS_REF/jobs/
COPY jenkins-home/init.groovy.d $JENKINS_REF/init.groovy.d/
COPY jenkins-home/config-file-provider $JENKINS_REF/config-file-provider/
COPY jenkins-home/dsl $JENKINS_REF/dsl/
