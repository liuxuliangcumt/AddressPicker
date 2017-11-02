package com.realpower.addresspicker;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void four(View view) {
        Intent intent = new Intent(this, FourPickerActivity.class);
        startActivity(intent);
    }

    public void three(View view) {
        Intent intent = new Intent(this, ThreePickerActivity.class);
        startActivity(intent);

    }
}
