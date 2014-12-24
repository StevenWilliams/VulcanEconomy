package net.vulcanmc.vulcaneconomy.rest;

/**
 * Created by steven on 23/12/14.
 */
public class Transaction {
    private Long transactionid;
    public Transaction(Long transactionid) {
        this.transactionid = transactionid;
    }
    public enum TransactionType {
        CREDIT("CREDIT"), DEBIT("DEBIT");
        private String value;

        private TransactionType(String value) {
            this.value = value;
        }
    };
}
