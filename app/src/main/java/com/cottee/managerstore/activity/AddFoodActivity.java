package com.cottee.managerstore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.DrmInitData;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.cottee.managerstore.R;
import com.cottee.managerstore.activity1.ProjectManageActivity;
import com.cottee.managerstore.bean.FoodDetail;
import com.cottee.managerstore.bean.UserRequestInfo;
import com.cottee.managerstore.handle.LoginRegisterInformationHandle;
import com.cottee.managerstore.manage.AddFoodInfoIsEmpty;
import com.cottee.managerstore.manage.ProjectTypeDetailManager;
import com.cottee.managerstore.utils.BitmapUtils;
import com.cottee.managerstore.utils.LogUtils;
import com.cottee.managerstore.utils.OssUtils;
import com.cottee.managerstore.utils.ToastUtils;
import com.cottee.managerstore.view.SelectPicPopupWindow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.cottee.managerstore.activity.FrontCoverActivity
        .readBitmapAutoSize;

public class AddFoodActivity extends Activity {
    private ImageButton imgbtn_foodImg;
    private EditText edit_foodName;
    private EditText edit_foodPrice;
    private EditText edit_foodDescription;
    private Button btn_cancel;
    private Button btn_oK;
    private SelectPicPopupWindow selectPicPopupWindow;
    private String filePath=null;
    private TextView tv_inputNumber;
    private Drawable foodImage=null;
    private int num=100;
    private int type=-1;
    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private AddFoodInfoIsEmpty addFoodInfoIsEmpty;
//    public static List<FoodDetail> foodDetailList=new ArrayList<>();
    public  List<FoodDetail> foodDetailList=ManageFoodDetailActivity.detailFoodList;
    private  String path=null;
    private Handler mHandler = new Handler();
    private ScrollView scroll_addFood;
    FoodDetail foodDetail=new FoodDetail();
    private TextView tv_moneyErro;
    private String name;
    public static String objectKey;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        userEmail = UserRequestInfo.getUserEmail();
        objectKey = "merchant"+"/"+ userEmail +"/"+"item"+"/"+new DateFormat().format( "yyyyMMdd_hhmmss",
                Calendar.getInstance( Locale.CHINA ) ) + ".jpg";
        initView();
        initEvent();

    }

    private void initView() {
        scroll_addFood = findViewById(R.id.scroll_addFood);
        imgbtn_foodImg = findViewById(R.id.imgbtn_foodImg);
        edit_foodName = findViewById(R.id.edit_foodName);
        edit_foodPrice = findViewById(R.id.edit_foodPrice);
        edit_foodDescription = findViewById(R.id.edit_foodDescription);
        tv_inputNumber = findViewById(R.id.tv_inputNumber);
        btn_cancel = findViewById(R.id.btn_cancle);
        btn_oK = findViewById(R.id.btn_ok);
        tv_moneyErro = findViewById(R.id.tv_moneyErro);
    }
    private void initEvent() {

     edit_foodName.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             mHandler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     scroll_addFood.fullScroll(View.FOCUS_DOWN);

                 }
             },100);
         }
     });
//        菜品图片调取相册和相机
        imgbtn_foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicPopupWindow =
                        new SelectPicPopupWindow(AddFoodActivity.this);
                selectPicPopupWindow.setOnClickItem(new SelectPicPopupWindow.OnClickItem() {


                    @Override
                    //拍照
                    public void click_take_photo() {
                        AlertDialog.Builder builder = new AlertDialog.Builder
                                (AddFoodActivity.this);
                        builder.setTitle("用户提示")
                                .setMessage("小主务必将手机横向拍摄！")
                                .setCancelable(false);
                        builder.setPositiveButton("偏不", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });

                        builder.setNegativeButton("好的", new DialogInterface.OnClickListener() {



                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                                startActivityForResult( intent, 1 );
                            }
                        });
                        builder.show();

                    }

                    @Override
