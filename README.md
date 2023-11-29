# jdk-playground

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
    at com.sun.tools.javac.jvm.ClassReader.readClass (ClassReader.java:2926)
    at com.sun.tools.javac.jvm.ClassReader.readClassBuffer (ClassReader.java:3044)
    at com.sun.tools.javac.jvm.ClassReader.readClassFile (ClassReader.java:3068)
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
