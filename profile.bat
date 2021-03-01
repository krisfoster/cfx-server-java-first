SET rootPath=%CD%
SET GRAAL_AGENT_PATH=%rootPath%/src/main/resources/META-INF/native-image

  java ^
  -agentlib:native-image-agent=config-output-dir="%GRAAL_AGENT_PATH%" ^
  -jar build/libs/cxf-server-java-first-0.0.1-SNAPSHOT.jar --debug