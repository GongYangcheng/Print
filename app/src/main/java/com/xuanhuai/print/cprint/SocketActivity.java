package com.xuanhuai.print.cprint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.DollarTransform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@SuppressLint("NewApi")
public class SocketActivity extends Activity {
	BluetoothAdapter bluetoothAdapter;
	BluetoothDevice device;
	BluetoothSocket socket;
	BluetoothServerSocket serSocket;
	TextView infoText1;
	TextView infoText2;
	TextView infoText3;
	TextView infoText4;
	TextView stateText;
	EditText infoEdit,edt_print_pice;
	private static Handler stateHandler;
	public static Handler getStateHandler(){
		return  stateHandler;
	}
	private static Handler infoHandler;
	public static Handler getInfoHandler(){
		return  infoHandler;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket);
		
		infoText1 = (TextView) findViewById(R.id.infoText1);
		infoText2 = (TextView) findViewById(R.id.infoText2);
		infoText3 = (TextView) findViewById(R.id.infoText3);
		infoText4 = (TextView) findViewById(R.id.infoText4);
		infoEdit=(EditText) findViewById(R.id.infoEdit);
		edt_print_pice=(EditText) findViewById(R.id.edt_print_pice);
		//设置EditText的显示方式为多行文本输入
		infoEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		infoEdit.setSingleLine(false);
		stateText= (TextView) findViewById(R.id.stateText);
		
		Intent i=getIntent();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if("service".equals(i.getStringExtra("role"))){
			new ServiceThread().start();
		}else if("client".equals(i.getStringExtra("role"))){
			device = bluetoothAdapter.getRemoteDevice(i.getStringExtra("btAddress"));
			if(device.getBondState()==BluetoothDevice.BOND_NONE){device.createBond();}//若设备未配对则自动配对
			new ClientThread().start();
		}

		//处理消息
		stateHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String s=(String) msg.obj;
				stateText.setText(s);
			}
		};
		infoHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String s=(String) msg.obj;
				infoText4.setText(infoText3.getText());
				infoText3.setText(infoText2.getText());
				infoText2.setText(infoText1.getText());
				infoText1.setText(s);
			}
		};
	}
	
	public void connect(View v){
		
	}
	public void connected(View v){
		
	}
	String infoss = "! 0 200 200 400 1\r\n" +
        "PAGE-WIDTH 600\r\n" +
        "GAP-SENSE\r\n" +
        "SETMAG 1 2\r\n" +
        "TEXT 14 6 50 60 物料号：\r\n" +
        "TEXT 14 6 160 60 1234232\r\n" +
        "TEXT 14 6 50 150 物料描述：\r\n" +
        "TEXT 14 6 180 150 消声器\r\n" +
        "TEXT 14 6 180 200 DS-UI-09-7887\r\n" +
        "TEXT 14 6 50 290 交货量：\r\n" +
        "TEXT 14 6 160 290 20件\r\n" +
        "FORM\r\n" +
        "PRINT\r\n";
	public void sendMsg(View v){
		String info=infoEdit.getText().toString();

		sendMessage(info);
	}
//	public void sendMsgTest(View v){
//	让他们做测试用的
//		String s = "";
//		try {
//			s = DollarTransform.dollarTransform(infoss, new JSONObject(DollarTransform.json));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		sendMessage(s);
//	}
	int printTimes = 1;
	int ii = 1;
	/**
	 * 发送消息
	 * @param msg：发送的消息
	 */
	public void sendMessage(String msg) {
		if (TextUtils.isEmpty(edt_print_pice.getText().toString().trim())||Integer.parseInt(edt_print_pice.getText().toString().trim()) == 0){
			printTimes  = 1 ;
		}else{
			printTimes = Integer.parseInt(edt_print_pice.getText().toString().trim());
		};
		if(socket==null){Toast.makeText(getApplicationContext(), "未建立连接", Toast.LENGTH_SHORT).show();return;}//防止未连接就发送信息
		try {
			//使用socket获得outputstream
			OutputStream out=socket.getOutputStream();
			out.write(msg.getBytes("GBK"));//将消息字节发出
			if(ii == printTimes){
				out.flush();//确保所有数据已经被写出，否则抛出异常
				ii = 1;
			}else{
				ii++;
				sendMessage(msg);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 客户端，进行连接的线程
	 * @author Administrator
	 *
	 */
	class ClientThread extends Thread{
		@Override
		public void run(){
			try {
				//创建一个socket尝试连接，UUID用正确格式的String来转换而成
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				Utils.sonUiStateMsg("正在连接，请稍后......");
				//该方法阻塞，一直尝试连接
				socket.connect();
				Utils.sonUiStateMsg("连接成功");
				//进行接收线程
				new ReadMsg().start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Utils.sonUiStateMsg("连接失败");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 服务端，接收连接的线程
	 * @author Administrator
	 *
	 */
	class ServiceThread extends Thread{
		@Override
		public void run(){
			try {
				//先用本地蓝牙适配器创建一个serversocket
				serSocket= bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				Utils.sonUiStateMsg("正在等待连接");
				if(socket!=null){Utils.sonUiStateMsg("连接成功");}
				//等待连接，该方法阻塞
				socket=serSocket.accept();
				Utils.sonUiStateMsg("连接成功");
				new ReadMsg().start();
			} catch (IOException e) {
//				Toast.makeText(getApplicationContext(), "IOExeption", 1).show();
				System.out.println("IOExeption");
				Utils.sonUiStateMsg("连接失败");
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 循环读取信息的线程
	 * @author Administrator
	 *
	 */
	class ReadMsg extends Thread{
		@Override
		public void run(){
			byte[] buffer = new byte[1024];//定义字节数组装载信息
			int bytes;//定义长度变量
			InputStream in=null;
			try {
				//使用socket获得输入流
				in = socket.getInputStream();
				//一直循环接收处理消息
				while(true){
					if((bytes=in.read(buffer))!=0){
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++){
							buf_data[i]=buffer[i];
						}
						String msg=new String(buf_data);//最后得到String类型消息
						Utils.sonUiInfoMsg(msg);
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.sonUiStateMsg("连接已断开");
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
