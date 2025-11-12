# Reproducer for Javac issue

Call `./reproduce.sh 1` to reproduce problem 1, `./reproduce.sh 2` to reproduce problem 2.

The scenarios only differ by the order of `ProblematicClass$1B.class` vs. `ProblematicClass$1C.class` in the JAR.

Both problems can be observed when putting a breakpoint into `com.sun.tools.javac.jvm.ClassReader.TypeAnnotationSymbolVisitor.visitVarSymbol`
with the condition `s instanceof RecordComponent && s.owner.owner instanceof MethodSymbol`.

## Problem 1: javac crashes with java.lang.AssertionError: Cannot add metadata to this type: METHOD.

In this scenario, the order in which the `ClassFinder` will fill the `PackageSymbol` members is:

```
a/ProblematicClass$1B.class
a/ProblematicClass$1C.class
a/ProblematicClass.class
```

Due to this, `ClassSymbol#apiComplete` is called for `B` first.

In `com.sun.tools.javac.jvm.ClassReader.TypeAnnotationStructuralTypeMapping#visitClassType`,
the enclosing type of `B` is a `MethodType`,
so the call to `visit` will call `visitType`, which then calls `com.sun.tools.javac.jvm.ClassReader.TypeAnnotationStructuralTypeMapping#reannotate`. 

In `reannotate`, the call to `com.sun.tools.javac.code.Type#annotatedType` will try to clone the `MethodType`,
which fails with an `AssertionError`, saying that metadata can't be added to METHOD.

## Problem 2 (Possibly related?): incorrect processing order (?) and unknown impact

In this scenario, the order in which the `ClassFinder` will fill the `PackageSymbol` members is:

```
a/ProblematicClass$1C.class
a/ProblematicClass$1B.class
a/ProblematicClass.class
```

Due to this, `ClassSymbol#apiComplete` is called for `C` first, even though it depends on `B`.
`C` is a record that has a record component of type `B`, so in the "finish attaching annotations" phase,
because the `ClassType` for `B` is not completed yet, the following code wrongly assumes that `B` has no enclosing type.

The `TypeAnnotationCompleter` eventually calls `TypeAnnotationSymbolVisitor#addTypeAnnotations` for the record component of `C`,
trying to add a `TYPE_USE` annotation `@Nullable`, which ends up in `com.sun.tools.javac.jvm.ClassReader.TypeAnnotationStructuralTypeMapping#visitClassType`.

In this method, the enclosing type of the passed `ClassType` for `B` is read, which at this point is still `Type.noType`, because the `ClassSymbol` of `B` wasn't completed yet.
Due to that, the `visit` method is not called on the `MethodType` and problem 1 is not reproduced.

It's unclear if there is another impact (incorrect metadata?) hiding just beneath the surface.
