package com.cottee.managerstore.activity1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cottee.managerstore.R;
import com.cottee.managerstore.handle.LoginRegisterInformationHandle;
import com.cottee.managerstore.manage.LoginRegisterInformationManage;
import com.cottee.managerstore.utils.ToastUtils;


/**
 * Created by Administrator on 2017/11/13.
 */

public class BossLoginActivity extends Activity {

    private TextView tvregister;
    private EditText etloginemail;
    private EditText etloginpassword;
    private Button btnlogin;
    private Button btnbacktomain;
    private TextView tvforgetpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_and_register);
        tvregister = (TextView) findViewById(R.id.tv_register);
        etloginemail = (EditText) findViewById(R.id.et_login_email);
        etloginpassword = (EditText) findViewById(R.id.et_login_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnbacktomain = (Button) findViewById(R.id.btn_back_to_store_main);
        tvforgetpwd = (TextView) findViewById(R.id.tv_forget_pwd);


        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BossLoginActivity.this,RegisterVarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = etloginemail.getText().toString().trim();
                String loginPassword = etloginpassword.getText().toString().trim();
                if(!loginEmail.isEmpty()){
                    if(!loginPassword.isEmpty()){
                        LoginRegisterInformationManage loginManage = new LoginRegisterInformationManage(BossLoginActivity.this, new
                                LoginRegisterInformationHandle(BossLoginActivity.this,
                                ""));
                        loginManage.userLogin(loginEmail,loginPassword);
                    }else {
                        ToastUtils.showToast(BossLoginActivity.this,"亲，密码不能为空");
                    }

                }else {
                    ToastUtils.showToast(BossLoginActivity.this,"亲，邮箱不能为空");
                }

            }
        });

        btnbacktomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });


        tvforgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BossLoginActivity.this,ForgetVarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }

}
