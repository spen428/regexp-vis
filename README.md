# Read-me

This is the Git repository for the CO600 project.

## Team members

* Matthew Nicholls <mjn33@kent.ac.uk>
* Sam Pengelly <sp611@kent.ac.uk>
* Parham Ghassemi <pg272@kent.ac.uk>
* Billy Dix <wrd2@kent.ac.uk>

## Project Structure

The project code can be found under the `regexp_vis` directory and miscellaneous
tools and scripts under `tools`. Project management documentation was previously
under a directory called `doc`, this has been erased from the Git history.

`regexp_vis` is further subdivided into modules. The project follows a
Model-View-Controller (MVC) architecture design pattern, `model` and `view` are
designed to not depend on any other modules, the `controller` package acts as
the "glue" between the `model` and `view` packages. Accompanying unit tests are
in packages `test.model`, `test.view`, and `test.controller`.

We have also retained some demo code written to test the *Jython* programming
language, as we initially considered using that language so that we could
utilise libraries and syntactic sugar from both Java and Python. This demo can
be found under the `jython_test` directory.

## Dependencies

- Apache Ant
- JDK 8+ (including JavaFX)

## Compiling and Running

Use `ant jar` in the `regexp_vis` directory to build the project and generate an
executable jar file. The program can then be run by executing the jar `ant run`
or `java -jar dist/{jarname}.jar`.

## Testing

The project includes a number of unit tests that can be compiled and run by
executing the command `ant runtests` in the `regexp_vis` directory. This will
generate HTML-formatted test results under the directory `testreports`.

## Licence

This project is licenced under the MIT / X11 licence, a copy can be found in the
LICENSE file.
