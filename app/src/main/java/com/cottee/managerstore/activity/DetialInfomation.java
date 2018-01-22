package com.cottee.managerstore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.cottee.managerstore.Filter.CashierInputFilter;
import com.cottee.managerstore.R;
import com.cottee.managerstore.bean.StoreInfo;
import com.cottee.managerstore.handle.LoginRegisterInformationHandle;
import com.cottee.managerstore.manage.StoreInfoManager;
import com.cottee.managerstore.manage.SubmitStoreInfoManager;
import com.cottee.managerstore.utils.ToastUtils;

import java.io.File;
import java.util.Calendar;

/**
 * Created by user on 2017/12/12.
 */

public class DetialInfomation extends Activity implements View.OnClickListener {
    private static final int AM_TIME = 1;
    private static final int PM_TIME = 2;
    private TextView tv_storeName;
    private Button btn_edit;
    private Button btn_save;
    private TextView tv_timeAM;
    private Button btn_timeAM;
    private TextView tv_sign;
    private EditText et_sign;
    private TextView tv_money;
    private EditText et_money;
    private TextView tv_phone;
    private EditText et_phone;
    private TextView tv_style;
    private TextView tv_address;
    private ImageView iv_photo1;
    private ImageView iv_photo2;
    private ImageView iv_photo3;
    private String sign;
    private String timeAM;
    private String timePM;
    private String money;
    private String phone;
    private StoreInfo storeInfo;
    private Bitmap bitmap;
    private ImageView iv_surface;
    private String surface;
    private String one;
    private String two;
    private String three;
    public Context mContext = DetialInfomation.this;
    private TextView tv_timePM;
    private Button btn_timePM;
    private ToggleButton btn_order;
    private boolean reserve = false;
    private Drawable on;
    private Drawable off;
    private String time;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detialinfomation );
        Resources resources = this.getResources();
        on = resources.getDrawable( R.mipmap.turnon );
        off = resources.getDrawable( R.mipmap.turnoff );
        Intent intent = getIntent();
        storeInfo = (StoreInfo) intent.getSerializableExtra( "storeInfo" );
        findView();
        surface = storeInfo.getSurface();
        one = storeInfo.getThumbnail_one();
        two = storeInfo.getThumbnail_two();
        three = storeInfo.getThumbnail_three();
        reserve = storeInfo.isReserve();
        if (reserve==true) {
            btn_order.setChecked( true );
            btn_order.setBackground( on );
        } else {
            btn_order.setChecked( false );
            btn_order.setBackground( off );
        }
        Glide.with( this ).load( surface ).into( iv_surface );
