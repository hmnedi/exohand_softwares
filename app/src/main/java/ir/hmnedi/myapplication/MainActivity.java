package ir.hmnedi.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView loadings = (ImageView)findViewById(R.id.loadingImgView);
        AnimationDrawable animation = (AnimationDrawable)loadings.getDrawable();
        animation.start();


        // hiding the action bar and going fullscreen
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // delay and opening a new activity, also deleting the previous activity
        // TODO: animate the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                animation.stop();
                finish();
            }
        },4000);
    }
}