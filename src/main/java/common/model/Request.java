package common.model;

import org.json.JSONObject;
import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operation;
    private String data;
    private transient Object callback;  // Mark as transient since callbacks often can't be serialized

    public Request() { }

    public Request(String operation, String data) {
        this.operation = operation;
        this.data = data;
    }

    // Rest of your methods remain the same
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getCallback() {
        return callback;
    }

    public void setCallback(Object callback) {
        this.callback = callback;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("operation", operation);
        json.put("data", data);
        return json.toString();
    }
}