package no.spt.sdk.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that collects errors and uses {@link java.util.logging.Logger} to log collected errors
 */
public class LoggingErrorCollector implements ErrorCollector {

    private static final Logger logger = Logger.getLogger(LoggingErrorCollector.class.getName());

    private Level logLevel;

    public LoggingErrorCollector() {
        this(Level.INFO);
    }

    public LoggingErrorCollector(Level logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void collect(DataTrackingException e) {
        logger.log(logLevel, e.getMessage());
        if (e instanceof CommunicationDataTrackingException) {
            logger.log(logLevel, String.format("Request body to data collector was: %s", (
                    (CommunicationDataTrackingException) e).getRequestBody()));
            logger.log(logLevel, String.format("Response code from data collector was: %s", (
                    (CommunicationDataTrackingException) e).getResponseCode()));
            logger.log(logLevel, String.format("Response body from data collector was: %s", (
                    (CommunicationDataTrackingException) e).getResponseBody()));
        }
    }

    @Override
    public void close() throws DataTrackingException {
        // Do nothing
    }
}
