# jdk-playground

This reproducer was trimmed down as much as possible.
Some classes and methods are unused, but they do have an impact on whether the problem gets reproduced or not.
The processed module includes the processor in its compile-time classpath (not just in the annotation processor path);
this is also necessary to reproduce the problem.

To reproduce:

```shell
./mvnw clean install -e
```

Resulting exception:

```shell
[...]
Caused by: java.lang.AssertionError: Filling jrt:/java.compiler/javax/lang/model/type/TypeMirror.class during JarFileObject[/home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar:/org/hibernate/jpamodelgen/util/TypeUtils$2.class]
    at com.sun.tools.javac.util.Assert.error (Assert.java:162)
    at com.sun.tools.javac.code.ClassFinder.fillIn (ClassFinder.java:366)
    at com.sun.tools.javac.code.ClassFinder.complete (ClassFinder.java:302)
    at com.sun.tools.javac.code.Symbol.complete (Symbol.java:682)
    at com.sun.tools.javac.code.Symbol$ClassSymbol.complete (Symbol.java:1418)
    at com.sun.tools.javac.code.Symbol.apiComplete (Symbol.java:688)
    at com.sun.tools.javac.code.Type$ClassType.getKind (Type.java:1208)
    at com.sun.tools.javac.code.Types$14.combineMetadata (Types.java:2413)
    at com.sun.tools.javac.code.Types$14.visitClassType (Types.java:2453)
    at com.sun.tools.javac.code.Types$14.visitClassType (Types.java:2409)
    at com.sun.tools.javac.code.Type$ClassType.accept (Type.java:1050)
    at com.sun.tools.javac.code.Types$DefaultTypeVisitor.visit (Types.java:4903)
    at com.sun.tools.javac.code.Types.erasure (Types.java:2404)
    at com.sun.tools.javac.code.Types.erasure (Types.java:2390)
    at com.sun.tools.javac.jvm.ClassReader.isSameBinaryType (ClassReader.java:1403)
    at com.sun.tools.javac.jvm.ClassReader.findMethod (ClassReader.java:1376)
    at com.sun.tools.javac.jvm.ClassReader.readEnclosingMethodAttr (ClassReader.java:1323)
    at com.sun.tools.javac.jvm.ClassReader$10.read (ClassReader.java:999)
    at com.sun.tools.javac.jvm.ClassReader.readAttrs (ClassReader.java:1435)
    at com.sun.tools.javac.jvm.ClassReader.readClassAttrs (ClassReader.java:1449)
    at com.sun.tools.javac.jvm.ClassReader.readClass (ClassReader.java:2931)
    at com.sun.tools.javac.jvm.ClassReader.readClassBuffer (ClassReader.java:3049)
    at com.sun.tools.javac.jvm.ClassReader.readClassFile (ClassReader.java:3073)
    at com.sun.tools.javac.code.ClassFinder.fillIn (ClassFinder.java:373)
    at com.sun.tools.javac.code.ClassFinder.complete (ClassFinder.java:302)
    at com.sun.tools.javac.code.Symbol.complete (Symbol.java:682)
    at com.sun.tools.javac.code.Symbol$ClassSymbol.complete (Symbol.java:1418)
    at com.sun.tools.javac.code.Symbol.apiComplete (Symbol.java:688)
    at com.sun.tools.javac.code.Symbol$TypeSymbol.getEnclosedElements (Symbol.java:859)
    at com.sun.tools.javac.code.Symbol$TypeSymbol.getEnclosedElements (Symbol.java:797)
    at org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor.process (JPAMetaModelEntityProcessor.java:36)
    at com.sun.tools.javac.processing.JavacProcessingEnvironment.callProcessor (JavacProcessingEnvironment.java:1021)
    at com.sun.tools.javac.processing.JavacProcessingEnvironment$DiscoveredProcessors$ProcessorStateIterator.runContributingProcs (JavacProcessingEnvironment.java:857)
    at com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run (JavacProcessingEnvironment.java:1263)
    at com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing (JavacProcessingEnvironment.java:1402)
    at com.sun.tools.javac.main.JavaCompiler.processAnnotations (JavaCompiler.java:1276)
    at com.sun.tools.javac.main.JavaCompiler.compile (JavaCompiler.java:946)
    at com.sun.tools.javac.api.JavacTaskImpl.lambda$doCall$0 (JavacTaskImpl.java:104)
    at com.sun.tools.javac.api.JavacTaskImpl.invocationHelper (JavacTaskImpl.java:152)
    at com.sun.tools.javac.api.JavacTaskImpl.doCall (JavacTaskImpl.java:100)
    at com.sun.tools.javac.api.JavacTaskImpl.call (JavacTaskImpl.java:94)
    at org.codehaus.plexus.compiler.javac.JavaxToolsCompiler.compileInProcess (JavaxToolsCompiler.java:126)
    at org.codehaus.plexus.compiler.javac.JavacCompiler.performCompile (JavacCompiler.java:174)
    at org.apache.maven.plugin.compiler.AbstractCompilerMojo.execute (AbstractCompilerMojo.java:1134)
    at org.apache.maven.plugin.compiler.CompilerMojo.execute (CompilerMojo.java:187)
    at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:137)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:210)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:105)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:972)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:293)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:196)
    at jdk.internal.reflect.DirectMethodHandleAccessor.invoke (DirectMethodHandleAccessor.java:103)
    at java.lang.reflect.Method.invoke (Method.java:580)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:282)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:225)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:406)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:347)
    at jdk.internal.reflect.DirectMethodHandleAccessor.invoke (DirectMethodHandleAccessor.java:103)
    at java.lang.reflect.Method.invoke (Method.java:580)
    at org.apache.maven.wrapper.BootstrapMainStarter.start (BootstrapMainStarter.java:52)
    at org.apache.maven.wrapper.WrapperExecutor.execute (WrapperExecutor.java:161)
    at org.apache.maven.wrapper.MavenWrapperMain.main (MavenWrapperMain.java:73)
```

