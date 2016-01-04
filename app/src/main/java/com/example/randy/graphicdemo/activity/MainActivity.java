package com.example.randy.graphicdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.randy.graphicdemo.R;

public class MainActivity extends AppCompatActivity {

    //private RainbowTextView rainbowTextView;

    private Button spotlightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //rainbowTextView = (RainbowTextView)findViewById(R.id.linear_gradient_txt);
        /**
         * BitmapShader
         */
        TextView textView = (TextView)findViewById(R.id.textview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cheetah_tile);
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        //Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);

        /**
         * SpotlightView
         */
       /* spotlightBtn = (Button)findViewById(R.id.spotlight_btn);
        spotlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SpotlightActivity.class);
                startActivity(intent);
            }
        });*/

    }


    /*public void spotLight(){
        Intent intent = new Intent(MainActivity.this, SpotlightActivity.class);
        startActivity(intent);
    }*/

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

        if(id == R.id.action_spotlight){

            Intent intent = new Intent(MainActivity.this, SpotlightActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_ColorMatrix){
            Intent intent = new Intent(MainActivity.this, ColorMatrixActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_fourcolor){
            Intent intent = new Intent(MainActivity.this, FourColorActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_porterduff){
            Intent intent = new Intent(MainActivity.this, PorterDuffActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_hollow){
            Intent intent = new Intent(MainActivity.this, HollowTextActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_Emboss){
            Intent intent = new Intent(MainActivity.this, EmbossMaskFilterActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_blur){
            Intent intent = new Intent(MainActivity.this, BlurMaskFilterActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_renderscript){
            Intent intent = new Intent(MainActivity.this, RenderscriptBlurActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_circle){
            Intent intent = new Intent(MainActivity.this, CircleImageViewActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
