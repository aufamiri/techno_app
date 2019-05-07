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

        String desk1 = "Slide 1";
        String desk2 = "Slide 2";
        String desk3 = "Slide 3";

        addSlide(AppIntro2Fragment.newInstance("IT'S FREE!", desk1, R.drawable.gettingstarteds1, R.color.bgGreenLight));
        addSlide(AppIntro2Fragment.newInstance("REALIZE YOUR DREAM!", desk2, R.drawable.gettingstarteds2, R.color.bgGreenDark));
        addSlide(AppIntro2Fragment.newInstance("ENJOY!", desk3, R.drawable.gettingstarteds3, R.color.bgRedLight));

        showSkipButton(false);
        showStatusBar(false);
        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 3);

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
