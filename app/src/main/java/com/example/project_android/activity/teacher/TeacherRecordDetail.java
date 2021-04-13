package com.example.project_android.activity.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ResourceUtils;
import com.example.project_android.R;
import com.example.project_android.adapter.AttendDetailAdapter;
import com.example.project_android.entity.AttendList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class TeacherRecordDetail extends AppCompatActivity {
    private String data = "[]";
    private AttendList attend;

    private TeacherRecordDetailViewModel viewModel;

    Handler getListHandler = new Handler(msg -> {
        if (msg.what == 1){
            data = msg.getData().getString("data");
            viewModel.setRecordList(data);
        }
        Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        return false;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"考勤详情");

        Intent intent = getIntent();
        attend = (AttendList) intent.getExtras().getSerializable("attend");

        viewModel = new ViewModelProvider(this).get(TeacherRecordDetailViewModel.class);
        viewModel.getAttendDetailList().observe(this,attendDetailLists -> {
            ViewUtils.setRecycler(this,R.id.recycler_record_list,new AttendDetailAdapter(attendDetailLists));
        });

        //do Net Method
        Map<String, String> map = new HashMap<>();
        map.put("attendId",attend.getCourseId());
        NetUtil.getNetData("record/findAllRecord",map,getListHandler);
    }



    @OnClick({R.id.record_type_all,R.id.record_type_success,
            R.id.record_type_failure,R.id.record_type_absent,R.id.record_type_leave})
    public void onClicked(View view){
        refreshButton();
        switch (view.getId()){
            case R.id.record_type_all:
                viewModel.setRecordList(data);
                break;
            case R.id.record_type_success:
                viewModel.updateRecordList(data,2);
                break;
            case R.id.record_type_failure:
                viewModel.updateRecordList(data,1);
                break;
            case R.id.record_type_absent:
                viewModel.updateRecordList(data,0);
                break;
            case R.id.record_type_leave:
                viewModel.updateRecordList(data,3);
                break;
            default:break;
        }
        view.setBackground(ResourceUtils.getDrawable(R.color.gray));
    }

    public void refreshButton(){
        findViewById(R.id.record_type_all).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_success).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_failure).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_absent).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_leave).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
    }
}