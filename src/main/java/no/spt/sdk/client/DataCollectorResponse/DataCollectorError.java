package no.spt.sdk.client.DataCollectorResponse;

import java.util.List;
import java.util.Map;

/**
 * Represents an error that was returned from the data collector
 */
public class DataCollectorError {

    private String status;
    private List<Map<String, String>> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }
}
