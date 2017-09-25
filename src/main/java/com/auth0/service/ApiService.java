package com.auth0.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.auth0.AppConfig;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApiService {

  @Autowired
  AppConfig appConfig;

  public String getAuth0MgmtToken() throws IOException {

    System.out.println("updateUserEmail invoked...");

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.createObjectNode();
    ((ObjectNode) rootNode).put("client_id", appConfig.getClientId());
    ((ObjectNode) rootNode).put("client_secret", appConfig.getClientSecret());
    ((ObjectNode) rootNode).put("audience", "https://" + appConfig.getDomain() + "/api/v2/");
    ((ObjectNode) rootNode).put("grant_type", "client_credentials");

    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
//    System.out.println(jsonString);

    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, jsonString);
    Request request = new Request.Builder().url("https://" + appConfig.getDomain() + "/oauth/token").post(body)
        .addHeader("content-type", "application/json").addHeader("cache-control", "no-cache").build();

    Response response = client.newCall(request).execute();
    String jsonData = response.body().string();
    JSONObject responseJson = new JSONObject(jsonData);
    final String accessToken = (String) responseJson.get("access_token");
    return accessToken;
  }

  public boolean updateUserPassword(final String mgmtToken, final String userId, final String password) throws IOException {

    System.out.println("updateUserPassword invoked...");

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.createObjectNode();
    ((ObjectNode) rootNode).put("password", password);

    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    System.out.println(jsonString);

    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, jsonString);
    Request request = new Request.Builder().url("https://" + appConfig.getDomain() + "/api/v2/users/" + userId)
            .patch(body).addHeader("content-type", "application/json").addHeader("authorization", "Bearer " + mgmtToken)
            .addHeader("cache-control", "no-cache").build();

    Response response = client.newCall(request).execute();
    String jsonData = response.body().string();
    JSONObject responseJson = new JSONObject(jsonData);

    return responseJson.has("email");

  }

}
