package edu.miami.cs.isabel_ogilvie.tictactoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
//=================================================================================================
public class PlayGame extends Activity {
    private final int P1_WIN = 1;
    private final int P2_WIN = 2;
    private final int TIE = 0;
    private int movesCount; //amount of moves made
    private int[][] buttonGrid; //the tic tac toe grid of buttons represented in an array
    private int barTime;
    private final int CLICK_TIME = 100;
    ProgressBar myProgressBar;
    private boolean playedYet = false;

    String p1Name, p2Name, currName;
    Uri p1Uri, p2Uri;
    boolean p1Turn;
    Uri currUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        //Getting image uris to display at top
        if (this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.player_image_1") != null) {
            p1Uri = Uri.parse(this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.p1Image"));
        }
        if (this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.player_image_2") != null) {
            p2Uri = Uri.parse(this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.p2Image"));
        }
        //Getting player names
        p1Name = this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.name1");
        p2Name = this.getIntent().getStringExtra("edu.miami.cs.isabel_ogilvie.tictactoc.name2");
        //Getting turn and time to play
        barTime = this.getIntent().getIntExtra("edu.miami.cs.isabel_ogilvie.tictactoc.time", 10000);
        p1Turn = this.getIntent().getBooleanExtra("edu.miami.cs.isabel_ogilvie.tictactoc.p1Start", true);
        //Set up progress bar
        myProgressBar = (ProgressBar)findViewById(R.id.time_left);
        myProgressBar.setMax(barTime);
        myProgressBar.setProgress(myProgressBar.getMax());
        //Initializing tic tac toe grid to assign 0 to each button, 0 meaning that it hasnt been chosen yet
        buttonGrid = new int[3][3];
        for(int i=0; i < 3; i++) {
            for (int j=0; j < 3; j++) {
                buttonGrid[i][j] = 0;
            }
        }

        loadImages(); //loading player pic images to display at top
        currentPlayerView(); //loading correct player name and pic to display at top
    }

    //method loading the current player name and picture to display at top
    private void currentPlayerView() {
        //assigning variables to the respective image view xml layout
        ImageView image1 = (ImageView)findViewById(R.id.image1);
        ImageView image2 = (ImageView)findViewById(R.id.image2);

        playedYet = false; //initializing that th ecurrent player has not played as yet

        //Showing the name and picture of current player and hiding the ImageView of other player.
        if (p1Turn) {
            currUri = p1Uri;
            currName = p1Name;
            image1.setVisibility(ImageView.VISIBLE);
            image2.setVisibility(ImageView.INVISIBLE);
        } else {
            currUri = p2Uri;
            currName = p2Name;
            image1.setVisibility(ImageView.INVISIBLE);
            image2.setVisibility(ImageView.VISIBLE);
        }
        //setting the name currently being displayed at the top to that of the current player
        TextView text = (TextView)findViewById(R.id.player_name);
        text.setText(currName);

        //Run progress bar progresser
        myProgresser.run();
    }

    //method allowing progress bar to run
    private final Runnable myProgresser = new Runnable() {

        private Handler myHandler = new Handler();

        public void run() {

            //Removes callbacks and reset bar if player played, and if not updates bar.
            if (playedYet) {
                myHandler.removeCallbacksAndMessages(null);
                myProgressBar.setProgress(0);
            } else {
                myProgressBar.setProgress(myProgressBar.getProgress()-CLICK_TIME);
            }
            //Switches the current player if time runs out (skippping their turn), then resets progress bar, and starts next player turn
            if (myProgressBar.getProgress() == 0) {
                p1Turn = !p1Turn;
                myProgressBar.setProgress(myProgressBar.getMax());
                currentPlayerView();

            } else {
                myHandler.postDelayed(myProgresser,CLICK_TIME);
            }

        }
    };

    //method to load player pic images (if no new image selected, uses default red&green icons)
    private void loadImages() {
        ImageView p1Image;
        ImageView p2Image;

        p1Image = (ImageView)findViewById(R.id.image1);
        p2Image = (ImageView)findViewById(R.id.image2);

        //include case for new selected image if I have time - check if image URI is null nd if its not set the existing uri to a drawable image to use

        p1Image.setImageResource(R.drawable.red);
        p2Image.setImageResource(R.drawable.green);

    }

    public void myClickHandler(View v) {

        //Keep track of # of moves so you know when no more moves available in order to check tie condition
        movesCount++;
        TableRow parent;
        int row,col;

        ImageButton button = (ImageButton)v;
        parent = (TableRow)v.getParent();

        //Getting location of play and putting in board to check win condition, using tags for position
        col = Integer.parseInt(v.getTag().toString());
        row = Integer.parseInt(parent.getTag().toString());

        //1 for red and -1 for green player. Can check +3 and -3 for win condition
        if (p1Turn) {
            buttonGrid[row][col] = 1;
        } else {
            buttonGrid[row][col] = -1;
        }

        playedYet = true;
        //setting the button with the appropriate player pic and making sure the button cant be clicked again
        if (p1Turn) {
            button.setImageResource(R.drawable.red);
        } else {
            button.setImageResource(R.drawable.green);
        }
        button.setClickable(false);

        //Check if the game is over. If so, returns the winner or if its a tie to Main Activity.
        if (isGameOver()) {
            Intent returnIntent = new Intent();
            returnIntent.setClassName("edu.miami.cs.isabel_ogilvie.tictactoc", "edu.miami.cs.isabel_ogilvie.tictactoc.MainActivity");
           //there are only 9 possible moves since there are only 9 grids, so if 9 moves have been made and no winner its a tie
            if (movesCount >= 9) {
                setResult(TIE, returnIntent);
                finish();
            }
            //if the game is over on p1's turn and there are still moves left, then player 1 has won and vice versa
            if (p1Turn) {
                setResult(P1_WIN, returnIntent);
                finish();
            } else {
                setResult(P2_WIN, returnIntent);
                finish();
            }
        }
    }

    //checking if the game has been won or tied
    private boolean isGameOver() {
        int i, j;
        int total = 0;

        //Checking if any rows in the grid have been completed by the same player (so if a each button assigned a pos 1, p1 has gotten a
        //strike across the row, if each button in row assigned a -1, the p2 has gotten a strike across the row, so if its 3 or -3 a player has won
        for (i = 0; i < 3; i++) {
            total = 0;
            for (j = 0; j < 3; j++) {
                total += buttonGrid[i][j];
            }
            if (total >= 3 || total <= -3) {
                return true; //the current player wins
            }
        }
        //Checking if any columns in the grid have been completed by the same player (so if a each button assigned a pos 1, p1 has gotten a
        //strike across the column, if each button in row assigned a -1, the p2 has gotten a strike across the row, so if its 3 or -3 a player has won
        for (j = 0; j < 3; j++) {
            total = 0;
            for (i = 0; i < 3; i++) {
                total += buttonGrid[i][j];
            }
            if (total >= 3 || total <= -3) {
                return true; //currPlayer wins
            }
        }
        //Checking if the right to left diagonal in the grid has been completed by the same player
        total = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                if (i == j) {
                    total += buttonGrid[i][j];
                }
            }
        }
        if (total >= 3 || total <= -3) {
            return true; // currPlayer wins
        }
        //Checking if the left to right diagonal in the grid has been completed by the same player
        total = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                if (i + j == 2) {
                    total += buttonGrid[i][j];
                }
            }
        }
        if (total >= 3 || total <= -3) {
            return true; // currPlayer wins
        }
        //Checking for a tie case - if 9 moves have been made then the grid has been completed (no more moves possible) its finished in a tie
        if (movesCount >= 9) {
            return true; //its a tie
        }
        return false;//game is not over
    }

}
//=================================================================================================
