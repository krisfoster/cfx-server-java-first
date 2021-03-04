.PHONY: build

build:
	./gradlew build

compile: build
	./compile-only.sh

runjar:
	@echo "Running JAR"
	java -Dorg.apache.cxf.JDKBugHacks.all=true \
			-Dorg.graalvm.nativeimage.imagecode=agent \
			-Djava.util.logging.config.file=./src/main/resources/logging.properties \
 			-jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar

debug:
	@echo "Debugging JAR"
	java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
			-Dorg.graalvm.nativeimage.imagecode=agent \
			-Dorg.apache.cxf.JDKBugHacks.all=true \
			-Djava.util.logging.config.file=./src/main/resources/logging.properties \
			-jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar

profile: build
	@echo "Profiling to generate config.."
	java -agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image \
			-Dorg.graalvm.nativeimage.imagecode=agent \
			-Dorg.apache.cxf.JDKBugHacks.all=true \
			-jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar

capture: build
	@echo "Capturing"
	java -Dcapture=true \
         -Dcapture.dir=src/main/resources/dump \
         -Dorg.apache.cxf.JDKBugHacks.all=true \
         -Dorg.graalvm.nativeimage.imagecode=agent \
		  -jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar

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
