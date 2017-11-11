package com.salesmanager.shop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpPostClient {

	public static HttpResponse invokePostRequest(String url,String jsonRequest) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity;
		try {
			entity = new StringEntity(jsonRequest);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			response = client.execute(httpPost);
			System.out.println("Response Code "+response.getStatusLine().getStatusCode());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
		
			}		
		}
		return response;
	}

	public static String invokePostRequestAndReturnResponseAsString(String url,String jsonRequest) {
		CloseableHttpClient client = HttpClients.createDefault();
		String jsonResponse  = "";
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity;
		try {
			entity = new StringEntity(jsonRequest);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			response = client.execute(httpPost);
			jsonResponse = convertStreamToString(response);
			System.out.println("Response Code "+response.getStatusLine().getStatusCode());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		System.out.println("jsonResponse "+jsonResponse);
		return jsonResponse;
	}

	
	private static String convertStreamToString(HttpResponse response){
		try {
			InputStream  inputStream = response.getEntity().getContent();
			return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}

