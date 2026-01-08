FROM amazoncorretto:21
LABEL "version"="${project.version}"

ENV BASE_DIR=/usr/share/${project.artifactId}

RUN mkdir -p $BASE_DIR

WORKDIR $BASE_DIR

COPY ./target/${project.artifactId}-${project.version}.jar .

CMD java -jar $BASE_DIR/${project.artifactId}-${project.version}.jar