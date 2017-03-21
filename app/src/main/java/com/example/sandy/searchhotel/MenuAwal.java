package com.example.sandy.searchhotel;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MenuAwal extends AppCompatActivity {

    private ProgressBar spinner;
    ImageView gambar1;
    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.navigationbar);

        gambar1 = (ImageView) findViewById(R.id.icon_aplikasi);
        spinner = (ProgressBar) findViewById(R.id.progress);
        b1 = (Button) findViewById(R.id.button_search);
        b2 = (Button) findViewById(R.id.button_exit);
        spinner.setVisibility(View.GONE);
        Button btn2 = (Button) findViewById(R.id.button_exit);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        System.exit(0);
                    }
                });
        Button btn1 = (Button) findViewById(R.id.button_search);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                gambar1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                b2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent B = new Intent(MenuAwal.this, About.class);
                startActivity(B);
                // User chose the "Settings" item, show the app settings UI...
                break;

//            case R.id.refresh:
//
//               break;
        }
        return true;
    }

}
