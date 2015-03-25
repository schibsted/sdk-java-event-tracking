package no.spt.sdk.client.DataCollectorResponse;

import java.util.List;
import java.util.Map;

/**
 * Represents a response from the data collector
 */
public class DataCollectorResponse {
    private int code;
    private String status;
    private String type;
    private List<DataCollectorError> errors;
    private List<Map<String, String>> success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataCollectorError> getErrors() {
        return errors;
    }

    public void setErrors(List<DataCollectorError> errors) {
        this.errors = errors;
    }

    public List<Map<String, String>> getSuccess() {
        return success;
    }

    public void setSuccess(List<Map<String, String>> success) {
        this.success = success;
    }


}
