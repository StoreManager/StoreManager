package com.cottee.managerstore.manage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-21.
 */

public class StoreInfoManager {
  private Context context;
  private List<String > list;
    public StoreInfoManager(Context context) {
        this.context = context;
    }

    private String toast_storeName="请输入您店铺的名字";
  private String toast_storeStyle="请选择您店铺的类型";
  private String toast_storeAdress="请定位您店铺的地址";
  private String toast_storePhoneNumber="请输入您店铺的联系电话";
  private String toast_storeBusinessLicense="请上传您店铺的营业执照";
  public boolean infoIsEmpty(String storeName,String storeStyle,String storeAddress,
                             String storePhoneNumber,Drawable state)
  {
      if (TextUtils.isEmpty(storeName)) {
          Toast.makeText(context, toast_storeName, Toast.LENGTH_SHORT).show();
          return false;
      }else if (TextUtils.isEmpty(storeStyle))
      {
          Toast.makeText(context, toast_storeStyle, Toast.LENGTH_SHORT).show();
          return false;
      }else if (TextUtils.isEmpty(storeAddress))
      {
          Toast.makeText(context, toast_storeAdress, Toast.LENGTH_SHORT).show();
          return false;
      }else if (TextUtils.isEmpty(storePhoneNumber))
      {
          Toast.makeText(context, toast_storePhoneNumber, Toast.LENGTH_SHORT).show();
          return false;
      }else if (state==null){
          Toast.makeText(context, toast_storeBusinessLicense, Toast.LENGTH_SHORT).show();
          return false;
      }
      new ArrayList<String >();
      for (int i=0;i<list.size();i++)
      {
          list.add("AA"+i);
      }
      Toast.makeText(context, "完成信息填写！", Toast.LENGTH_SHORT).show();

      return true;
  }
}
