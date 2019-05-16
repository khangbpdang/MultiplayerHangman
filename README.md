# Multiplayer Hangman
## This README serves as a manual for the Multiplayer Hangman game
### By Khang Dang

#### For the game server:
- Within the folder "Multiplayer Hangman Server", run the main method of MultiplayerHangmanServerWithThread class and let it sit.
- Result for multiple game with multiple users should be displayed through the same terminal with no interruption/error.

#### For the client:
- Within the folder "Multiplayer Hangman Client", run the main method of MultiplayerHangmanClient class and and begin to interact with the server.

#### For multi-threaded purpose:
- Create a copy of the folder "Multiplayer Hangman Client" and repeat the same steps for the client to connect to the server.


#### Features implemented:
- Interaction between player and server to play the game.
- Overall Leaderboard - Players need to finish one game first before they can see the leaderboard
- Mini-scoreboard - With each correct guesses in each game instance, the server will compute the scores of the current instance and send to players accordingly.
- Thread support
- Synchronization support

#### Features to be implemented given more time:
- GUI support
- Username and password authentication.
- More advanced input sanitation.
- Database support (MySQL).

