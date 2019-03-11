package net.vulcanmc.vulcaneconomy.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import org.json.JSONArray;
import org.json.JSONObject;

public class Account {
    private long accountid;
    public long getBalance() {
        return VulcanEconomy.getPlugin().getBalancecache().get(this.accountid).getBalance();
    }
    public Account(Long accountid) {
        this.accountid = accountid;
        if(!VulcanEconomy.getPlugin().getBalancecache().containsKey(this.accountid)) {
            VulcanEconomy.getPlugin().getBalancecache().put(this.accountid, new BalanceCache(this));
        } else {
           VulcanEconomy.getPlugin().getBalancecache().get(this.accountid);
        }
    }
    public User getOwner() {
        /*
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
            return new User(userid, );
        }
        return null;
        */
        return null;
    }

    public Transaction createTransaction(String type, long amount, String description) {
        return createTransaction(type, amount, description, false);
    }
    //todo: insead of submitting a request for every transaction, group them and process them every 5 minutes (or 10 transactions, whichever comes first)
    public Transaction createTransaction(String type, Long amount, String description, boolean queue) {
        try {
            JSONObject obj = new JSONObject();

            obj.put("account", this.accountid);
            obj.put("type", type);
            obj.put("description", description);
            obj.put("amount", amount);
            Request request = new Request(VulcanEconomy.getApiURL() + "accounts/" + this.accountid + "/transactions", "PUT", obj);

            if(queue) {
                VulcanEconomy.getPlugin().getBalancecache().remove(accountid);
                VulcanEconomy.getPlugin().getQueue().addRequest(request);
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
            Transaction transaction = createTransaction(Transaction.TransactionType.DEBIT.toString(), amount, description, true);
            if(transaction != null) {
                VulcanEconomy.getPlugin().getBalancecache().get(this.accountid).withdraw(amount);
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
            Transaction transaction = createTransaction(Transaction.TransactionType.CREDIT.toString(), amount, description, true);
            if(transaction != null) {
                VulcanEconomy.getPlugin().getBalancecache().get(this.accountid).deposit(amount);
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
    public Long getId() {
        return this.accountid;
    }
}
