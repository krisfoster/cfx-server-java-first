.PHONY: build

build:
	./gradlew build

compile: build
	./compile-only.sh

# 			-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \

runjar:
	echo "profiling to generate config.."
	java -jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar \
			-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
			-Dorg.apache.cxf.JDKBugHacks.all=true \
			-Djava.util.logging.config.file=./src/main/resources/logging.properties

profile:
	echo "profiling to generate config.."
	java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image \
			-jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar \
			-Dorg.apache.cxf.JDKBugHacks.all=true \
			-Dorg.graalvm.nativeimage.imagecode=agent \
			-Djava.util.logging.config.file=./src/main/resources/logging.properties

clean:
	./gradlew clean

run:
	./build/native-image/cxf-server-java-first -Dorg.apache.cxf.JDKBugHacks.all=true



d-build:
	docker build \
		--pull \
		-f Dockerfile \
		-t krisfoster/cxf:01 .

d-run:
	docker run --rm -it -P krisfoster/cxf:01 /bin/bash
