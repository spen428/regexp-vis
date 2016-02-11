# Read-me
This is the Git repository for the CO600 project.

## Team members
* Matthew Nicholls <mjn33@kent.ac.uk>
* Sam Pengelly <sp611@kent.ac.uk>
* Parham Ghassemi <pg272@kent.ac.uk>
* Billy Dix <wrd2@kent.ac.uk>

## Project Structure
The project code can be found under the `regexp_vis` directory, documentation
under `doc`, and miscellaneous tools and scripts under `tools`.

`regexp_vis` is further subdivided into modules. The project follows a
Model-View-Controller (MVC) architecture design pattern, `model` and
`view` are designed to not depend on any other modules, the
`controller` package acts as the "glue" between the `model` and `view`
packages. Accompanying unit tests are in packages `test.model`,
`test.view`, and `test.controller`.

The application is written using JavaFX, see the JavaFX migration
document for more information.

We have also retained some demo code written to test the *Jython* programming
language, as we initially considered using that language so that we could
utilise libraries and syntactic sugar from both Java and Python. This demo can
be found under the `jython_test` directory.
