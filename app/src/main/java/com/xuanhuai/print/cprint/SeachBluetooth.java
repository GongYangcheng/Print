package com.xuanhuai.print.cprint;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeachBluetooth extends Activity {

	public static final String TAG ="Chunna==BlueActivity";
    private List<HashMap> blueList;
    private HashMap blueHashMap;
    private ListView glvPaired;
    private BluetoothAdapter adapter;
    private PairedBluetoothDialogAdapter pairedAdapter;

    public static BluetoothSocket socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach);
		
		initBlueTooth();
        glvPaired = (ListView)findViewById(R.id.lv_blue_paired);
        pairedAdapter = new PairedBluetoothDialogAdapter(this,blueList);
        pairedAdapter.notifyDataSetChanged();

        glvPaired.setAdapter(pairedAdapter);
        glvPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                BluetoothDevice gDevice = (BluetoothDevice)(((HashMap)pairedAdapter.getItem(position)).get("blue_device"));
                Map<String, String> infoMap = (Map<String, String>) parent.getItemAtPosition(position);
                //执行页面跳转
                Intent i=new Intent(SeachBluetooth.this, SocketActivity.class);
                CustomToast.showToast(SeachBluetooth.this,"点击item:"+position);
                //传递数据
                i.putExtra("btName", infoMap.get("btName"));
                i.putExtra("btAddress", infoMap.get("btAddress"));
//                i.putExtra("btRSSI", infoMap.get("btRSSI"));
                i.putExtra("role", "client");
                startActivity(i);

                //然后就可以连接或者做操作啦
            }
        });
	}
	
	private void initBlueTooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                adapter.enable();
                //sleep one second ,avoid do not discovery
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            blueList = new ArrayList<HashMap>();
            Log.d(TAG,"获取已经配对devices"+devices.size());
            for (BluetoothDevice bluetoothDevice : devices)
            {
                Log.d(TAG, "已经配对的蓝牙设备：");
                Log.d(TAG, bluetoothDevice.getName());
                Log.d(TAG, bluetoothDevice.getAddress());
                blueHashMap = new HashMap();
                blueHashMap.put("blue_device",bluetoothDevice);
                blueHashMap.put("btName",bluetoothDevice.getName());
                blueHashMap.put("btAddress",bluetoothDevice.getAddress());
                blueList.add(blueHashMap);
            }
    }else{
            Toast.makeText(this,"本机没有蓝牙设备",Toast.LENGTH_SHORT).show();
        }
    }
}
