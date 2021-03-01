FROM krisfoster/graal-ee-ol-base-debug:21.0.0.2-JDK8

RUN mkdir app
COPY . /app
WORKDIR app

RUN make compile