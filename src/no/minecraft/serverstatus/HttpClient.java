package no.minecraft.serverstatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class HttpClient {
	private String url = "http://www.minecraft.no/serverliste/rapporter";
	
	public Request send(Request status) {
		Gson json = new Gson();

		String payload = null;
		try {
			payload = URLEncoder.encode(json.toJson(status), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] data = payload.getBytes(StandardCharsets.UTF_8);
		
		HttpURLConnection http = null;
		try {
			http = (HttpURLConnection)new URL(url).openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			http.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		
		http.setDoOutput(true);
		http.setRequestProperty("Content-Type", "application/json");
		http.setRequestProperty("Charset", "UTF-8");
		http.setRequestProperty("Content-Length", Integer.toString(data.length));
		
		try {
			http.getOutputStream().write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Reader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String result = "";
        try {
        	int c = 0;
			while ((c = input.read()) != -1) {
				result += (char)c;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Request response = null;
        try {
        	response = json.fromJson(result, Request.class);
        } catch(JsonSyntaxException e) {
        	e.printStackTrace();
        }
        
		return response;
	}
}