package project.vichita.testaudiostreaming;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    final String URL = "http://api.aforce.io/dadsa.mp3";
    io.vov.vitamio.MediaPlayer player;
    Button playBtn, halfBtn, oneBtn, oneHalfBtn, twoBtn;
    SeekBar progressBar;

    boolean isPlaying = false;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.activity_main);
        playBtn = (Button) findViewById(R.id.play);
        halfBtn = (Button) findViewById(R.id.halfx);
        oneBtn = (Button) findViewById(R.id.onex);
        oneHalfBtn = (Button) findViewById(R.id.onehalfx);
        twoBtn = (Button) findViewById(R.id.twox);
        progressBar = (SeekBar) findViewById(R.id.seekbar);


        rate = 1f;

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        player = new io.vov.vitamio.MediaPlayer(this);
        try {
            player.setDataSource(URL);
            player.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(io.vov.vitamio.MediaPlayer mediaPlayer) {
                    player.start();
                    Log.d("Music", "start");
                    isPlaying = true;
                    playBtn.setText("Pause");

                    progressBar.setProgress(0);
                    progressBar.setMax((int) mediaPlayer.getDuration());
                }
            });
            player.setOnBufferingUpdateListener(new io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(io.vov.vitamio.MediaPlayer mediaPlayer, int i) {
                    progressBar.setSecondaryProgress(i);
                }
            });
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        halfBtn.setOnClickListener(new RateOnClick());
        oneBtn.setOnClickListener(new RateOnClick());
        oneHalfBtn.setOnClickListener(new RateOnClick());
        twoBtn.setOnClickListener(new RateOnClick());

        /*progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    long seekto = progress;
                    Log.d("Music","seek to:"+seekto);
                    player.seekTo(seekto);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        seekUpdation();
    }

    Handler seekHandler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        progressBar.setProgress((int) player.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }


    private class RateOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.halfx)
                rate = 0.5f;
            else if (v.getId() == R.id.onex)
                rate = 1f;
            else if (v.getId() == R.id.onehalfx)
                rate = 1.5f;
            else if (v.getId() == R.id.twox)
                rate = 2f;

            changeRate();
        }
    }

    private void play() {
        if (isPlaying) {
            isPlaying = false;
            playBtn.setText("Play");
            player.pause();
        } else {
            isPlaying = true;
            playBtn.setText("Pause");
            player.start();
        }
    }

    private void changeRate() {
        player.setPlaybackSpeed(rate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
