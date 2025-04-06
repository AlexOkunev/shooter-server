FROM amazoncorretto:21-alpine-full
LABEL "version"="${project.version}"

ENV BASE_DIR=/usr/share/inventory-grpc-server

RUN mkdir -p $BASE_DIR

WORKDIR $BASE_DIR

COPY ./target/inventory-grpc-server-${project.version}.jar .

CMD java -jar $BASE_DIR/inventory-grpc-server-${project.version}.jar