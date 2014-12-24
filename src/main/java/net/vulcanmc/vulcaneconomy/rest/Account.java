package net.vulcanmc.vulcaneconomy.rest;

import org.json.JSONArray;

import java.util.UUID;

public class Account {
    private long accountid;
    public long getBalance() {
        return -1;
    }
    public User getOwner() {
        return null;
    }
    public JSONArray getTransactions() {
        return null;
    }
    public boolean isActive() {
        return true;
    }
    public boolean withdraw(long amount) {
        return true;
    }
    public boolean deposit(long amount) {
        return true;
    }
    public boolean has(long amount) {
        if(this.getBalance() >= amount ) {
            return true;
        }
        return false;
    }
}