The reproducer relies on Maven and compiles with `javax.tools`,
which has the advantage of producing a detailed stacktrace.
Compilations fails when using `javac` too,
but then you won't get any details about the failure:

```shell
./mvnw clean install -e -Dmaven.compiler.forceJavacCompilerUse=true
```

Compiler logs:

```
[INFO] Compiling 2 source files to /home/yrodiere/workspaces/testcases/jdk-playground/processed/target/classes
[parsing started SimpleFileObject[/home/yrodiere/workspaces/testcases/jdk-playground/processed/src/main/java/org/hibernate/jpamodelgen/test/Dao1.java]]
[parsing completed 2ms]
[parsing started SimpleFileObject[/home/yrodiere/workspaces/testcases/jdk-playground/processed/src/main/java/org/hibernate/jpamodelgen/test/util/CompilationStatement.java]]
[parsing completed 1ms]
[loading /modules/java.base/module-info.class]
[loading /modules/jdk.internal.vm.ci/module-info.class]
[loading /modules/jdk.jcmd/module-info.class]
[loading /modules/java.logging/module-info.class]
[loading /modules/jdk.internal.ed/module-info.class]
[loading /modules/java.xml.crypto/module-info.class]
[loading /modules/jdk.jpackage/module-info.class]
[loading /modules/java.naming/module-info.class]
[loading /modules/jdk.jconsole/module-info.class]
[loading /modules/java.desktop/module-info.class]
[loading /modules/jdk.editpad/module-info.class]
[loading /modules/java.compiler/module-info.class]
[loading /modules/jdk.xml.dom/module-info.class]
[loading /modules/jdk.internal.le/module-info.class]
[loading /modules/jdk.jartool/module-info.class]
[loading /modules/jdk.httpserver/module-info.class]
[loading /modules/java.datatransfer/module-info.class]
[loading /modules/jdk.dynalink/module-info.class]
[loading /modules/jdk.management/module-info.class]
[loading /modules/java.sql/module-info.class]
[loading /modules/java.net.http/module-info.class]
[loading /modules/java.smartcardio/module-info.class]
[loading /modules/java.se/module-info.class]
[loading /modules/java.management.rmi/module-info.class]
[loading /modules/jdk.crypto.ec/module-info.class]
[loading /modules/java.rmi/module-info.class]
[loading /modules/jdk.jdeps/module-info.class]
[loading /modules/java.prefs/module-info.class]
[loading /modules/java.security.sasl/module-info.class]
[loading /modules/jdk.graal.compiler/module-info.class]
[loading /modules/jdk.security.auth/module-info.class]
[loading /modules/jdk.net/module-info.class]
[loading /modules/jdk.jlink/module-info.class]
[loading /modules/jdk.javadoc/module-info.class]
[loading /modules/jdk.jshell/module-info.class]
[loading /modules/jdk.unsupported.desktop/module-info.class]
[loading /modules/jdk.internal.opt/module-info.class]
[loading /modules/jdk.crypto.cryptoki/module-info.class]
[loading /modules/jdk.jsobject/module-info.class]
[loading /modules/jdk.nio.mapmode/module-info.class]
[loading /modules/java.xml/module-info.class]
[loading /modules/java.security.jgss/module-info.class]
[loading /modules/java.transaction.xa/module-info.class]
[loading /modules/jdk.compiler/module-info.class]
[loading /modules/java.instrument/module-info.class]
[loading /modules/jdk.charsets/module-info.class]
[loading /modules/java.scripting/module-info.class]
[loading /modules/jdk.hotspot.agent/module-info.class]
[loading /modules/jdk.internal.jvmstat/module-info.class]
[loading /modules/java.sql.rowset/module-info.class]
[loading /modules/jdk.sctp/module-info.class]
[loading /modules/jdk.jdi/module-info.class]
[loading /modules/jdk.naming.rmi/module-info.class]
[loading /modules/jdk.incubator.vector/module-info.class]
[loading /modules/jdk.jdwp.agent/module-info.class]
[loading /modules/jdk.random/module-info.class]
[loading /modules/jdk.unsupported/module-info.class]
[loading /modules/jdk.attach/module-info.class]
[loading /modules/jdk.security.jgss/module-info.class]
[loading /modules/jdk.jfr/module-info.class]
[loading /modules/jdk.localedata/module-info.class]
[loading /modules/java.management/module-info.class]
[loading /modules/jdk.zipfs/module-info.class]
[loading /modules/jdk.accessibility/module-info.class]
[loading /modules/jdk.naming.dns/module-info.class]
[loading /modules/jdk.jstatd/module-info.class]
[loading /modules/jdk.management.agent/module-info.class]
[loading /modules/jdk.graal.compiler.management/module-info.class]
[loading /modules/jdk.management.jfr/module-info.class]
[search path for source files: /home/yrodiere/workspaces/testcases/jdk-playground/processed/src/main/java,/home/yrodiere/workspaces/testcases/jdk-playground/processed/target/generated-sources/annotations]
[search path for class files: /home/yrodiere/tools/java/jdk-22-ea+25/lib/modules,/home/yrodiere/workspaces/testcases/jdk-playground/processed/target/classes,/home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar,.]
[loading /modules/java.base/java/util/List.class]
[loading /modules/java.base/java/lang/Object.class]
[loading /modules/java.base/java/lang/String.class]
[loading /modules/java.base/java/lang/Deprecated.class]
[loading /modules/java.base/java/lang/annotation/Annotation.class]
[loading /modules/java.base/java/lang/annotation/Retention.class]
[loading /modules/java.base/java/lang/annotation/RetentionPolicy.class]
[loading /modules/java.base/java/lang/annotation/Target.class]
[loading /modules/java.base/java/lang/annotation/ElementType.class]
Round 1:
        input files: {org.hibernate.jpamodelgen.test.Dao1, org.hibernate.jpamodelgen.test.util.CompilationStatement}
        annotations: [java.lang.Deprecated]
        last round: false
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/Context.class)]
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/JPAMetaModelEntityProcessor.class)]
[loading /modules/java.compiler/javax/annotation/processing/SupportedAnnotationTypes.class]
Processor org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor matches [java.base/java.lang.Deprecated] and returns true.
Round 2:
        input files: {}
        annotations: []
        last round: true
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/util/AccessTypeInformation.class)]
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/util/TypeUtils.class)]
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/util/TypeUtils$1.class)]
[loading /home/yrodiere/workspaces/testcases/jdk-playground/processor/target/jdk-playground-processor-1.0-SNAPSHOT.jar(/org/hibernate/jpamodelgen/util/TypeUtils$2.class)]
[total 55ms]
```