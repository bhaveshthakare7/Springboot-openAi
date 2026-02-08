package openai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class Api {

public static void main(String[] args) {
		
		try
		{
			String API_KEY="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";
			String END_POINT = "https://api.groq.com/openai/v1/chat/completions";
			
			String requestBody="""
					{
					"model":"llama-3.1-8b-instant",
					"messages":[{"role":"user","content":"suggest me in 5 steps how can i prepare for a Java Spring boot enterprise developer role"}]
					}
					""";
			
			HttpRequest request=HttpRequest.newBuilder()
					.uri(URI.create(END_POINT))
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer "+API_KEY)
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();
			
			HttpClient client=HttpClient.newHttpClient();
			HttpResponse<String> response=client.send(request, HttpResponse.BodyHandlers.ofString());
			
			System.out.println("Response from AI -");
			//System.out.println(response.body());
			
			JSONObject obj=new JSONObject(response.body());
			String AI_RESPONSE=obj.getJSONArray("choices")
					.getJSONObject(0)
					.getJSONObject("message")
					.getString("content");
			
			System.out.println(AI_RESPONSE);
			
			
		}
		catch(Exception e)
		{
			System.out.println("Error calling AI API");
			System.out.println(e);
		}
	}


}

