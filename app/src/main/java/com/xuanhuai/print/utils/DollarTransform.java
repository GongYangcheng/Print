package com.xuanhuai.print.utils;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class DollarTransform {
//	public static void main(String[] args) {
//		String s = "sunnyGong";
//		System.out.println(s.indexOf("n"));
//
//	}

	/**
	 *
	 * @param text html内容
	 * @param obj JSon对象
	 * @return
	 */
	public static String dollarTransform(String text,JSONObject obj){

		for (int i = 0; i < dollarCode.length; i++) {
			if (text.contains(dollarCode[i])) {
				try {
					if (text.substring((text.indexOf(dollarCode[i])-9),
							(text.indexOf(dollarCode[i])+dollarCode[i].length()+2))
							.equals("##CUT 0##"+dollarCode[i]+"##")) {
						if (obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)) != null) {
							text = text.replace("##CUT 0##"+dollarCode[i]+"##",
									obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)).
											toString().split(" ")[0]);
							i = i-1;
						}
					}else if (text.substring((text.indexOf(dollarCode[i])-9),
							(text.indexOf(dollarCode[i])+dollarCode[i].length()+2))
							.equals("##CUT 1##"+dollarCode[i]+"##")) {
						if (obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)) != null) {
							text = text.replace("##CUT 1##"+dollarCode[i]+"##",
									obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)).toString().split(" ")[1]);
							i = i-1;
						}
					}else {
						if (obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)) != null) {
							text = text.replace(dollarCode[i], obj.get(dollarCode[i].substring(1,dollarCode[i].length()-1)).toString());
						}
					};
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return text;
	}
	
	// 判断是数字或字母
		public static boolean isNumericOrLetter(String str) {
			Pattern pattern = Pattern.compile("[0-9]*");
			char c = str.charAt(0);
			int i = (int) c;
			if (pattern.matcher(str).matches()) {
				return true;
			}else {
				if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
					return true;
				} else {
					return false;
				}
			}
		}
//		测试json
	public static String json = "{ \"order_id\":56700, \"employee_no\":123 ,"+
			"\"user_name\":\"张三\",\"crate_no\":1,\"customer_name\":\"李四\",\"customer_id\":\"234\",\"customer_address\":\"南京市\"," +
			"\"customer_phone\":13735895468,\"transport_route\":\"京广线\",\"target_finish_time\":\"2018-01-02\","+
			"\"message\":\"提示信息\",\"loading_warehouse\":\"2018-02-02\",\"total_quantity\":\"20\",\"total_quantity_xiang\":\"20\","+
			"\"total_quantity_jian\":\"20\",\"quantity\":\"10\",\"next_location\":\"出货道\",\"total_container\":\"30\","+
			"\"current_container\":\"2\",\"end_time\":\"2018-03-03\",\"print_time\":\"2018-04-04\",\"product_name\":\"2018-05-05\","+
			"\"product_name\":\"标准气缸 DSBC-40-25-PPVA-N3\",\"brand\":\"大宝\",\"type\":\"ZT-01\"}";

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
//	<label style="margin-left:50px;font-size:24px;font-weight:bold;">单号：</label>
//	<label barcode="CODE128" style="barheight:60;text-align:center">##CUT 0##$order_id$##</label>
//	<label style="width:100%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">门店：$customer_name$</label>
//	<p style="line_height:63px;">
//	<label style="width:55%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">数量：共 $total_quantity_xiang$ 箱 $total_quantity_jian$ 件 </label><label style="width:45%;font-size:24px;text-align:left;font-weight:bold;">路线：$transport_route$</label>
//	</p>
//	<p style="line_height:63px;">
//	<label style="width:100%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">备注：$remark$</label>
//	</p>
//	<p style="line_height:63px;">
//	<label style="width:55%;margin-left:50px;font-size:24px;text-align:left;font-weight:bold;">日期：$print_time$</label><label style="width:45%;text-align:left;font-size:24px;font-weight:bold;">托盘：$total_container$-$current_container$</label>
//	</p>


}
