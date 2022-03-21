package org.apache.commons.mail.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class TestUtils {
    public static void addPartsToEmails(int numOfParts, MultiPartEmail email) {
        for (int i = 0; i < numOfParts; i++) {
            try {
                email.addPart("To be, or not to be, that is the question", "text/plain");
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }
    }
}
