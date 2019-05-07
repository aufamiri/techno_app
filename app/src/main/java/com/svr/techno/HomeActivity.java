package com.svr.techno;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Window;

public class HomeActivity extends AppCompatActivity {

    public RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.content_home_toolbar);
        setSupportActionBar(toolbar);
        //will hide the title

        fragmentChanger(new HomeFragment());
    }

    public void fragmentChanger (Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_content, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        else {

            finish();

        }
    }
}
