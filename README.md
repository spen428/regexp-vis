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

`regexp_vis` is further subdivided into modules. The project is
inspired by the Model-View-Controller (MVC) architecture design
pattern, however the main distinction currently is between the Model
(the `model` package) and the View and Controller combined (the `ui`
package). Accompanying unit tests are in packages `test.model` and
`test.ui`.

We have also retained some demo code written to test the *Jython* programming
language, as we initially considered using that language so that we could
utilise libraries and syntactic sugar from both Java and Python. This demo can
be found under the `jython_test` directory.
