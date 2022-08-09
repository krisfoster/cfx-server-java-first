
JARFILE_NAME=cxf-server-java-first-0.0.1-SNAPSHOT.jar

everything: clean profile compile

build:
	./gradlew --stop
	export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=true && ./gradlew build
	export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=true && ./gradlew bootJar
.PHONY: build

compile:
	export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=false && ./gradlew nativeCompile
.PHONY: compile

clean:
	./gradlew clean
.PHONY: clean

profile: build
	@echo "Profiling to generate config.."
	rm -f src/main/resources/META-INF/native-image/config-*.json
	# Run this in the backgournd
	java -DspringAot=true \
		-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image,track-reflection-metadata=false \
		-jar build/libs/$(JARFILE_NAME) \
		-DspringAot=true \
		-Dorg.apache.cxf.JDKBugHacks.all=true \
		-Djava.util.logging.config.file=./src/main/resources/logging.properties & echo $$! > .pid
	sleep 10
	# Hit the Rest endpoint
	curl -v http://localhost:8080/quote/a
	# Hit the SOAP endpoint
	curl -X POST -H "Content-Type: text/xml" \
    	-H 'SOAPAction: ' \
    	--data-binary @request.xml \
    	http://localhost:8080/cxf/ws/stockQuote
    # Kill the Java process
	cat .pid | xargs kill
	rm -f .pid
.PHONY: profile

run:
	echo "Profiling to generate config.."
	rm -f src/main/resources/META-INF/native-image/config-*.json
	# Run this in the backgournd
	java -DspringAot=true \
		-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image,track-reflection-metadata=false \
		-jar build/libs/$(JARFILE_NAME) \
		-DspringAot=true \
		-Dorg.apache.cxf.JDKBugHacks.all=true \
		-Djava.util.logging.config.file=./src/main/resources/logging.properties & echo $$! > .pid
.PHONY: run

