package net.vulcanmc.vulcaneconomy.rest;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestsQueue {
    ArrayList<Request> requests;
    public RequestsQueue(){

    }
    public void addRequest(Request request) {
        boolean merged = false;
        for(Request r : requests) {
            if(request.getUrl() == r.getUrl()) {
                JSONObject completeRBody = request.getBody();
                JSONArray data = (JSONArray) completeRBody.get("data");
                data.put(r.getBody());
                completeRBody.remove("data");
                completeRBody.put("data", data);
                r.setBody(completeRBody);
                merged = true;
                break;
            }
        }
        if(!merged) {
            requests.add(request);
        }
    }
    /*
    all requests have to be for same account/URI
    response: returns total account balances
    requests {
        1: {
            description: corn.
            amount: 544
            currency: default
            time: timestamp
        },
        2: {
            description: diamonds,
            amount: 134
            currency: default
            time: timestamp

        }
    }


     */
    public void executeRequests() {
        for(Request request : requests) {

            String type = request.getType();
            switch(type.toUpperCase()) {
                case "PUT":
                    request.executeAsync();
                case "GET":
                    //should be done async where called or with callback
                    break;
            }
        }
    }
}