//                    相册选取
                    public void click_pick_photo() {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,2);

                    }
                });
                selectPicPopupWindow.showAtLocation(AddFoodActivity.this.findViewById(R.id.scroll_addFood),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        //金额判断
        edit_foodPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int
                    i1, int i2) {
                String edit_price = edit_foodPrice.getText().toString().trim();
                if (edit_price.isEmpty())
                {
                    return;
                }
                boolean octNumber = DetialInfomation.isOctNumber(edit_price);
                if (octNumber)
                {
                    tv_moneyErro.setVisibility(View.GONE);
                }
                else {
                    tv_moneyErro.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//     菜品描述
        edit_foodDescription.addTextChangedListener(new TextWatcher() {

            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int
                    i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {
                wordNum= charSequence;//实时记录输入的字数

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = num - editable.length();
                //TextView显示剩余字数
                tv_inputNumber.setText("还剩余" + number+"字数");
                selectionStart=edit_foodDescription.getSelectionStart();
                selectionEnd = edit_foodDescription.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edit_foodDescription.setText(editable);
                    edit_foodDescription.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        //填写完成按钮
        btn_oK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodImage=imgbtn_foodImg.getDrawable();
                foodName = edit_foodName.getText().toString();
                foodPrice = edit_foodPrice.getText().toString();

                foodDescription = edit_foodDescription.getText().toString();
                addFoodInfoIsEmpty = new
                 AddFoodInfoIsEmpty(AddFoodActivity.this);
                boolean empty= addFoodInfoIsEmpty.isInfoEmpty(foodImage,foodName,foodPrice);
                if (empty)
                {
                    List<FoodDetail.ItemListBean> item_list = ManageFoodDetailActivity.foodDetail.getItem_list();
                    if (path!=null)
                    {
                        try {
                            InputStream open = new FileInputStream( path );
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int n = 0;
                            while (-1 != (n = open.read( buffer ))) {
                                output.write( buffer, 0, n );
                            }

                            OssUtils.updata( AddFoodActivity.this, objectKey,
                                    output.toByteArray());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    for (int i=0;i>item_list.size();i++)
                    {
                        item_list.get(i).setPhoto(path);
                        item_list.get(i).setName(foodName);
                        item_list.get(i).setUnivalence(foodPrice);
                        item_list.get(i).setDescription(foodDescription);
                        foodDetail.setItem_list(item_list);
                        foodDetailList.add(foodDetail);
                    }
                    System.out.println("名字："+foodName + ProjectManageActivity
                            .dishId + foodDescription + foodPrice);
                     ProjectTypeDetailManager detailManager = new
                            ProjectTypeDetailManager(AddFoodActivity.this,new LoginRegisterInformationHandle());

                        detailManager.projectDetailManageCommit(foodName,
                                ProjectManageActivity.dishId,
                               foodPrice, foodDescription,path);
                            finish();

                }


            }
        });
    }
    //加载相册图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        imgbtn_foodImg.setImageBitmap(bm);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (resultCode==RESULT_OK)
        {
            switch (requestCode){
                case 1:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals( Environment.MEDIA_MOUNTED )) { //
                        // 检测sd是否可用
                        Log.i( "TestFile",
                                "SD card is not avaiable/writeable right now." );
                        return;
                    }
                    name = new DateFormat().format( "yyyyMMdd_hhmmss",
                            Calendar.getInstance( Locale.CHINA ) ) + ".jpg";
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get( "data" );//
                    // 获取相机返回的数据，并转换为Bitmap图片格式
                    FileOutputStream b = null;
                    File file = new File( "/sdcard/myImage/" );
                    file.mkdirs();// 创建文件夹
                    filePath = "/sdcard/myImage/" + name;
                    try {
                        b = new FileOutputStream( filePath );
                        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, b );
                        // 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imgbtn_foodImg.setImageBitmap(bitmap);
                    startClipActivity(filePath);
                    break;
                case 2:
                    type=2;
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c =getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    filePath=imagePath;
                    showImage(imagePath);
                    startClipActivity(imagePath);
                    c.close();
                    break;
                case 4:
                    path = data.getStringExtra( "path" );
                    BitmapUtils bitmapUtils = new BitmapUtils( getApplicationContext() );
                    bitmap = bitmapUtils.decodeFile(path);
                    imgbtn_foodImg.setImageBitmap( bitmap );
                    break;
            }
        }

    }
    // 得到相册路径
    public String getCameraPath(Intent data) {
        Uri originalUri = data.getData();
        String[] proj = {MediaStore.Images.Media.DATA};

        // 好像是android多媒体数据库的封装接口，具体的看Android文档     数据库

        Cursor cursor = this.managedQuery( originalUri, proj,
                null, null, null );

        // 获取游标

        int column_index = cursor
                .getColumnIndexOrThrow( MediaStore.Images.Media.DATA );

        // 将光标移至开头 ，这个很重要，不小心很容易引起越界

        cursor.moveToFirst();

        // 最后根据索引值获取图片路径

        String path = cursor.getString( column_index );
        return path;
    }
    public void startClipActivity(String path) {
        Intent intent = new Intent( this, ManagePicActivity.class );
        intent.putExtra( "path", path );
        startActivityForResult( intent, 4 );
    }
    public void backType(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder
                (AddFoodActivity.this);
        builder.setTitle("用户提示")
                .setMessage("小主确定放弃本次菜单信息的输入吗？")
                .setCancelable(false);
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

            }
        });

        builder.setNegativeButton("不放弃", new DialogInterface.OnClickListener() {



            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}
