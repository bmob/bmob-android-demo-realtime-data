package cn.bmob.rdt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * @author zhangchaozhou
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Button mBtnUpdateChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnUpdateChat = findViewById(R.id.btn_update_chat);
        mBtnUpdateChat.setOnClickListener(this);
        startBmobRealTimeData();
    }

    private void startBmobRealTimeData() {

        final BmobRealTimeData bmobRealTimeData = new BmobRealTimeData();
        bmobRealTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if (e == null) {
                    Log.i(TAG, "连接情况：" + (bmobRealTimeData.isConnected() ? "已连接" : "未连接"));
                    if (bmobRealTimeData.isConnected()) {
                        //TODO 如果已连接，设置监听动作为：监听Chat表的更新
                        bmobRealTimeData.subTableUpdate("Chat");
                    }
                } else {
                    Log.e(TAG, "连接出错：" + e.getMessage());
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                /**
                 * {"nameValuePairs":{"appKey":"f25fe6dad5bca9d0bb090072ea1e3c65","tableName":"Chat","objectId":"","action":"updateTable",
                 * "data":{"nameValuePairs":{"content":"更新Chat表测试+1537324483401","createdAt":"2018-09-19 10:35:00","name":"RDT","objectId":"d5fffe82e9","updatedAt":"2018-09-19 10:35:00"}}}}
                 */
                Gson gson = new Gson();
                String action = jsonObject.optString("action");
                String jsonString = gson.toJson(jsonObject);
                Log.i(TAG, "更新返回内容是：" + jsonString);
                Log.i(TAG, "当前更新动作是：" + action);
                if (action.equals(BmobRealTimeData.ACTION_UPDATETABLE)) {
                    //TODO 如果监听表更新
                    JSONObject data = jsonObject.optJSONObject("data");
                    Log.i(TAG, "name：" + data.optString("name"));
                    Log.i(TAG, "content：" + data.optString("content"));
                    Toast.makeText(MainActivity.this, "监听到更新：" + data.optString("name") + "-" + data.optString("content"), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_chat:
                Chat chat = new Chat();
                chat.setName("RDT");
                chat.setContent("更新Chat表测试" + System.currentTimeMillis());
                chat.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Log.i(TAG, "新增表数据成功：" + objectId);
                            Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "新增表数据出错：" + e.getErrorCode() + "-" + e.getMessage());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
