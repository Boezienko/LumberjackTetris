## Tetris

Division of labor

Schaene

- Initial prototype of game with basic functionality
- Multiplayer functionality
- Interfacing with arcade machine and external controller
- Rotation of pieces
- Kicks for rotating pieces at last second
- Initial graphics display including resizing
- Hard drop
- 7 bag for more reasonable “random” piece generation
- Displaying held piece
- Held piece implementation

  Sydney

- Factory design pattern implementation
- Implementation of secondary canvas to display statistics
- Displaying next piece to be dropped
- Next piece implementation
- Displaying score and level
- Score mechanics
- Leveling and increasing speed of piece drop upon level increase
- Refactoring code to enforce design principles
- Documentation

  Boe

- Intro title screen
- Initial UML
- Designed animation for title screen
- Lose condition
- Organized classes into packages (much more difficult than we initially thought)
- Display for once player loses
- Re-enable start button to play again upon lose allowing re-start
- Leaderboard implementation

Design decisions

- We designed out software so that each tetromino type (I, L, J, S, Z, T, O) is a subclass of tetromino. By doing this, we can use polymorphism to determine the specific subclass of each tetromino at runtime and treat all of them as objects of the Tetromino class
- We used factories to abstract the generation of each Tetromino and enforce Single Responsibility Principle

  Known bugs

- None

  Debugging collaborators

- None

  Approximate time spent

- 100 hours
