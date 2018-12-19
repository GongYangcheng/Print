package com.xuanhuai.print.cprint;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
	public static List<Map<String, String>> getListOfMap(ArrayList<Bluetooth> list){
		List<Map<String, String>> resList=new ArrayList<Map<String,String>>();
		for (Bluetooth l:list) {
			Map<String , String> data=new HashMap<String, String>();
			data.put("btName", l.getBtName());
			data.put("btAddress", l.getBtAddress());
			data.put("btRSSI", l.getBtRSSI());
			resList.add(data);
		}
		return resList;
	}

	public static void sonUiStateMsg(String s){
		Message msg=new Message();
		msg.obj=s;
		Handler handler = SocketActivity.getStateHandler();
		handler.sendMessage(msg);
	}
	public static void sonUiInfoMsg(String s){
		Message msg=new Message();
		msg.obj=s;
		Handler handler = SocketActivity.getInfoHandler();
		handler.sendMessage(msg);
	}

}