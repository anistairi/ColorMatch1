package com.example.colormatch;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Activity_jeu extends Activity {

    private ColorMatchView colorview;
    private boolean res = false; // indique pour onResume si l'activity vient d'une pause ou non
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //--- Pour le plein écran ---
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //---------------------------
        setContentView(R.layout.activity_activity_jeu);
        colorview = (ColorMatchView) findViewById(R.id.ColorMatchView);
        colorview.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() { // permet d'indiquer à la "view" qu'il faut stopper le thread
        // TODO Auto-generated method stub
        super.onPause();
        colorview.stopthread();
        res = true;
    }

    @Override
    protected void onResume() { // si r == true on indique à la "view" si il y a eu une pause ou non
        // TODO Auto-generated method stub
        if(res == true)
            colorview.resumeGame();
        super.onResume();

    }

    @Override
    protected void onDestroy() {// stop le thread
        // TODO Auto-generated method stub
        colorview.stopthread();
        this.finish();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // menu de l'activity
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_restart:
                colorview.regame();
                return true;
            case R.id.menu_quitter:
                this.finish();
                return true;
            case R.id.menu_son:
                colorview.pause_son();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
