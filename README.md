**TicTacToc**

TicTacToc is an enhanced version of the well known Tic-Tac-Toe game. TicTacToc is an Android app that allows two people to play the game on a phone, with the additional feature that each play has to be done within a limited amout of time ... if a player does not play within the time limit they lose their turn. The app keeps track of the players' scores - the first player to win 5 games is the winner of the tournament. The following details are easy to understand if you download, install, and run the TicTacToc app (don't change the names or clicking on the red and green buttons - that's part of the "Extra Credit" explained below).

The app starts on the scoring screen, which identifies the players by red and green buttons, and their scores as RatingBars. The "Start" button starts the play activity. The choice of which player goes first is random, but biased so that it is unlikely for one player to go first too many times in a row (more details below). The play interface indicates which player has to play, by displaying either a red button on the top left or a green button on top right of the screen. Below that is a ProgressBar that shows the time left for the current player. Finally the 3x3 board is below that, as a grid of buttons.

When a player selects a unplayed position in the grid, that button is colored to the player's color. If the play results in a win for that player, then the activity ends and returns to the scoring screen, where the RatingBar for the winning player is incremented. If a player reaches 5 wins, then the "Start" button is removed. If the play results in a draw, or the user uses the device's back button, then the activity then the activity ends and returns to the scoring screen, where nothing happens. If the play does not result in a win, then the player indicator is switched, the timer is reset, and the game continues. If a player fails to make a play within the time limit, then the player indicator is switched, the timer is reset, and the game continues.

The scoring interface has a menu that allows the play time to be adjusted to 1s, 2s, 5s, or 10s. The menu also allows the tournament to be reset.

Here's the trick for biasing the randomizing which player starts each game. At the start of the tournament set a dividing line at 0.5. For each game generate a random number. If the random number is less than the dividing line then green starts the game, and the dividing line is decreased by 0.1. Otherwise red starts and the dividing line is increased by 0.1.

Use best coding practices for all aspects of the code, including separated styles and dimens, and a nice icon!

The app allows the players to enter their names, and select a photo to use. In the play interface the players' names are used in the field that says "Player", and photos are used for the indicator and grid buttons. Hint: To put an image into a button, you need to convert the Uri to a Drawable, then use setForeground on the button. 
