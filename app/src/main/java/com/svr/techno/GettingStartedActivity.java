package com.svr.techno;


import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GettingStartedActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String desk1 = "";
        String desk2 = "";

        addSlide(AppIntro2Fragment.newInstance("", desk1, R.drawable.logo, getResources().getColor(R.color.bsWhite)));
        addSlide(AppIntro2Fragment.newInstance("", desk2, R.drawable.slide1, getResources().getColor(R.color.slide2)));

        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS}, 2);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
