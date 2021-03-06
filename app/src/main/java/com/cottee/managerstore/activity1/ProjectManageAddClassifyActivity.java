package com.cottee.managerstore.activity1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cottee.managerstore.R;
import com.cottee.managerstore.bean.ProjectManageInfo;
import com.cottee.managerstore.handle.LoginRegisterInformationHandle;
import com.cottee.managerstore.manage.ProjectTypeManage;
import com.cottee.managerstore.utils.ToastUtils;
import com.cottee.managerstore.widget.slidelistutils.SlideAndDragListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/23.
 */

public class ProjectManageAddClassifyActivity extends AppCompatActivity implements View.OnClickListener ,SlideAndDragListView.OnDragDropListener {

    private Toolbar tbprojectmanageadd;
    private List<ProjectManageInfo> projectList = new ArrayList<>();
    /*private List<ProjectManageInfo> projectTestList = new ArrayList<>();*/
    private List<String> addDishList = new ArrayList<>();
    private List<String> moveUpdateDishList = new ArrayList<>();
    private List<String> moveUpdateDishIdList = new ArrayList<>();
    private List<String> updateDishList = new ArrayList<>();
    private List<String> updateDishIdList = new ArrayList<>();
    private List<String> deleteIdList = new ArrayList<>();
    private SlideAndDragListView lvprojectmanageadd;
    private Button btnprojectmanageaddclassifysave;
    private ProjectManageAddAdapter adapter;


    public static Map<Integer, Boolean> checkedMap = new HashMap<Integer, Boolean>();
    private Button btn_back_to_project_manage_from_add;
    private List<String> dishesExampleList = new ArrayList<>();
    private List<String> allDishTypeList = new ArrayList<>();

