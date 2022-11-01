package edu.miami.cs.isabel_ogilvie.tictactoc;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

//=================================================================================================
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //-------------------------------------------------------------------------------------------------
    private final int P1_WIN = 1;
    private final int P2_WIN = 2;
    private final int TIE = 0;

    private ImageView p1Image, p2Image, newImage;
    private String p1Name, p2Name, p1Uri, p2Uri;
    private RatingBar p1ScoreBar, p2ScoreBar;
    private EditText name1Edit, name2Edit;
    private int p1Score = 0, p2Score = 0, time = 5000;
    private  double dividingLine = 0.5;

    //-------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assigning variables to corresponding xml elements (the layout views)
        p1Image = (ImageView) findViewById(R.id.image1);
        p2Image = (ImageView) findViewById(R.id.image2);
        name1Edit = (EditText) findViewById(R.id.name1);
        name2Edit = (EditText) findViewById(R.id.name2);
        p1Image.setOnClickListener(this);
        p2Image.setOnClickListener(this);
        p1ScoreBar = (RatingBar) findViewById(R.id.score1);
        p2ScoreBar = (RatingBar) findViewById(R.id.score2);


//        //updating the score bar
//        p1ScoreBar = findViewById(R.id.score1);
//        p1ScoreBar.setRating(p1Score);
//        p2ScoreBar = findViewById(R.id.score2);
//        p2ScoreBar.setRating(p2Score);


    }

    //-------------------------------------------------------------------------------------------------
    public void myClickHandler(View view) {

        boolean p1Start;

        //Play button sends names from edit texts and uri of pics chosen, if chosen.
        switch (view.getId()) {
            case R.id.start_button:
                //getting names
                p1Name = name1Edit.getText().toString();
                p2Name = name2Edit.getText().toString();

                //getting order of players (whos going first)
                p1Start = decideOrder();

                //sending names and user images to playGame
                Intent nextActivity = new Intent();
                nextActivity.setClassName("edu.miami.cs.isabel_ogilvie.tictactoc", "edu.miami.cs.isabel_ogilvie.tictactoc.PlayGame");

                nextActivity.putExtra(
                        "edu.miami.cs.isabel_ogilvie.tictactoc.p1Image", p1Uri);
                nextActivity.putExtra(
                        "edu.miami.cs.isabel_ogilvie.tictactoc.p2Image", p2Uri);
                nextActivity.putExtra(
                        "edu.miami.cs.isabel_ogilvie.tictactoc.name1", p1Name);
                nextActivity.putExtra(
                        "edu.miami.cs.isabel_ogilvie.tictactoc.name2", p2Name);
                nextActivity.putExtra(
                        "edu.miami.cs.isabel_ogilvie.tictactoc.p1Start", p1Start);
                nextActivity.putExtra("edu.miami.cs.isabel_ogilvie.tictactoc.time", time);

                //launching the tic tac toe game activity (PlayGame)
                startPlayGame.launch(nextActivity);

                break;
            default:
                break;
        }
    }

    //getting winner info from the playGame activity and updating the scoreBar and start button accordingly
    ActivityResultLauncher<Intent> startPlayGame = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //if the player wins, one star added to their respective scorebar, in case of tie no stars added to either scorebar
                    switch(result.getResultCode()) {
                        case P1_WIN:
                            if (p1Score <= 4) {
                                p1ScoreBar.setRating(++p1Score);
                            }
                            break;
                        case P2_WIN:
                            if (p2Score <= 4) {
                                p2ScoreBar.setRating(++p2Score);
                            }
                            break;
                        case TIE:
                            break;
                        default:
                            break;
                    }
                    //if a players score is 5, the tournament is won and done, so the start button is removed by making it invisible and unclickable
                    if (p1Score >= 5 || p2Score >= 5) {
                        Button startButton = (Button)findViewById(R.id.start_button);
                        startButton.setVisibility(Button.INVISIBLE);
                        startButton.setClickable(false);
                    }
                }
            });

    //getting the results from teh activity fetching the new image for user profile pic from gallery. it doesnt work tho :(
    ActivityResultLauncher<Intent> startGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                //this one sends the result of the gallery to the second activity
                @Override
                public void onActivityResult(ActivityResult result) {

                    //Gets pic uri from gallery and assigns them to ImageView through bitmap.


                        if (result.getResultCode() != RESULT_CANCELED) {
                            ImageView pictureView;
                            Bitmap selectedPicture = null;
                            Uri selectedUri;

                            pictureView = newImage;

                            if (newImage == p1Image) {
                                selectedUri = result.getData().getData();
                                p1Uri = result.getData().toUri(0);
                            } else {
                                selectedUri = result.getData().getData();
                                p2Uri = result.getData().toUri(0);
                            }

                            selectedUri = result.getData().getData();
                        }


                           // Drawable image = Drawable.createFromStream(context.getContentResolver().openInputStream(selectedUri),selectedUri.toString());
                }
            });

    //method taking player to gallery to choose new player picture
    public void replacePlayerImage(View view){
        Intent galleryIntent;

        newImage = (ImageView)view;

        galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startGallery.launch(galleryIntent);

    }

    //creating main menu using main menu xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Loading menu

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return(true);
    }

    //method attaching an action to each menu item so that when the button is clicked it performs the appropriate action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Menu items. Reset and set times for game.
        //sets the progress bar max time to the respective player choice (1s, 2s, 3s or 10s) or resets the entire tournament based on player choice
        switch (item.getItemId()) {
            case R.id.one_sec:
                time = 1000;
                return(true);
            case R.id.two_sec:
                time = 2000;
                return(true);
            case R.id.five_sec:
                time = 5000;
                return(true);
            case R.id.ten_sec:
                time = 10000;
                return(true);
            case R.id.reset:

                //Resetting game - this basically initializes the game except for names and pics

                p1Score = 0;
                p2Score = 0;
                time = 10000;
                dividingLine = 0.5;
                p1ScoreBar.setRating(p1Score);
                p2ScoreBar.setRating(p1Score);
                //making the button visible again if the button i gone - resetting the button
                Button startButton = (Button)findViewById(R.id.start_button);
                startButton.setVisibility(Button.VISIBLE);
                startButton.setClickable(true);
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }

    //method that attaches an action to clicking a player photo
    public void onClick(View view){
        replacePlayerImage(view);
    }

    //method deciding which player starts by choosing at random
    public boolean decideOrder(){
        double number = Math.random(); //choosing a random number to eliminate bias

        //returns true if p1 goes first, false if p2 goes first
        if (number < dividingLine) {
            dividingLine =- 0.1;
            return true;
        } else {
            dividingLine =+ 0.1;
            return false;
        }
    }


}

