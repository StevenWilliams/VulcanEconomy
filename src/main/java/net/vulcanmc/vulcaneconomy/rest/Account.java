package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class Account {
    private long accountid;
    public long getBalance() {
        Long balance = -1L;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.apiURL + "accounts/" + accountid).asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                balance = object.getLong("balance");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return balance;
    }
    public Account(Long accountid) {
        this.accountid = accountid;
    }
    public User getOwner() {
        Integer userid = -1;
        try {
            JsonNode response = Unirest.get(VulcanEconomy.apiURL + "accounts/" + accountid).asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                userid = object.getInt("account_owner");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if(userid != -1) {
            return new User(userid);
        }
        return null;
    }

    public Transaction createTransaction(String type, Long amount, String description) {
        try {
            JsonNode response = Unirest.post(VulcanEconomy.apiURL + "accounts/" + this.accountid + "/transactions").queryString("account", this.accountid).queryString("type", type).queryString("description", description).queryString("amount", amount).asJson().getBody();
            JSONArray jsonArray = response.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Long transactionid = object.getLong("id");
                return new Transaction(transactionid);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getTransactions() {
        return null;
    }
    public boolean isActive() {
        return true;
    }
    public Transaction withdraw(long amount) {

        return withdraw(amount, "No description");
    }
    public Transaction withdraw(long amount, String description) {
        if(has(amount)) {
            Transaction transaction = createTransaction(Transaction.TransactionType.DEBIT.toString(), amount, description);
            if(transaction != null) {
                return transaction;
            }
        }
        return null;
    }
    public Transaction deposit(long amount) {

        return deposit(amount, "No description");
    }
    public Transaction deposit(long amount, String description) {
            Transaction transaction = createTransaction(Transaction.TransactionType.CREDIT.toString(), amount, description);
            if(transaction != null) {
                return transaction;
            }
        return null;
    }
    public boolean has(long amount) {
        if(this.getBalance() >= amount ) {
            return true;
        }
        return false;
    }
}