//        Glide.with( this ).load( one ).into( iv_photo1 );
//        Glide.with( this ).load( two ).into( iv_photo2 );
//        Glide.with( this ).load( three ).into( iv_photo3 );
        time = storeInfo.getBusiness_hours();
        if (time != null) {
            String[] split = time.split( "-" );
            timeAM = split[0];
            timePM = split[1];
            tv_timeAM.setText( timeAM );
            tv_timePM.setText( timePM );
        }
        //不可更改
        tv_storeName.setText( storeInfo.getName() );
        tv_address.setText( storeInfo.getAddress() );
        tv_style.setText( storeInfo.getClassify() );

        //更改
        sign = storeInfo.getIntroduce();
        tv_sign.setText( sign );
        phone = storeInfo.getPhone();
        tv_phone.setText( phone );
        money = storeInfo.getAvecon();
        tv_money.setText( money );

        btn_order.setOnCheckedChangeListener( new OnChangeListener());
    }

    private void findView() {

        tv_storeName = (TextView) findViewById( R.id.tv_storeName );

        btn_edit = (Button) findViewById( R.id.btn_edit );
        btn_save = (Button) findViewById( R.id.btn_save );

        tv_timeAM = (TextView) findViewById( R.id.tv_timeAM );
        btn_timeAM = (Button) findViewById( R.id.et_timeAM );

        tv_timePM = (TextView) findViewById( R.id.tv_timePM );
        btn_timePM = (Button) findViewById( R.id.et_timePM );

        tv_sign = (TextView) findViewById( R.id.tv_sign );
        et_sign = (EditText) findViewById( R.id.et_sign );

        tv_money = (TextView) findViewById( R.id.tv_money );
        et_money = (EditText) findViewById( R.id.et_money );

        tv_phone = (TextView) findViewById( R.id.tv_phone );
        et_phone = (EditText) findViewById( R.id.et_phone );

        tv_style = (TextView) findViewById( R.id.tv_style );
        tv_address = (TextView) findViewById( R.id.tv_address );

        iv_surface = (ImageView) findViewById( R.id.iv_surface );
        iv_surface.setOnClickListener( this );
        iv_photo1 = (ImageView) findViewById( R.id.iv_photo1 );
        iv_photo1.setOnClickListener( this );
        iv_photo2 = (ImageView) findViewById( R.id.iv_photo2 );
        iv_photo2.setOnClickListener( this );
        iv_photo3 = (ImageView) findViewById( R.id.iv_photo3 );
        iv_photo3.setOnClickListener( this );

        btn_back = (Button) findViewById( R.id.btn_backs );
        btn_back.setOnClickListener( this );

        btn_timeAM.setOnClickListener( this );
        btn_timePM.setOnClickListener( this );

        btn_order = (ToggleButton) findViewById( R.id.btn_order );
        CashierInputFilter[] filters = {new CashierInputFilter()};
        et_money.setFilters( filters );

    }

    public void edit(View view) {
        sign = tv_sign.getText().toString().trim();
        timeAM = tv_timeAM.getText().toString().trim();
        timePM = tv_timePM.getText().toString().trim();
        money = tv_money.getText().toString().trim();
        phone = tv_phone.getText().toString().trim();

        tv_sign.setVisibility( View.GONE );
        et_sign.setVisibility( View.VISIBLE );

        tv_timeAM.setVisibility( View.GONE );
        btn_timeAM.setVisibility( View.VISIBLE );

        tv_timePM.setVisibility( View.GONE );
        btn_timePM.setVisibility( View.VISIBLE );

        tv_money.setVisibility( View.GONE );
        et_money.setVisibility( View.VISIBLE );

        tv_phone.setVisibility( View.GONE );
        et_phone.setVisibility( View.VISIBLE );

        btn_edit.setVisibility( View.GONE );
        btn_save.setVisibility( View.VISIBLE );


        et_sign.setText( sign );
        btn_timeAM.setText( timeAM );
        btn_timePM.setText( timePM );
        et_money.setText( money );
        et_phone.setText( phone );

    }

    public void save(View view) {
        sign = et_sign.getText().toString().trim();
        timeAM = btn_timeAM.getText().toString().trim();
        timePM = btn_timePM.getText().toString().trim();
        money = et_money.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        time = timeAM + "-" + timePM;
        submit();
    }

    private void submit() {
        Drawable background = btn_order.getBackground();
        if (background == on) {
            reserve = true;
        } else {
            reserve = false;
        }
        boolean mobileNo = isMobileNo( phone );
        boolean phoneNo = isTelePhoneNo( phone );
        boolean octNumber = isOctNumber( money );
        if (octNumber == false) {
            ToastUtils.showToast( this, "金额格式有误哦" );
            return;
        }

        if (mobileNo == true || phoneNo == true) {
            et_sign.setVisibility( View.GONE );
            tv_sign.setVisibility( View.VISIBLE );

            btn_timeAM.setVisibility( View.GONE );
            tv_timeAM.setVisibility( View.VISIBLE );

            btn_timePM.setVisibility( View.GONE );
            tv_timePM.setVisibility( View.VISIBLE );

            et_money.setVisibility( View.GONE );
            tv_money.setVisibility( View.VISIBLE );

            et_phone.setVisibility( View.GONE );
            tv_phone.setVisibility( View.VISIBLE );

            btn_edit.setVisibility( View.VISIBLE );
            btn_save.setVisibility( View.GONE );

            tv_sign.setText( sign );
            tv_timeAM.setText( timeAM );
            tv_timePM.setText( timePM );
            tv_money.setText( money );
            tv_phone.setText( phone );
        } else {
            ToastUtils.showToast( this, "号码输入有误哦" );
            return;
        }

        SubmitStoreInfoManager submitStoreInfo = new SubmitStoreInfoManager( this, new LoginRegisterInformationHandle(
                this, ""
        ) );
        submitStoreInfo.changeInfo( sign, time, money, String.valueOf( reserve ), phone );
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent( this, BigPhotoActivity.class );
        switch (view.getId()) {
            case R.id.iv_surface:
                Intent surfaceIntent = new Intent( this, FrontCoverActivity.class );
                surfaceIntent.putExtra( "photo_url", surface );
                startActivity( surfaceIntent );
                break;
            case R.id.iv_photo1:
                intent.putExtra( "photo_url", one );
                startActivity( intent );
                break;
            case R.id.iv_photo2:
                intent.putExtra( "photo_url", two );
                startActivity( intent );
                break;
            case R.id.iv_photo3:
                intent.putExtra( "photo_url", three );
                startActivity( intent );
                break;
            case R.id.et_timeAM:
                showTimePickerDialog( btn_timeAM, timeAM, AM_TIME );
                break;
            case R.id.et_timePM:
                showTimePickerDialog( btn_timePM, timePM, PM_TIME );
                break;
            case R.id.btn_backs:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * 时间轴
     */
    private void showTimePickerDialog(final Button button, String times, final int flag) {
        String[] split = times.split( ":" );
        final int hour = Integer.parseInt( split[0] );
        int minute = Integer.parseInt( split[1] );

        Dialog dialog = new TimePickerDialog( DetialInfomation.this,
                new TimePickerDialog.OnTimeSetListener() {

                    private String hour;
                    private String min;

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (hourOfDay < 10) {
                            hour = "0" + String.valueOf( hourOfDay );
                        } else {
                            hour = String.valueOf( hourOfDay );
                        }
                        if (minute < 10) {
                            min = "0" + String.valueOf( minute );
                        } else {
                            min = String.valueOf( minute );
                        }
                        String time = hour + ":" + min;
                        button.setText( time );
                        if (flag == AM_TIME) {
                            timeAM = time;
                        } else if (flag == PM_TIME) {
                            timePM = time;
                        }
                    }
                }, hour, minute, DateFormat.is24HourFormat( DetialInfomation.this ) );
        dialog.show();
    }

    /**
     * 手机号正则
     */
    public static boolean isMobileNo(String mobileNums) {
        String telRegex = "[1][3587]\\d{9}";
        if (TextUtils.isEmpty( mobileNums ))
            return false;
        else
            return mobileNums.matches( telRegex );
    }

    /**
     * 固定电话正则
     */
    public static boolean isTelePhoneNo(String mobileNums) {
        String telRegex = "^\\d{3,4}-\\d{7,8}$";
        if (TextUtils.isEmpty( mobileNums ))
            return false;
        else
            return mobileNums.matches( telRegex );
    }



    @Override
    public void onBackPressed() {
        if (btn_save.getVisibility() == View.VISIBLE) {
            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setMessage( "信息尚未保存，确定要返回吗？" );
            builder.setCancelable( true );
            builder.setNegativeButton( "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            } );
            builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            } );
            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
//为Window设置动画
            window.setWindowAnimations( R.style.CustomDialog );
            dialog.show();
        } else {
            finish();
        }
    }

    //十进制
    private static boolean isOctNumber(String str) {
        boolean flag = false;
        for (int i = 0, n = str.length(); i < n; i++) {
            char c = str.charAt( i );
            if (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8' | c == '9') {
                flag = true;
            }
        }

        if (str.length() > 1) {
            String num = str.substring( 0, 1 );
            if (Integer.valueOf( num ) == 0) {
                if (str.substring( 1, 2 ).equals( "." ) || str.substring( 1, 2 ) == null) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            String last = str.substring( str.length() - 1, str.length() );
            boolean equals = last.equals( "." );
            if (equals) {
                flag = false;
            }
        }
        return flag;
    }

    public class OnChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            btn_order.setBackground( b ? on : off );
        if(btn_save.getVisibility()==View.VISIBLE){
           return;
        }
        submit();
        }
    }

}
