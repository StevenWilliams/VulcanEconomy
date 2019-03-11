package net.vulcanmc.vulcaneconomy.rest;

import java.util.ArrayList;

public class TransactionsQueue {
    public ArrayList<Transaction> transactions;
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
