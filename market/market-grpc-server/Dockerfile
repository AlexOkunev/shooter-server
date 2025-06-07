FROM amazoncorretto:21-alpine-full
LABEL "version"="${project.version}"

ENV BASE_DIR=/usr/share/market-grpc-server

RUN mkdir -p $BASE_DIR

WORKDIR $BASE_DIR

COPY ./target/market-grpc-server-${project.version}.jar .

CMD java -jar $BASE_DIR/market-grpc-server-${project.version}.jar