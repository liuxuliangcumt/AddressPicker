package com.realpower.addresspicker;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.realpower.addresspicker.bean.AddressBean;
import com.realpower.addresspicker.util.DataHelper;
import com.realpower.addresspicker.view.PickAddressInterface;
import com.realpower.addresspicker.view.PickAddressThreeView;
import com.realpower.addresspicker.view.PickAddressView;
import com.realpower.addresspicker.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级联动
 */
public class ThreePickerActivity extends AppCompatActivity {
    private List<AddressBean> addressBeans;
    private PickAddressThreeView threePick;
    private LinearLayout ll_chose, ll_street, ll_wheels;
    private TextView tv_cityAddress, tv_streetAddress;
    private List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();
    private WheelView street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_picker);
        tv_cityAddress = (TextView) findViewById(R.id.tv_cityAddress);
        tv_streetAddress = (TextView) findViewById(R.id.tv_streetAddress);
        ll_wheels = (LinearLayout) findViewById(R.id.ll_wheels);
        street = (WheelView) findViewById(R.id.street);
        ll_street = (LinearLayout) findViewById(R.id.ll_street);
        threePick = (PickAddressThreeView) findViewById(R.id.threePick);
        ll_chose = (LinearLayout) findViewById(R.id.ll_chose);
        ll_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threePick.setVisibility(View.VISIBLE);
                ll_wheels.setVisibility(View.GONE);
            }
        });

        addressBeans = DataHelper.getAddress(this);
        threePick.setData(addressBeans);
        threePick.setOnTopClicklistener(new PickAddressInterface() {

            @Override
            public void onOkClick(String name, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans) {
                tv_cityAddress.setText(name);
                streetChildsBeans = beans;
                ArrayList<String> streetData = new ArrayList<>();
                for (int i = 0; i < streetChildsBeans.size(); i++) {
                    streetData.add(streetChildsBeans.get(i).getName());
                }
                street.resetData(streetData);
                threePick.setVisibility(View.GONE);
            }

            @Override
            public void onCancelClick() {
                threePick.setVisibility(View.GONE);
            }
        });
        ll_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (streetChildsBeans.size() != 0) {
                    ll_wheels.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ThreePickerActivity.this, "shuz大小" + streetChildsBeans.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        street.setData(new ArrayList<String>());
        street.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(final int id, String text) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tv_streetAddress.setText(streetChildsBeans.get(id).getName());
                        ll_wheels.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }
}
