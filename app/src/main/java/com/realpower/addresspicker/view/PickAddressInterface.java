package com.realpower.addresspicker.view;

import com.realpower.addresspicker.bean.AddressBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public interface PickAddressInterface {
    abstract void onOkClick(String name, List<AddressBean.CityChildsBean.CountyChildsBean.StreetChildsBean> beans);
    abstract void onCancelClick();
}
