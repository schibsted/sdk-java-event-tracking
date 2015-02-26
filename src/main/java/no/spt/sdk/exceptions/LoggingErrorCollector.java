package no.spt.sdk.exceptions;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that collects errors and uses {@link java.util.logging.Logger} to log collected errors
 */
public class LoggingErrorCollector implements ErrorCollector {

    private static final int MAX_ERROR_QUEUE_SIZE = 10000;

    private static final Logger logger =
            Logger.getLogger(LoggingErrorCollector.class.getName());

    private Level logLevel;
    private LinkedBlockingQueue<DataTrackingException> errorQueue;

    public LoggingErrorCollector() {
        this(Level.INFO);
    }

    public LoggingErrorCollector(Level logLevel) {
        this.errorQueue = new LinkedBlockingQueue<DataTrackingException>();
        this.logLevel = logLevel;
    }

    @Override
    public void collect(DataTrackingException e) {
        if(errorQueue.size() < MAX_ERROR_QUEUE_SIZE) {
            errorQueue.add(e);
        }
        logger.log(logLevel, e.getMessage());
        if(e.getResponseCode() != null) {
            logger.log(logLevel, String.format("Response code from data collector was: %s", e.getResponseCode()));
        }
        if(e.getResponseBody() != null) {
            logger.log(logLevel, String.format("Response body from data collector was: %s", e.getResponseBody()));
        }
    }
}
