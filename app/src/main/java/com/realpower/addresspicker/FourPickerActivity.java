package com.realpower.addresspicker;

import android.content.res.AssetManager;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.realpower.addresspicker.bean.AddressBean;
import com.realpower.addresspicker.util.DataHelper;
import com.realpower.addresspicker.view.PickAddressInterface;
import com.realpower.addresspicker.view.PickAddressView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

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
        addressBeanList = DataHelper.getAddress(this);
        fourPicker.setData(addressBeanList);
    }
    List<AddressBean> addressBeanList;
    @Override
    protected void onDestroy() {
        fourPicker.onDistory();
        addressBeanList.clear();
        addressBeanList = null;
        fourPicker.removeAllViews();
        fourPicker = null;
        super.onDestroy();
    }
}
