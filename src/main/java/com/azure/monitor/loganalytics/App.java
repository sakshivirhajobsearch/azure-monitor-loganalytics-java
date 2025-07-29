package com.azure.monitor.loganalytics;

import java.io.FileInputStream;
import java.util.Properties;

public class App {
	
	public static void main(String[] args) {
		
		try {
			Properties props = new Properties();
			props.load(new FileInputStream("src/main/resources/application.properties"));

			String workspaceId = props.getProperty("workspace.id");
			String sharedKey = props.getProperty("shared.key");
			String logType = props.getProperty("log.type");

			AzureLogAnalyticsClient client = new AzureLogAnalyticsClient(workspaceId, sharedKey, logType);

			String log = "{ \"Level\": \"INFO\", \"Message\": \"Sample Java application log to Azure Log Analytics\" }";

			client.sendLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
