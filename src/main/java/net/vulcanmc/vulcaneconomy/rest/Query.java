package net.vulcanmc.vulcaneconomy.rest;

public class Query {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private Object value;

    public Query(String key, Object value) {

        this.key = key;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
