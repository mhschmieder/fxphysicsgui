# fxphysicsgui
JavaFX GUI support (including data model property bindings) for my physicstoolkit library

This library is split off from my FxGuiToolkit, as physics is a large umbrella for many concepts that don't come up in lots of applications that are more run-of-the-mill than the scientific and engineering desktop applicationsd that I tend to work on.

Additionally, I came to the conclusion that JavaFX-based observable properties containers tend to only be used in a JavaFX GUI context as well, so those got moved into this GUI library rather than staying behind in more general commons libraries. This also helps solve the dilemma of keeping pure data model libraries completely decoupled from any JavaFX package dependencies.
