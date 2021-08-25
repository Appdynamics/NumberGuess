package com.example.numberguess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GuessNumberActivity extends AppCompatActivity {

    public static final String TAG = "NUMGUESS.GuessAct";   // tag for logger

    // message handler constants for switch statement
    private static final int MSG_GET_RANDOM_NUM  = 1;
    private static final int MSG_GUESS_RESULT    = 2;
    private static final int MSG_FAIL_GET_RAND   = 3;

    private final Handler mHandler = handlerSetup();
    private int theAnswer = 10; // "sane" default value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d( TAG, "onCreate(): BEGIN" );
        setContentView(R.layout.activity_guess_number);
        Log.d( TAG, "onCreate(): BEGIN" );
    } // end onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        Log.d( TAG, "onStart(): BEGIN" );

        getRandomNumber();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView tvPlayer = findViewById(R.id.tvPlayerName);
        tvPlayer.setText(message);

        Log.d( TAG, "onStart():   END" );
    } // end onStart()

    public void pushButton(View view) {
        Button btn = findViewById(view.getId());
        btn.setEnabled(false);
        Message msg = Message.obtain( mHandler, MSG_GUESS_RESULT, btn.getText().toString() );
        mHandler.sendMessage( msg );
    } // end pushButton()


    // HELPER METHODS


    private Handler handlerSetup() {
        return ( new Handler( Looper.getMainLooper() ) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d( TAG, "handleMessage(): BEGIN" );

                String text = (String) msg.obj;
                Log.d(TAG, "\t" + text);
                TextView tvMsg = findViewById(R.id.tvNumber);
                switch ( msg.what ) {
                    case MSG_GET_RANDOM_NUM:
                        tvMsg.setText(text);
                        TableLayout tl = findViewById(R.id.layoutButtons);
                        tl.setEnabled(true);
                        Log.d(TAG, "Ending Number: " + theAnswer);
                        break;
                    case MSG_GUESS_RESULT:
                        Stats.guessTotal++;
                        int guess = Integer.parseInt(text);
                        processGuess(guess);
                        break;
                    case MSG_FAIL_GET_RAND:
                        tvMsg.setText(text);
                        Context ctx = getApplicationContext();
                        Toast tst = Toast.makeText( ctx,
                                text,
                                Toast.LENGTH_LONG );
                        tst.show();
                        break;
                } // end switch

                Log.d( TAG, "handleMessage():   END" );
            } // end handleMEssage()
        } // end new Handler object
        );
    } // end handlerSetup()


    private void processGuess( int guess ) {
        CharSequence result;
        if (guess == theAnswer) {
            result = "You won!";
            Stats.gamesPlayed++;
            finish();
        }
        else if (guess < theAnswer)
            result = "Too low!";
        else
            result = "Too high!";
        TextView tvGR = findViewById(R.id.tvGuessResult);
        tvGR.setText(result);
        Context ctx = getApplicationContext();
        Toast tst = Toast.makeText(ctx, result, Toast.LENGTH_SHORT);
        tst.show();
    } // end processGuess()


    private String fetchURL() throws Exception {

        Log.d( TAG, "fetchURL(): BEGIN" );

        URL url = new URL( "http://192.168.4.254:8080/" );
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        InputStream is = new BufferedInputStream( httpCon.getInputStream() );
        byte[] buf =  new byte[512];
        int len = is.read(buf);
        is.close();
        httpCon.disconnect();
        String result = null;
        if ( len > 0 ) {
            result = new String(buf, 0, len);
        }

        Log.d( TAG, "fetchURL():   END" );

        return(result);

    } // end fetchURL()


    private void getRandomNumber() {

        Runnable getHTML = new Runnable() {
            @Override
            public void run() {

                try {
                    String result = fetchURL();
                    theAnswer = Integer.parseInt(result);
                    Log.d( TAG, "\tgetRandomNumber(): rand num = " + theAnswer);
                    Message msg = Message.obtain( mHandler, MSG_GET_RANDOM_NUM, result );
                    mHandler.sendMessage( msg );
                } // end try
                catch (NumberFormatException nfe) {
                    Log.e( TAG, nfe.getMessage() );
                    theAnswer = 10; // reset answer to sane default
                }
                catch (Exception e) {
                    Log.e( TAG, "\t" + e.getMessage() );
                    // we failed to get a new random number
                    Message msg = Message.obtain( mHandler, MSG_FAIL_GET_RAND, e.getMessage() );
                    mHandler.sendMessage( msg );
                } // end catch

            } // end run()
        }; // end new Runnable object

        Thread t = new Thread( getHTML );
        t.setPriority(Process.THREAD_PRIORITY_BACKGROUND );
        Log.d( TAG, "\tgetRandomNumber(): start num = " + theAnswer);
        t.start();

    } // end getRandomNumber()


} // end class GuessNumberActivity