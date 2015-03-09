package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import android.view.View;


public final class HttpUtils {
	private HttpUtils(){};
	
	//通过普通URL方法实现获取数据
	public static String getByUrl(String urlPath){
		String count="";
		try {
			URL url = new URL(urlPath);
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setConnectTimeout(3000);
			openConnection.setReadTimeout(2500);
			if(openConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
				return count;
			}
			InputStream is = openConnection.getInputStream();
			byte[] buffer=new byte[1024];
			int len=0;
			ByteArrayBuffer arrayBuffer=new ByteArrayBuffer(1000);
			while (-1!=(len=is.read(buffer))) {
				arrayBuffer.append(buffer, 0, len);
			}
			count=new String(arrayBuffer.buffer(),0,arrayBuffer.buffer().length);
			
		} catch (MalformedURLException e) {
			count="连接失败";
		} catch (IOException e) {
			count="连接失败";
		}
		return count;
	}
	//通过使用现成的API获得数据
	public static String getByApi(String urlPath){
		String count="";
		try {
			HttpClient deClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(urlPath);
			HttpResponse httpResponse = deClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			//利用实体工具类进行toString转换
			count=EntityUtils.toString(entity,"utf-8");
			
		} catch (ClientProtocolException e) {
			count="连接失败";
		} catch (IOException e) {
			count="连接失败";
		}
		return count;
	}
	public static String postByApi(String urlPATH,Map<String, String> parMap){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlPATH);
		String count="";
		Iterator<String> iterator = parMap.keySet().iterator();
		ArrayList<BasicNameValuePair> arrayList=new ArrayList<BasicNameValuePair>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = parMap.get(key);
			BasicNameValuePair nameValuePair=new BasicNameValuePair(key, value);
			arrayList.add(nameValuePair);
		}
		try {
			HttpEntity formEntity = new UrlEncodedFormEntity(arrayList,"utf-8");
			httpPost.setEntity(formEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			count = EntityUtils.toString(entity, "utf-8");
			Log.e("count", "count="+count);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
}
