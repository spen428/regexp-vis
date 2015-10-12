Jython Test
===========
Author: sp611

Intro
-----
As of Jython 2.2, support for compiling Jython to Java bytecode is no longer 
supported. Therefore the application must run on the Jython interpretter, 
unless a wrapper that can run on the JVM is used (if one even exists).

Req
---
- JDK
- JRE
- Python
- Jython

Compilation
-----------

Execute `sh crun.sh` to compile and run the example project.

To manually compile and run:
```
javac src/SomeClass.java
jython src/some_class.py
```

