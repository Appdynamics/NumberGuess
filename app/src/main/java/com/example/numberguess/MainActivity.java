package com.example.numberguess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.example.numberguess.MESSAGE";
    public static final String GUESSES       = "com.example.numberguess.GUESSES";
    public static final String PLAYS         = "com.example.numberguess.PLAYS";
    public static final String TAG           = "NUMGUESS.Main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d( TAG, "onCreate(): BEGIN");
        setContentView(R.layout.activity_main);
        Log.d( TAG, "onCreate():   END");
    } // end onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        Log.d( TAG, "onStart(): BEGIN");

        Log.d( TAG, "\tguess = " + Stats.guessTotal + ", games = " + Stats.gamesPlayed );
        TextView valPlays = (TextView) findViewById(R.id.valPlays);
        TextView valGuess = (TextView) findViewById(R.id.valGuesses);
        valPlays.setText(new Integer(Stats.gamesPlayed).toString());
        valGuess.setText(new Integer(Stats.guessTotal).toString());

        Log.d( TAG, "onStart():   END");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d( TAG, "onStop(): BEGIN");
        Log.d( TAG, "onStop():   END");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d( TAG, "onSaveInstanceState(): BEGIN");
        Log.d( TAG, "onSaveInstanceState():   END");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d( TAG, "onRestoreInstanceState(): BEGIN");
        Log.d( TAG, "onRestoreInstanceState():   END");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( TAG, "onDestroy(): BEGIN");
        Log.d( TAG, "onDestroy():   END");
    }

    // HELPER METHODS


    public void playGame(View view) {
        Intent intent = new Intent(this, GuessNumberActivity.class);
        EditText etName = (EditText) findViewById(R.id.etName);
        String message = etName.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end playGame()


} // end class MainActivity