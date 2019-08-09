package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID id;
    private User owner;
    private Currency currency;
    private BigDecimal value;

    public BigDecimal getBalance() {
        return value;
    }
    public Account(UUID id, User owner, Currency currency, BigDecimal value) {
        this.id = id;
        this.owner = owner;
        this.currency = currency;
        this.value = value;
    }

    public User getOwner() {
        return owner;
    }

    public Transaction createTransaction(String type, long amount, String description) {
        return createTransaction(type, amount, description, false);
    }

    //todo: insead of submitting a request for every transaction, group them and process them every 5 minutes (or 10 transactions, whichever comes first)
    public Transaction createTransaction(String type, Long amount, String description, boolean queue) {
        try {
            JSONObject obj = new JSONObject();

            obj.put("account", this.id);
            obj.put("type", type);
            obj.put("description", description);
            obj.put("amount", amount);
            Request request = new Request(VulcanEconomy.getApiURL() + "accounts/" + this.id + "/transactions", "PUT", obj);

            if(queue) {
               // VulcanEconomy.getPlugin().getBalancecache().remove(id);
                //VulcanEconomy.getPlugin().getQueue().addRequest(request);
            } else {
                JsonNode response = request.execute().getBody();/*
                JSONObject data = response.getObject().getJSONObject("data");
                Long transactionid = data.getLong("id");
                return new Transaction(transactionid);*/
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
            Transaction transaction = createTransaction(Transaction.TransactionType.CREDIT.toString(), amount, description, true);
            if(transaction != null) {
                value = value.subtract(BigDecimal.valueOf(amount));
                return transaction;
            }
        }
        //todo: check null checks
        return null;
    }


    public Transaction withdrawAsync(long amount, String description) {
         return null;
        //todo
    }
    public Transaction withdrawAsync(long amount) {
        return withdrawAsync(amount, "No description");
    }

    public Transaction deposit(long amount) {

        return deposit(amount, "No description");
    }
    public Transaction deposit(long amount, String description) {
            Transaction transaction = createTransaction(Transaction.TransactionType.DEBIT.toString(), amount, description, true);
            if(transaction != null) {
                value = value.add(BigDecimal.valueOf(amount));
                return transaction;
            }
        return null;
    }

    public boolean has(long amount) {
        if(this.getBalance().compareTo(BigDecimal.valueOf(amount)) >= 0) {
            return true;
        }
        return false;
    }
    public UUID getId() {
        return this.id;
    }
}
