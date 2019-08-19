package com.util.file.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * urlencode工具类
 */
public class URLUtils {

	private static URLUtils instance = new URLUtils();
	private URLUtils(){}
	public static URLUtils getInstance(){
		return instance;
	}
	private String DEFAULT = "utf-8";
	
	public String urlEncode(String source) throws UnsupportedEncodingException{
		return urlEncode(source, DEFAULT);
	}
	public String urlEncode(String source, String type) throws UnsupportedEncodingException{
		return URLEncoder.encode(source, type);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(instance.urlEncode("{\\\"boxes_total_num\\\":2,\\\"customer_address\\\":\\\"客户地址\\\",\\\"merchant_latitude\\\":25.60000000000000142108547152020037174224853515625,\\\"uuid\\\":\\\"62e4e850e60141bb9042da4dff95da24\\\",\\\"food_quantity\\\":15,\\\"merchant_longitude\\\":25.60000000000000142108547152020037174224853515625,\\\"customer_latitude\\\":25.60000000000000142108547152020037174224853515625,\\\"merchant_promise_cooking_time\\\":1565265,\\\"food_amount\\\":6666.0,\\\"chain_store_code\\\":\\\"123456\\\",\\\"boxes_total_price\\\":153.0,\\\"customer_longitude\\\":25.60000000000000142108547152020037174224853515625,\\\"position_source\\\":3}"));
	}
}
