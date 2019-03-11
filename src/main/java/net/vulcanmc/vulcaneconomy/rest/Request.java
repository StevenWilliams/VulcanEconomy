package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class Request {
    private org.json.simple.JSONObject body;
    private String url;
    private String type;
//    private List<Query> queries;

    public String getUrl() {
        return url;
    }

    public Request(String url, String type, JSONObject body) {
        this.url = url;
        this.type = type;

        org.json.simple.JSONObject completeBody = new org.json.simple.JSONObject();
        ArrayList data = new ArrayList();
        data.add(body);
        this.body = completeBody;
    }

    /*
    public List<Query> getQueries() {
        //todo: return MAP
        return queries;
    }*/

    public String getType() {
        return type;
    }

    public org.json.simple.JSONObject getBody() {
        return body;
    }
    public void setBody(org.json.simple.JSONObject body) {
        this.body = body;
    }

    public HttpResponse<JsonNode> execute() throws UnirestException {
        HttpRequestWithBody request = Unirest.post(url).basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass());
        request.body(String.valueOf(body));
        HttpResponse<JsonNode> response = request.asJson();
        return response;
    }
    public Future<HttpResponse<JsonNode>> executeAsync() {
        HttpRequestWithBody request = Unirest.post(url).basicAuth(VulcanEconomy.getPlugin().getApiUser(), VulcanEconomy.getPlugin().getApiPass());
        request.body(String.valueOf(body));
        Future<HttpResponse<JsonNode>> httpResponseFuture = request.asJsonAsync(null);
        return httpResponseFuture;

    }
}
