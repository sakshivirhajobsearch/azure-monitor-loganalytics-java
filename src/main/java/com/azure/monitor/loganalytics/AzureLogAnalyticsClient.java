package com.azure.monitor.loganalytics;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;

public class AzureLogAnalyticsClient {

	private final String workspaceId;
	private final String sharedKey;
	private final String logType;

	public AzureLogAnalyticsClient(String workspaceId, String sharedKey, String logType) {

		this.workspaceId = workspaceId;
		this.sharedKey = sharedKey;
		this.logType = logType;
	}

	public void sendLog(String jsonMessage) throws Exception {

		String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
		String signature = buildSignature(jsonMessage.length(), timestamp);

		URI uri = new URI("https://" + workspaceId + ".ods.opinsights.azure.com/api/logs?api-version=2016-04-01");

		String response = Request.post(uri).addHeader("Content-Type", "application/json").addHeader("Log-Type", logType)
				.addHeader("x-ms-date", timestamp).addHeader("Authorization", signature)
				.bodyString(jsonMessage, ContentType.APPLICATION_JSON).execute().returnContent().asString();

		System.out.println("Response from Azure Monitor: " + response);
	}

	private String buildSignature(int contentLength, String date) throws Exception {

		String stringToSign = "POST\n" + contentLength + "\napplication/json\nx-ms-date:" + date + "\n/api/logs";
		String decodedKey = sharedKey;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(Base64.getDecoder().decode(decodedKey), "HmacSHA256"));
		byte[] hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
		String signature = Base64.getEncoder().encodeToString(hash);
		return "SharedKey " + workspaceId + ":" + signature;
	}
}
