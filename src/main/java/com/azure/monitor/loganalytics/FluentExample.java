package com.azure.monitor.loganalytics;

import org.apache.hc.client5.http.fluent.Request;

public class FluentExample {
	
	public static void main(String[] args) throws Exception {
		
		String response = Request.get("https://httpbin.org/get").execute().returnContent().asString();

		System.out.println(response);
	}
}
