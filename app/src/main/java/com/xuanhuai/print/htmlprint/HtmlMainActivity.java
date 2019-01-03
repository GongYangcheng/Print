package com.xuanhuai.print.htmlprint;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.CustomToast;
import com.xuanhuai.print.utils.DollarTransform;
import com.yingmei.printsdk.JolimarkPrint;
import com.yingmei.printsdk.print.DeviceInfo;
import com.yingmei.printsdk.print.PrintParameters;
import com.yingmei.printsdk.print.SearchCallBack;
import com.yingmei.printsdk.print.SendCallBack;
import com.yingmei.printsdk.print.TransType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HtmlMainActivity extends AppCompatActivity {

    private DeviceInfo dev;
    private TextView tv_text;
    private ListView listView;
    private ImageView iv_table;
    private EditText edt_code_input,edt_print_pice,edt_pager_width,edt_pager_height,edt_json_input;
    private MyAdapter myAdapter;
    private ArrayList<DeviceInfo> deviceInfos;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_main);
        path = getFilesDir() + "importent.txt";
        file = new File(path);
        mContext = this;
        tv_text = (TextView) findViewById(R.id.tv_text);
        listView = (ListView) findViewById(R.id.listView);
        iv_table = (ImageView) findViewById(R.id.iv_table);
        edt_code_input = findViewById(R.id.edt_code_input);
        edt_json_input = findViewById(R.id.edt_json_input);
        edt_print_pice = findViewById(R.id.edt_print_pice);
        edt_pager_width = findViewById(R.id.edt_pager_width);
        edt_pager_height = findViewById(R.id.edt_pager_height);
        if (!hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(HtmlMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //权限已申请
        }

        JolimarkPrint.setDebug(true);

        deviceInfos = new ArrayList<>();
        myAdapter = new MyAdapter(this, deviceInfos);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dev = myAdapter.getItem(position);
                tv_text.setText("当前选择:" + dev.getDid());
                JolimarkPrint.stopSearch();
                JolimarkPrint.close();
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    public static boolean hasPermissions(Context context, String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //OK
            } else { //拒绝
                openSettingActivity();
            }
        }
    }

    private void openSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void search(View v) {

        JolimarkPrint.searchDevices(this, 10 * 1000, TransType.TRANS_ALL, searchCallBack);
    }

    SearchCallBack searchCallBack = new SearchCallBack() {
        @Override
        public void startDevices() {
            Log.d("xing", "startDevices");
            deviceInfos.clear();
            myAdapter.notifyDataSetChanged();
        }

        @Override
        public void stopDevices(List<DeviceInfo> list) {
            Log.d("xing", "stopDevices= " + list.size());
        }

        @Override
        public void findDevices(DeviceInfo deviceInfo) {
            deviceInfos.add(deviceInfo);
            myAdapter.notifyDataSetChanged();
        }
    };

    int type = 2;

    public void send(View v) {
        if (dev == null) {
            Toast.makeText(this, "无选择设备", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            PrintParameters printParameters = new PrintParameters(getCurentDateForDb());
            printParameters.setPaper_type(type);
            InputStream in = getAssets().open("piao.html");
            if (v.getId() == R.id.bt_send1) {
                in = getAssets().open("1.html");
                printParameters.setPaper_type(1);
            } else if (v.getId() == R.id.bt_send2) {
                in = getAssets().open("2.html");
                printParameters.setPaper_type(3);
            }
            String data = convertStreamToString(in);

            printParameters.setPdata(data);
            progressDialog.show();
            progressDialog.setMessage("打印中...");
            for (int i = 0; i < printTimes; i++) {
                printParameters.setTaskid("" + (i+1));
                JolimarkPrint.sendToData(this, dev, printParameters, new SendCallBack() {
                    @Override
                    public void sendState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
                        if (state != 0) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void printState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
                        if (Integer.valueOf(taskId) == 3 || state != 0) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    String path;
    File file;
    int printTimes = 1;
    int pagerWidth = 100;
    /**
     * 标签打印机
     *
     * @param v
     */
    public void sendParameter(View v) {
//        打印几张标签
        if (TextUtils.isEmpty(edt_print_pice.getText().toString().trim())){
            printTimes = 1;
        }else{
            printTimes = Integer.parseInt(edt_print_pice.getText().toString().trim());
        }

//        标签纸宽
        if (TextUtils.isEmpty(edt_pager_width.getText().toString().trim())||edt_pager_width.getText().toString().trim() == null){
            pagerWidth = 100;
        }else{
            pagerWidth = Integer.parseInt(edt_pager_width.getText().toString().trim());
        }
//        标签纸内容
        if(TextUtils.isEmpty(edt_code_input.getText().toString().trim())){
            CustomToast.showToast(mContext,"打印内容不能为空");
            return;
        }

        String printContent = edt_code_input.getText().toString().trim();
        JSONObject jsonObject = null;
        try {
            if(TextUtils.isEmpty(edt_json_input.getText().toString().trim())){
                jsonObject = new JSONObject(DollarTransform.json);
            }else{
                jsonObject = new JSONObject(edt_json_input.getText().toString().trim());
            }
            printContent = DollarTransform.dollarTransform(printContent,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            CustomToast.showToast(mContext,"JSON错误："+e.toString());
        }
        String printTop = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
                + "</head>" + "<body style=\"width:384px;\">\r\n";
        String printEnd = "</body></html>";
        if (TextUtils.isEmpty(printContent)){
            CustomToast.showToast(this,"打印内容不能为空");
            return;
        }
        String str = printTop + printContent + printEnd;
        byte[] b = str.getBytes();
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(b);
//            InputStream in = getAssets().open("3.html");
            InputStream in = new FileInputStream(file);
            PrintParameters printParameters = new PrintParameters(getCurentDateForDb());
            printParameters.setPaper_type(type);//打印纸张类型，1：热敏   2：标签  3:带孔
            printParameters.setPdata(convertStreamToString(in)); //打印数据（HTML标签数据）
            printParameters.setPaper_width(pagerWidth);//打印宽度， 单位 毫米
            printParameters.setPaper_height(0);//打印高度， 单位 毫米 (默认为0，自动适配)
            printParameters.setCopies(1); //打印份数
            printParameters.setTaskid(getCurentDateForDb()); //任务ID(必须)
            printParameters.setPtype(1);//打印方式，暂时固定为html打印

            progressDialog.show();
            progressDialog.setMessage("打印中...");
            for (int i = 0; i < printTimes; i++) {
                printParameters.setTaskid("" + (i+1));
                JolimarkPrint.sendToData(this, dev, printParameters, new SendCallBack() {
                    @Override
                    public void sendState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
//                        if (state != 0) {
//                            progressDialog.dismiss();
//                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void printState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
//                        if (Integer.valueOf(taskId) == 3 || state != 0) {
                            progressDialog.dismiss();
//                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendParameterTest(View v) {
        if (TextUtils.isEmpty(edt_print_pice.getText().toString().trim())){
            printTimes = 1;
        }else{
            printTimes = Integer.parseInt(edt_print_pice.getText().toString().trim());
        }
        //        标签纸宽
        if (TextUtils.isEmpty(edt_pager_width.getText().toString().trim())||edt_pager_width.getText().toString().trim() == null){
            pagerWidth = 100;
        }else{
            pagerWidth = Integer.parseInt(edt_pager_width.getText().toString().trim());
        }
        String shopName = "2018121201 温州乐青国贸店D";
        String orderNum = "S03736222874";
        String packingNum = "VK 18120602";
        String currentDate = "2018-12-12";
        String packingName = "张三李四张三李四";

        String str = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
                + "</head>" + "<body style=\"width:384px;\">\r\n"+
                "<h3/>"
                + "<h1 style=\"margin-left: 2px;\">"
                + shopName
                + "</h1></br>"
                + "<h6/>"
                + "<div style=\"margin-left:2px;font-size:24px\">订单号:</div><div style=\"margin-left:2px;font-size:48px\">"
                + orderNum
                + "</div></br>\r\n"
                + "</br>"
                + "<div><label style=\"margin-left:2px;font-size:24px\">拣选单号:</label><label style=\"font-size:48px\">"
                + packingNum
                + "</label></div></br>\r\n"
                + "</br>"
                + "<div><label style=\"margin-left:2px;font-size:24px;\">时间:"
                + currentDate
                + "</label>"
                + "<label style=\"font-size:24px;text-align:right;font-weight:bold;\">拣选员："
                + packingName + "</label></div>\r\n"
                + "</body></html>";

        byte[] b = str.getBytes();
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(b);
//            InputStream in = getAssets().open("3.html");
            InputStream in = new FileInputStream(file);
            PrintParameters printParameters = new PrintParameters(getCurentDateForDb());
            printParameters.setPaper_type(type);//打印纸张类型，1：热敏   2：标签  3:带孔
            printParameters.setPdata(convertStreamToString(in)); //打印数据（HTML标签数据）
            printParameters.setPaper_width(pagerWidth);//打印宽度， 单位 毫米
            printParameters.setPaper_height(0);//打印高度， 单位 毫米 (默认为0，自动适配)
            printParameters.setCopies(1); //打印份数
            printParameters.setTaskid(getCurentDateForDb()); //任务ID(必须)
            printParameters.setPtype(1);//打印方式，暂时固定为html打印

            progressDialog.show();
            progressDialog.setMessage("打印中...");
            for (int i = 0; i < printTimes; i++) {
                printParameters.setTaskid("" + (i+1));
                JolimarkPrint.sendToData(this, dev, printParameters, new SendCallBack() {
                    @Override
                    public void sendState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
//                        if (state != 0) {
//                            progressDialog.dismiss();
//                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void printState(int state, String taskId, String msg) {
                        Log.d("xing", state + "<>" + taskId + "<>" + msg);
//                        if (Integer.valueOf(taskId) == 3 || state != 0) {
                            progressDialog.dismiss();
//                        }
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurentDateForDb() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        JolimarkPrint.close();
        super.onDestroy();
    }

    class MyAdapter extends BaseAdapter {

        Context context;
        ArrayList<DeviceInfo> devices;

        public MyAdapter(Context context, ArrayList<DeviceInfo> devices) {
            this.context = context;
            this.devices = devices;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public DeviceInfo getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHold viewHold = null;
            if (convertView == null) {
                viewHold = new ViewHold();
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                viewHold.tv_name = convertView.findViewById(R.id.tv_name);
                viewHold.tv_address = convertView.findViewById(R.id.tv_address);
                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }

            DeviceInfo deviceInfo = getItem(position);
            if (deviceInfo.getTransType() == TransType.TRANS_WIFI) {
                viewHold.tv_name.setText("WIFI:" + deviceInfo.getDid());
            } else {
                viewHold.tv_name.setText("蓝牙:" + deviceInfo.getDid());
            }
            viewHold.tv_address.setText(deviceInfo.getAddress());

            return convertView;
        }

        class ViewHold {
            TextView tv_name;
            TextView tv_address;
        }
    }
}
