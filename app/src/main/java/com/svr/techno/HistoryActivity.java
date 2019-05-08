package com.svr.techno;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Fragment fragment = new fragmentHistory();

        fragmentChanger(fragment);
    }


    public void fragmentChanger (Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.history_fragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        //Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.vp_main);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        else {
            finish();
        }
    }

}