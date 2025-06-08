FROM amazoncorretto:21-alpine-full
LABEL "version"="${project.version}"

ENV BASE_DIR=/usr/share/payment-stub-grpc-server

RUN mkdir -p $BASE_DIR

WORKDIR $BASE_DIR

COPY ./target/payment-stub-grpc-server-${project.version}.jar .

CMD java -jar $BASE_DIR/payment-stub-grpc-server-${project.version}.jar