package bhavesh.com.OpenAiProject.controller;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bhavesh.com.OpenAiProject.airesponse.OpenAiResponse;

@RestController
public class bhaveshcontroller {
	
	@Value("${api_key}")
	private String API_KEY;
	
	@GetMapping("/test")
	public String test() {
		return "Spring boot open ai ";
	}
	@CrossOrigin(origins = "*")
	@PostMapping("/call")
	public OpenAiResponse callGroq(@RequestBody OpenAiResponse obj) {

	    try {
	        String END_POINT = "https://api.groq.com/openai/v1/chat/completions";

	        JSONObject message = new JSONObject()
	                .put("role", "user")
	                .put("content", obj.getPrompt());

	        JSONObject requestJson = new JSONObject()
	                .put("model", "llama-3.1-8b-instant")
	                .put("messages", new org.json.JSONArray().put(message));

	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(END_POINT))
	                .header("Content-Type", "application/json")
	                .header("Authorization", "Bearer " + API_KEY)
	                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
	                .build();

	        HttpClient client = HttpClient.newHttpClient();
	        HttpResponse<String> response =
	                client.send(request, HttpResponse.BodyHandlers.ofString());

	      
	        System.out.println("RAW GROQ RESPONSE:");
	        System.out.println(response.body());

	        JSONObject jobj = new JSONObject(response.body());

	       
	        if (jobj.has("error")) {
	            obj.setPrompt("GROQ ERROR: " +
	                    jobj.getJSONObject("error").getString("message"));
	            return obj;
	        }

	       
	        String AI_RESPONSE = jobj
	                .getJSONArray("choices")
	                .getJSONObject(0)
	                .getJSONObject("message")
	                .getString("content");

	        obj.setResponse(AI_RESPONSE);

	    } catch (Exception e) {
	        e.printStackTrace();
	        obj.setPrompt("JAVA ERROR: " + e.getMessage());
	    }

	    return obj;
	}
}
