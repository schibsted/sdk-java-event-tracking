package no.spt.sdk;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

public class TrackingUtilTest {

    @Test
    public void testVersion() {
        Pattern pattern = Pattern.compile("\\d.\\d.\\d(-SNAPSHOT)?");
        Matcher matcher = pattern.matcher(TrackingUtil.getSdkVersion());
        assertTrue(matcher.matches());
    }

}