    private String moveUpdateType = "";
    private String moveUpdateIdType = "";
    private String updateType = "";
    private String updateIdType = "";
    private String deleteType = "";
    private List<String> jsonDishName;
    private List<String> jsonDishId;
    private List<String> newJsonIdList;
    private LinearLayout ll_add_empty;
    private int beginPosition;
    private int endPosition;
    private Button btn_project_manage_add_classify_no_save;
    private ProjectManageInfo mDraggedEntity;
    /*private ProjectManageHandler handler = new ProjectManageHandler();*/
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manage_add_classify);
        initview();
        tbprojectmanageadd.setBackgroundColor(getResources().getColor(R.color.purplishblue));
        setSupportActionBar(tbprojectmanageadd);


        Intent intent = getIntent();
        jsonDishName = (List<String>) intent.getSerializableExtra("dishName");
        jsonDishId = (List<String>) intent.getSerializableExtra("dishId");

        System.out.println("json" + jsonDishName);
        System.out.println("json" + jsonDishId);
        allDishTypeList = jsonDishName;


        updateDishList.clear();
        updateDishIdList.clear();
        deleteIdList.clear();


        for (int i = 0; i < jsonDishName.size(); i++) {
            ProjectManageInfo info = new ProjectManageInfo(jsonDishName.get(i));

            projectList.add(info);
            /*projectTestList.add(info);*/
        }


        adapter = new ProjectManageAddAdapter(this, projectList);
        if (!adapter.isEmpty()) {
            ll_add_empty.setVisibility(View.GONE);

        } else {
            ll_add_empty.setVisibility(View.VISIBLE);
        }
        lvprojectmanageadd.setAdapter(adapter);
        lvprojectmanageadd.setOnDragDropListener(this);

        if (beginPosition != endPosition) {
            btn_project_manage_add_classify_no_save.setVisibility(View.GONE);
            btnprojectmanageaddclassifysave.setVisibility(View.VISIBLE);
        }


    }


    public void initview() {
        tbprojectmanageadd = (Toolbar) findViewById(R.id.tb_project_manage_add);
        lvprojectmanageadd = (SlideAndDragListView) findViewById(R.id.lv_project_manage_add);
        btnprojectmanageaddclassifysave = (Button) findViewById(R.id.btn_project_manage_add_classify_save);
        btn_project_manage_add_classify_no_save = (Button) findViewById(R.id.btn_project_manage_add_classify_no_save);
        btn_back_to_project_manage_from_add = (Button) findViewById(R.id.btn_back_to_project_manage_from_add);
        ll_add_empty = (LinearLayout) findViewById(R.id.ll_add_empty);
        btnprojectmanageaddclassifysave.setOnClickListener(this);

        btn_back_to_project_manage_from_add.setOnClickListener(this);
    }


    private String updateSlideData() {
        for (int i = 0; i < moveUpdateDishList.size(); i++) {

            String updateDishType = moveUpdateDishList.get(i);
            if (i == moveUpdateDishList.size() - 1) {
                moveUpdateType = moveUpdateType + updateDishType;

            } else {
                moveUpdateType = moveUpdateType + updateDishType + "#";
            }

        }
        String update = moveUpdateType.trim();
        /*update = update.substring(0, -1);*/
        return update;
    }

    private String updateSlideIdData() {
        for (int i = 0; i < moveUpdateDishIdList.size(); i++) {

            String updateDishType = moveUpdateDishIdList.get(i);
            if (i == moveUpdateDishIdList.size() - 1) {
                moveUpdateIdType = moveUpdateIdType + updateDishType;

            } else {
                moveUpdateIdType = moveUpdateIdType + updateDishType + "#";
            }

        }
        String update = moveUpdateIdType.trim();
        /*update = update.substring(0, -1);*/
        return update;
    }

    private String updateData() {
        for (int i = 0; i < updateDishList.size(); i++) {

            String updateDishType = updateDishList.get(i);
            if (i == updateDishList.size() - 1) {
                updateType = updateType + updateDishType;

            } else {
                updateType = updateType + updateDishType + "#";
            }

        }
        String update = updateType.trim();
        /*update = update.substring(0, -1);*/
        return update;
    }

    private String updateIdData() {
        for (int i = 0; i < updateDishIdList.size(); i++) {

            String updateDishType = updateDishIdList.get(i);
            if (i == updateDishIdList.size() - 1) {
                updateIdType = updateIdType + updateDishType;

            } else {
                updateIdType = updateIdType + updateDishType + "#";
            }

        }
        String update = updateIdType.trim();
        /*update = update.substring(0, -1);*/
        return update;
    }

    private String deleteIdData() {
        for (int i = 0; i < deleteIdList.size(); i++) {

            String updateDishType = deleteIdList.get(i);
            if (i == deleteIdList.size() - 1) {
                deleteType = deleteType + updateDishType;

            } else {
                deleteType = deleteType + updateDishType + "#";
            }

        }
        String update = deleteType.trim();
        /*update = update.substring(0, -1);*/
        return update;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_project_manage_add_classify_save:
                if (beginPosition != endPosition) {
                    moveUpdateDishList.clear();
                    moveUpdateDishIdList.clear();
                /*for(int i=0;i<projectList.size();i++){
                    allDishTypeList.add(projectList.get(i).getProjectName());
                }*/
                    ProjectTypeManage manage = new ProjectTypeManage(ProjectManageAddClassifyActivity.this, new LoginRegisterInformationHandle
                            (ProjectManageAddClassifyActivity.this, ""));
                    for (int i = 0; i < allDishTypeList.size(); i++) {
                        if (projectList.get(i).getProjectName() != allDishTypeList.get(i)) {
                            moveUpdateDishList.add(projectList.get(i).getProjectName());
                            moveUpdateDishIdList.add(jsonDishId.get(i));
                            System.out.println("滑动后确定修改的菜品" + projectList.get(i).getProjectName());
                            System.out.println("滑动后确定修改的id" + jsonDishId.get(i));
                        }
                    }
                /*moveUpdateDishList.add(allDishTypeList.get(endPosition));
                moveUpdateDishIdList.add(jsonDishId.get(endPosition));
                moveUpdateDishList.add(allDishTypeList.get(beginPosition));
                moveUpdateDishIdList.add(jsonDishId.get(beginPosition));*/
                    String update = updateSlideData();
                    String updateId = updateSlideIdData();

                    System.out.println("滑动更新名字:" + update);
                    System.out.println("滑动更新id:" + updateId);

                    if (!update.equals("") && !updateId.equals("")) {

                        manage.projectManageUpdate(update, updateId);
                    }
                }
                finish();

                break;


            case R.id.btn_back_to_project_manage_from_add:

                if (beginPosition != endPosition) {
                    new AlertDialog.Builder(ProjectManageAddClassifyActivity.this).setTitle("是否放弃本次修改？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    finish();
                }


                break;


        }
    }


    public class ProjectManageAddAdapter extends BaseAdapter {

        private Context context;
        /* private List<ProjectManageInfo> projectManageList;*/
        private String projectName;


        public ProjectManageAddAdapter(Context context, List<ProjectManageInfo> projectManageList) {


            this.context = context;
            /*this.projectManageList = projectManageList;*/


        }

        @Override
        public int getCount() {
            return projectList.size();
        }

        @Override
        public Object getItem(int position) {
            return projectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertview, ViewGroup viewGroup) {


            if (convertview == null) {
                viewHolder = new ViewHolder();
                convertview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_project_manage_add, viewGroup, false);
                viewHolder.tvItemName = (TextView) convertview.findViewById(R.id.tv_item_project_manage_classify_name);
                viewHolder.btnItemUpdate = (Button) convertview.findViewById(R.id.btn_item_project_manage_classify_update);
                viewHolder.btnItemDelete = (Button) convertview.findViewById(R.id.btn_item_project_manage_classify_delete);
                viewHolder.btnItemDrag = (Button) convertview.findViewById(R.id.btn_item_project_manage_classify_drag);
                viewHolder.btnItemDrag.setOnTouchListener(mOnTouchListener);
                convertview.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertview.getTag();
            }

            viewHolder.btnItemDrag.setTag(Integer.parseInt(position + ""));
            if (projectList.get(position).getProjectName().length() < 7) {
                viewHolder.tvItemName.setText(projectList.get(position).getProjectName());
            } else {
                viewHolder.tvItemName.setText(projectList.get(position).getProjectName().substring(0, 9)
                        + ".....");
            }


            viewHolder.btnItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ProjectManageAddClassifyActivity.this).setTitle("确定删除此菜品？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    allDishTypeList.clear();

                                    projectList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    for (int i = 0; i < projectList.size(); i++) {
                                        allDishTypeList.add(projectList.get(i).getProjectName());
                                    }
                                    System.out.println("修改之后的总菜品：" + allDishTypeList);
                                    if (position + 1 <= jsonDishId.size()) {
                                        deleteIdList.add(jsonDishId.get(position));
                                        jsonDishId.remove(jsonDishId.get(position));

                                    }
                                    ProjectTypeManage manage = new ProjectTypeManage(ProjectManageAddClassifyActivity.this, new LoginRegisterInformationHandle
                                            (ProjectManageAddClassifyActivity.this, ""));
                                    String deleteId = deleteIdData();

                                    System.out.println("删除id:" + deleteId);
                                    if (!deleteId.equals("")) {

                                        manage.projectManageDelete(deleteId);
                                    }


                                    if (!adapter.isEmpty()) {
                                        ll_add_empty.setVisibility(View.GONE);

                                    } else {
                                        ll_add_empty.setVisibility(View.VISIBLE);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });


            viewHolder.btnItemUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDishList.clear();
                    updateDishIdList.clear();
                    allDishTypeList.clear();
                    projectName = projectList.get(position).getProjectName();
                    Log.d("ssss", "名字是：" + projectName);
                    final EditText et = new EditText(ProjectManageAddClassifyActivity.this);
                    et.setText(projectName);
                    new AlertDialog.Builder(ProjectManageAddClassifyActivity.this).setTitle("请修改菜品分类名称")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String input = et.getText().toString();

                                    if (input.equals("")) {
                                        Toast.makeText(getApplicationContext(), "输入不能为空！", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        for (ProjectManageInfo projectInfo : projectList) {
                                            if (input.equals(projectInfo.getProjectName())) {
                                                ToastUtils.showToast(ProjectManageAddClassifyActivity.this, "亲，修改的菜品类型已存在了");
                                                return;
                                            }
                                        }

                                        projectList.get(position).setProjectName(input);
                                        adapter.notifyDataSetChanged();
                                        for (int i = 0; i < projectList.size(); i++) {
                                            allDishTypeList.add(projectList.get(i).getProjectName());
                                        }
                                        System.out.println("修改之后的总菜品：" + allDishTypeList);
                                        if (position + 1 <= jsonDishId.size()) {
                                            updateDishIdList.add(jsonDishId.get(position));
                                            updateDishList.add(input);
                                        }
                                        System.out.println("修改的是:" + updateDishIdList + " " + updateDishList);
                                        ProjectTypeManage manage = new ProjectTypeManage(ProjectManageAddClassifyActivity.this, new LoginRegisterInformationHandle
                                                (ProjectManageAddClassifyActivity.this, ""));
                                        String update = updateData();
                                        String updateId = updateIdData();
                                        System.out.println("更新名字:" + update);
                                        System.out.println("更新id:" + updateId);

                                        if (!update.equals("") && !updateId.equals("")) {


                                            manage.projectManageUpdate(update, updateId);
                                        }


                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });


            return convertview;


        }


    }

    protected static class ViewHolder {

        TextView tvItemName;
        Button btnItemUpdate;
        Button btnItemDelete;
        Button btnItemDrag;


    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Object o = v.getTag();
            if (o != null && o instanceof Integer) {
                lvprojectmanageadd.startDrag(((Integer) o).intValue());
            }
            return false;
        }
    };

    @Override
    public void onDragViewStart(int beginPosition) {
        this.beginPosition = beginPosition;
        mDraggedEntity = projectList.get(beginPosition);
        new AlertDialog.Builder(ProjectManageAddClassifyActivity.this).setTitle("亲，滑动和修改中间有些逻辑bug，暂停维护，请谅解！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /*for (int i = 0; i < lvprojectmanageadd.getCount(); i++) {
                            View view = lvprojectmanageadd.getChildAt(2);
                            *//*View view1 = lvprojectmanageadd.getChildAt(i).findViewById(R.id.btn_item_project_manage_classify_delete);*//*
                            view.setVisibility(View.INVISIBLE);
                           *//* view1.setVisibility(View.INVISIBLE);*//*
                        }*/

                    }
                }).setNegativeButton("取消", null)
                .show();
        /*ToastUtils.showToast(this,"onDragViewStart   beginPosition--->" + beginPosition);*/
    }



    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {
        ProjectManageInfo remove = projectList.remove(fromPosition);
        projectList.add(toPosition, remove);
        /*ToastUtils.showToast(this,"onDragDropViewMoved   fromPosition--->" + fromPosition + "  toPosition-->" + toPosition);*/
    }

    @Override
    public void onDragViewDown(int finalPosition) {
        endPosition = finalPosition;
        if(beginPosition!=endPosition){
            btn_project_manage_add_classify_no_save.setVisibility(View.GONE);
            btnprojectmanageaddclassifysave.setVisibility(View.VISIBLE);
        }
        projectList.set(finalPosition, mDraggedEntity);
        ToastUtils.showToast(this,"onDragViewDown   finalPosition--->" + finalPosition);
        for(int i=0;i<projectList.size();i++){
            System.out.println("滑动完显示目前有的东西"+projectList.get(i));
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(beginPosition!=endPosition){
                new AlertDialog.Builder(ProjectManageAddClassifyActivity.this).setTitle("是否放弃本次修改？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }else {
                finish();
            }

            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }







}
