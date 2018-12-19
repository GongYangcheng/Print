package com.xuanhuai.print.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class DollarTransform {

//	public static void main(String[] args) {
//		String text = "<p style=\"line_height:10px;\"></p>"+
//				"<label style=\"margin-left:50px;font-size:24px;font-weight:bold;\">单号：</label>"+
//				"<label barcode=\"CODE128\" style=\"barheight:60;text-align:center\">$order_id$</label>"+
//				"<p style=\"line_height:63px;\">"+
//				"<label style=\"width:55%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;\">"+
//				"日期：$print_time$</label>" +
//				"<label style=\"width:45%;text-align:left;font-size:24px;font-weight:bold;\">" +
//				"托盘：$total_container$-$current_container$</label></p>";
//
////		JSONObject jsonObj = new JsonObject();
//		DollarTransform d= new DollarTransform();
//
//		JSONObject obj;
//		try {
//			obj = new JSONObject(json);
//			String test = d.test(text,obj);
//			System.out.println(test);
//			String s = "123456789";
////			System.out.println(s.substring(1,s.length()-1));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public static String test(String text,JSONObject obj){
		for (int i = 0; i < dollarCode.length; i++) {
			if (text.contains(dollarCode[i])) {
				try {
					if (obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)) != null) {
						text = text.replace(dollarCode[i], obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)).toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return text;
	}
	public static String json = "{ \"order_id\":56700, \"employee_no\":123 ,"+
			"\"user_name\":\"张三\",\"crate_no\":1,\"customer_name\":\"李四\",\"customer_id\":\"234\",\"customer_address\":\"南京市\"," +
			"\"customer_phone\":13735895468,\"transport_route\":\"京广线\",\"target_finish_time\":\"2018-01-02\","+
			"\"message\":\"提示信息\",\"loading_warehouse\":\"2018-02-02\",\"total_quantity\":\"20\",\"total_quantity_xiang\":\"20\","+
			"\"total_quantity_jian\":\"20\",\"quantity\":\"10\",\"next_location\":\"出货道\",\"total_container\":\"30\","+
			"\"current_container\":\"2\",\"end_time\":\"2018-03-03\",\"print_time\":\"2018-04-04\",\"product_name\":\"2018-05-05\","+
			"\"product_name_simplify\":\"品名简称\",\"brand\":\"大宝\",\"type\":\"ZT-01\"}";

	public static String[] dollarCode = { "$order_id$", "$employee_no$",
			"$user_name$", "$crate_no$", "$customer_name$", "$customer_id$",
			"$customer_address$", "$customer_phone$", "$transport_route$",
			"$target_finish_time$", "$message$", "$loading_time$",
			"$loading_warehouse$", "$total_quantity$",
			"$total_quantity_xiang$", "$total_quantity_jian$", "$quantity$",
			"$next_location$", "$total_container$", "$current_container$",
			"$end_time$", "$print_time$", "$product_name$",
			"$product_name_simplify$", "$brand$", "$type$", "$color$",
			"$size$", "$weight$", "$sku_id$", "$store_location$",
			"$batch_number$", "$unit$", "$manufacture_date$", "$volume$",
			"$store_location_checkNo$", "$store_location_backup$",
			"$carton_pcs$", "$package$", "$package_code$",
			"$package_checkcode$", "$num_perpackage$", "$filler$",
			"$pallet_id$", "$remark$" };
	
//	<p style="line_height:10px;">
//	</p>
//	<label style="margin-left:50px;font-size:24px;font-weight:bold;">单号：</label><label barcode="CODE128" style="barheight:60;text-align:center">9876543210</label>
//	<label style="width:100%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">门店：杭州嘉里中心丼丼屋</label>
//	<p style="line_height:63px;">
//	<label style="width:55%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">数量：共 3 箱 4 件 </label><label style="width:45%;font-size:24px;text-align:left;font-weight:bold;">路线：路线一</label>
//	</p>
//	<p style="line_height:63px;">
//	<label style="width:100%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">备注：生鲜冷冻商品</label>
//	</p>
//	<p style="line_height:63px;">
//	<label style="width:55%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">日期：2018-12-10</label><label style="width:45%;text-align:left;font-size:24px;font-weight:bold;">托盘：15-2</label>
//	</p>

}
