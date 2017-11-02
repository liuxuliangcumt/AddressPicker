package com.realpower.addresspicker.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.realpower.addresspicker.R;
import com.realpower.addresspicker.bean.AddressBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxuliangcumt on 2017/10/30.
 */

public class PickAddressView extends LinearLayout {
    /**
     * 与选择地址相关
     */
    protected ArrayList<String> mProvinceDatas = new ArrayList<>();

    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mCountyPicker;
    private WheelView mStreetPicker;
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName;
    protected String mCurrentStreetName;
    private TextView cancel, ok;
    protected boolean isDataLoaded = false;
    private Context context;
    private PickAddressInterface pickAddressInterface;

    public PickAddressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.address, this);
        this.context = context;
        initData();
    }

    public PickAddressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void initData() {
        mProvincePicker = (WheelView) findViewById(R.id.province);
        mCityPicker = (WheelView) findViewById(R.id.city);
        mCountyPicker = (WheelView) findViewById(R.id.county);
        mStreetPicker = (WheelView) findViewById(R.id.street);
        cancel = (TextView) findViewById(R.id.box_cancel);
        ok = (TextView) findViewById(R.id.box_ok);

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = new ArrayList<>();
                    cityChildsBeans = addressBeanList.get(id).getChilds();
                    for (int i = 0; i < cityChildsBeans.size(); i++) {
                        mCityData.add(cityChildsBeans.get(i).getName());
                    }
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    mCurrentCityName = mCityData.get(0);

                    ArrayList<String> mCountyData = new ArrayList<>();
                    countyChildsBeans = cityChildsBeans.get(0).getChilds();
                    for (int i = 0; i < countyChildsBeans.size(); i++) {
                        mCountyData.add(countyChildsBeans.get(i).getName());
                    }
                    mCountyPicker.resetData(mCountyData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);

                    ArrayList<String> mStreetData = new ArrayList<>();
                    streetChildsBeans = countyChildsBeans.get(0).getChilds();
                    for (int i = 0; i < streetChildsBeans.size(); i++) {
                        mStreetData.add(streetChildsBeans.get(i).getName());
                    }
                    mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);


                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCountyData = new ArrayList<>();
                countyChildsBeans = cityChildsBeans.get(id).getChilds();
                for (int i = 0; i < countyChildsBeans.size(); i++) {
                    mCountyData.add(countyChildsBeans.get(i).getName());
                }
                mCountyPicker.resetData(mCountyData);
                mCountyPicker.setDefault(0);
                mCurrentDistrictName = mCountyData.get(0);

                streetChildsBeans = countyChildsBeans.get(0).getChilds();
                ArrayList<String> mStreetData = new ArrayList<>();
                for (int i = 0; i < streetChildsBeans.size(); i++) {
                    mStreetData.add(streetChildsBeans.get(i).getName());
                }
                mStreetPicker.resetData(mStreetData);
                mStreetPicker.setDefault(0);
                mCurrentStreetName = mStreetData.get(0);

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String currentname = countyChildsBeans.get(id).getName();
                if (!mCurrentDistrictName.equals(currentname)) {
                    mCurrentDistrictName = currentname;

                    streetChildsBeans = countyChildsBeans.get(id).getChilds();
                    ArrayList<String> mStreetData = new ArrayList<>();
                    for (int i = 0; i < streetChildsBeans.size(); i++) {
                        mStreetData.add(streetChildsBeans.get(i).getName());
                    }
                    mStreetPicker.resetData(mStreetData);
                    mStreetPicker.setDefault(0);
                    mCurrentStreetName = mStreetData.get(0);

                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });


        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickAddressInterface != null) {
                    pickAddressInterface.onOkClick(mProvinceDatas.get(mProvincePicker.getSelected())
                            + mCityPicker.getSelectedText()
                            + mCountyPicker.getSelectedText());
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickAddressInterface != null) {
                    pickAddressInterface.onCancelClick();
                }
            }
        });

        // 启动线程读取数据
        new Thread() {
            @Override
            public void run() {
                // 读取数据
                isDataLoaded = readAddrDatas();

                // 通知界面线程
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cityChildsBeans.clear();

                        for (int i = 0; i < addressBeanList.size(); i++) {
                            mProvinceDatas.add(addressBeanList.get(i).getName());
                        }
                        mProvincePicker.setData(mProvinceDatas);
                        mProvincePicker.setDefault(0);
                        mCurrentProviceName = mProvinceDatas.get(0);

                        cityChildsBeans.addAll(addressBeanList.get(0).getChilds());
                        ArrayList<String> mCityData = new ArrayList<>();
                        for (int i = 0; i < cityChildsBeans.size(); i++) {
                            mCityData.add(cityChildsBeans.get(i).getName());
                        }
                        mCityPicker.setData(mCityData);
                        mCityPicker.setDefault(0);
                        mCurrentCityName = mCityData.get(0);

                        countyChildsBeans = cityChildsBeans.get(0).getChilds();
                        ArrayList<String> mDistrictData = new ArrayList<>();
                        for (int i = 0; i < countyChildsBeans.size(); i++) {
                            mDistrictData.add(countyChildsBeans.get(i).getName());
                        }
                        mCountyPicker.setData(mDistrictData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mDistrictData.get(0);

                        streetChildsBeans = countyChildsBeans.get(0).getChilds();
                        ArrayList<String> mStreetData = new ArrayList<>();
                        for (int i = 0; i < streetChildsBeans.size(); i++) {
                            mStreetData.add(streetChildsBeans.get(i).getName());
                        }
                        mStreetPicker.setData(mStreetData);
                        mStreetPicker.setDefault(0);
                        mCurrentStreetName = mStreetData.get(0);


                    }
                }, 100);
            }
        }.start();

    }

    /**
     * 读取地址数据，请使用线程进行调用
     *
     * @return
     */
    List<AddressBean> addressBeanList;
    List<AddressBean.CityChildsBean> cityChildsBeans = new ArrayList<>();
    List<AddressBean.CityChildsBean.CountyChildsBean> countyChildsBeans = new ArrayList<>();
    List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> streetChildsBeans = new ArrayList<>();

    protected boolean readAddrDatas() {


        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(context.getAssets().open(
                    "address.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<AddressBean>>() {
            }.getType();
            addressBeanList = gson.fromJson(stringBuilder.toString(), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setOnTopClicklistener(PickAddressInterface pickAddressInterface) {
        this.pickAddressInterface = pickAddressInterface;
    }

}
