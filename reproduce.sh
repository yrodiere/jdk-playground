#!/usr/bin/env -S bash -e

if (( $# != 1 )) || (( $1 < 1 )) || (( $1 > 2 ))
then
  cat 2>&1 <<EOF
Invalid arguments.

Usage:
- To demonstrate problem 1:
    $0 1
- To demonstrate problem 2:
    $0 2
EOF
  exit 1
fi

rm -rf module1/target/ module2/target/ processor/target/
mkdir -p module1/target/classes module2/target/classes processor/target/classes

set -x

echo "== Building Library"
javac -d module1/target/classes \
  -classpath : \
  -sourcepath module1/src/main/java: \
  -g -encoding UTF-8 \
  module1/src/main/java/a/Nullable.java module1/src/main/java/a/Processed.java module1/src/main/java/a/ProblematicClass.java
if (( $1 == 1 ))
then
  echo "Demonstrating problem 1: java.lang.AssertionError: Cannot add metadata to this type: METHOD"
  jar cf module1/target/module1.jar \
    -C module1/target/classes a/Nullable.class \
    -C module1/target/classes a/ProblematicClass.class \
    -C module1/target/classes 'a/ProblematicClass$1B.class' \
    -C module1/target/classes 'a/ProblematicClass$1C.class' \
    -C module1/target/classes a/Processed.class
else
  echo "Demonstrating problem 2: @Nullable annotation is not added to parameter of 1C's constructor upon loading."
  jar cf module1/target/module1.jar \
    -C module1/target/classes a/Nullable.class \
    -C module1/target/classes a/ProblematicClass.class \
    -C module1/target/classes 'a/ProblematicClass$1C.class' \
    -C module1/target/classes 'a/ProblematicClass$1B.class' \
    -C module1/target/classes a/Processed.class
fi

echo "== Building Processor"
javac -d processor/target/classes \
  -classpath module1/target/module1.jar: \
  -sourcepath processor/src/main/java: \
  -g -encoding UTF-8 \
  processor/src/main/java/a/processor/MyProcessor.java
jar cf processor/target/processor.jar \
  -C processor/target/classes a/processor/MyProcessor.class \
  -C processor/src/main/resources META-INF/services/javax.annotation.processing.Processor

echo "== Building Application using processor + library"
javac -d module2/target/classes \
  -classpath module1/target/module1.jar: \
  -processorpath processor/target/processor.jar:module1/target/module1.jar: \
  -sourcepath module2/src/main/java: \
  -processor a.processor.MyProcessor \
  -g -encoding UTF-8 \
  module2/src/main/java/b/Reproducer.java
