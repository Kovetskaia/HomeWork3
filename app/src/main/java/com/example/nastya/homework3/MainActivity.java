package com.example.nastya.homework3;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Chip chip0, chip1, chip2, chip3, chip10, chip11, chip12, chip13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chip0 = findViewById(R.id.chip0);
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip10 = findViewById(R.id.chip10);
        chip11 = findViewById(R.id.chip11);
        chip12 = findViewById(R.id.chip12);
        chip13 = findViewById(R.id.chip13);

        chip0.setOnCloseIconClickListener(this);
        chip1.setOnCloseIconClickListener(this);
        chip2.setOnCloseIconClickListener(this);
        chip3.setOnCloseIconClickListener(this);
        chip10.setOnCloseIconClickListener(this);
        chip11.setOnCloseIconClickListener(this);
        chip12.setOnCloseIconClickListener(this);
        chip13.setOnCloseIconClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MyLayout myLayout = findViewById(R.id.ml);
        MyLayout myLayout1 = findViewById(R.id.ml1);

        if (myLayout.indexOfChild(view) != -1) {
            myLayout.removeView(view);
            myLayout1.addView(view);
        } else {
            myLayout1.removeView(view);
            myLayout.addView(view);
        }

    }

}
