package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiPartEmailAddPartTest {
    MultiPartEmail email;

    final String TEST_RESOURCE_PATH = "./src/test/resources/t2resource/";

    @Before
    public void setupEmail() {
        email = new MultiPartEmail();
    }

    private void addPartsToEmails(int numOfParts) {
        for (int i = 0; i < numOfParts; i++) {
            try {
                email.addPart("To be, or not to be, that is the question", "text/plain");
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void canAddTextPlainContentAtIndexZeroInEmailWith3Part() throws MessagingException {
        addPartsToEmails(3);
        MimeMultipart m = new MimeMultipart("text/plain");
        MimeBodyPart plainTextPart = new MimeBodyPart();
        plainTextPart.setContent("a", "text/plain");
        m.addBodyPart(plainTextPart);
        try {
          email.addPart(m, 0);
          assertEquals(4, email.getContainer().getCount());

          MimeMultipart c = (MimeMultipart) email.getContainer().getBodyPart(0).getContent();
          String addedContent  = (String) c.getBodyPart(0).getContent();
          assertEquals("a", addedContent);

        } catch (EmailException | IOException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addTextPlainContentAtIndexMinusOneInEmailWith3PartShouldFail() throws MessagingException {
        addPartsToEmails(3);
        MimeMultipart m = new MimeMultipart("text/plain");
        MimeBodyPart plainTextPart = new MimeBodyPart();
        plainTextPart.setContent("a", "text/plain");
        m.addBodyPart(plainTextPart);
        try {
            email.addPart(m, -1);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
