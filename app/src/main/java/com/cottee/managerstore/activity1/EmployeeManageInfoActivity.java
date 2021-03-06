package com.cottee.managerstore.activity1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.TextView;

import com.cottee.managerstore.R;
import com.cottee.managerstore.bean.SingleEmployeeInfo;
import com.cottee.managerstore.bean.viewdata.LineChartData;
import com.cottee.managerstore.handle.oss_handler.OssHandler;
import com.cottee.managerstore.httputils.Https;
import com.cottee.managerstore.properties.Properties;
import com.cottee.managerstore.utils.ToastUtils;
import com.cottee.managerstore.utils.myt_oss.ConfigOfOssClient;
import com.cottee.managerstore.utils.myt_oss.DownloadUtils;
import com.cottee.managerstore.view.ImageViewExtend;
import com.cottee.managerstore.widget.LineChart;
import com.cottee.managerstore.widget.Title;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/27.
 */

public class EmployeeManageInfoActivity extends Activity {

    private Title title;
    private LineChart mWeekLineChart;
    private String[] mWeekItems = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private int[] mWeekPoints = new int[7];
    private List<LineChartData> mWeekList = new ArrayList<>();

    private EmployeeInfoHandle handler = new EmployeeInfoHandle();
    private TextView tv_emp_info_name;
    private TextView tv_emp_info_sex;
    private TextView tv_emp_info_birth;
    private TextView tv_emp_info_phone;
    private ImageViewExtend imv_emp_header;
    private int [] empAllTime = new int[7];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);
        Intent intent = getIntent();
        String emp_id = intent.getStringExtra("EMP_ID");
        initTitle();
        intiView();
        sendRequest(emp_id);




    }


    private void sendRequest(final String empId) {
        new Thread() {
            @Override
            public void run() {

                    Https.sendSessionAndFieldOkHttpRequest(Properties.EMPLOYEE_INFO_GSON_PATH, "staff_id", empId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responeData = response.body().string();
                            System.out.println("员工具体信息 Json:" + responeData);
                            if(responeData.trim().equals("250")){
                                ToastUtils.showToast(EmployeeManageInfoActivity.this,"session无效效，正在重新登陆请稍等" );

                            /*sendRequestWithOkHttp();*/
                            }
                            else if (responeData.trim().equals("0")){

                            }
                            else {

                                parseJSONWithGSON(responeData);
                            }
                        }
                    });

            }
        }.start();

    }

    private void parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        SingleEmployeeInfo empInfo = gson.fromJson(jsonData, SingleEmployeeInfo.class);
       /* List<SingleEmployeeInfo> empInfo = (List<SingleEmployeeInfo>) singleEmployeeInfo;*/
        System.out.println("员工信息外部数据："+empInfo);
       /* System.out.println("员工信息时间数据："+timeInfo);*/
        Message message = new Message();
        message.what = Properties.EMPLOYEE_INFO;
        message.obj = empInfo;



        handler.sendMessage(message);

    }

    public class EmployeeInfoHandle extends Handler {

        private String empName;
        private String empSex;
        private String empBirth;
        private String empPhone;
        private String[] empTime;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Properties.EMPLOYEE_INFO:
                    SingleEmployeeInfo empInfo = (SingleEmployeeInfo) msg.obj;


                    empName = empInfo.getName();
                    empSex = empInfo.getSex();
                    empBirth = empInfo.getBirth();
                    empPhone = empInfo.getPhone_number();
                    empTime = empInfo.getTime_list();
                    String empPhoto = empInfo.getPhoto();
                    OssHandler ossHandler = new OssHandler(EmployeeManageInfoActivity.this,imv_emp_header);
                    File cache_image = new File(getCacheDir(), Base64.encodeToString(empPhoto.getBytes(), Base64.DEFAULT));
                    System.out.println("员工的具体图片缓存："+cache_image);
                    DownloadUtils.downloadFileFromOss(cache_image, ossHandler, ConfigOfOssClient.BUCKET_NAME, empPhoto);
                    System.out.println("单个员工名字："+ empName);
                        System.out.println("单个员工性别："+ empSex);
                        System.out.println("单个员工生日："+ empBirth);
                        System.out.println("单个员工电话："+ empPhone);

                        for(int i=0;i<empTime.length;i++){
                            mWeekPoints[i]=Integer.parseInt(empTime[i]);

                        }
                    tv_emp_info_name.setText(empName);
                    tv_emp_info_sex.setText(empSex);
                    tv_emp_info_birth.setText(empBirth);
                    tv_emp_info_phone.setText(empPhone);
                    for (int i = 0; i < mWeekItems.length; i++) {
                        LineChartData data = new LineChartData();
                        data.setItem(mWeekItems[i]);
                        data.setPoint(mWeekPoints[i]);
                        mWeekList.add(data);
                    }
                    mWeekLineChart.setData(mWeekList);

                   /* if (!adapter.isEmpty()) {
                        ll_empty.setVisibility(View.GONE);

                    } else {


                        ll_empty.setVisibility(View.VISIBLE);
                    }*/



                    break;
            }
            super.handleMessage(msg);
        }
    }





    private void intiView() {
        mWeekLineChart = (LineChart) findViewById(R.id.line_chart_week);
        tv_emp_info_name = (TextView) findViewById(R.id.tv_emp_info_name);
        tv_emp_info_sex = (TextView) findViewById(R.id.tv_emp_info_sex);
        tv_emp_info_birth = (TextView) findViewById(R.id.tv_emp_info_birth);
        tv_emp_info_phone = (TextView) findViewById(R.id.tv_emp_info_phone);
        imv_emp_header = (ImageViewExtend) findViewById(R.id.imv_emp_header);
        imv_emp_header.setmDrawShapeType(ImageViewExtend.SHAPE_CIRCLE);
        /*imv_emp_header.setImageResource(R.mipmap.img_coffee);*/
    }

    private void initTitle(){
        title = (Title)findViewById(R.id.title);
        title.setTitleNameStr("员工个人信息");
        title.setTheme(Title.TitleTheme.THEME_TRANSLATE);
        title.mSetButtonInfo(new Title.ButtonInfo(true, Title
                .BUTTON_LEFT, R.mipmap.back_2x,null
        ));
        //可加button1
        title.mSetButtonInfo(new Title.ButtonInfo(true, Title
                .BUTTON_RIGHT1, 0,
                "删除员工"));
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_RIGHT1:
                        break;
                    case Title.BUTTON_LEFT:
                        finish();
                        break;
                }
            }
        });

    }
}
