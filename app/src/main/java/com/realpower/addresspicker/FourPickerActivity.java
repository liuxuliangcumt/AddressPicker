package com.realpower.addresspicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.realpower.addresspicker.view.PickAddressInterface;
import com.realpower.addresspicker.view.PickAddressView;

/**
 * 四级联动
 */
public class FourPickerActivity extends AppCompatActivity {
    private PickAddressView fourPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_picker);
        fourPicker = (PickAddressView) findViewById(R.id.fourPicker);
        fourPicker.setOnTopClicklistener(new PickAddressInterface() {
            @Override
            public void onOkClick(String name) {

            }

            @Override
            public void onCancelClick() {

            }
        });
    }
}
