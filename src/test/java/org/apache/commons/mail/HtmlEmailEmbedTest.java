package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class HtmlEmailEmbedTest {
    HtmlEmail email;

    final String VALID_URL = "http://www.google.com";

    @Before
    public void setupEmail() throws EmailException {
        String htmlEmailTemplate = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";

        email = new HtmlEmail();
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setHtmlMsg(htmlEmailTemplate);
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

    // 3.1
    @Test
    public void canEmbedValidUrlWithLenOneEmbedNameIntoEmailWith3Part() throws EmailException, MessagingException {
        addPartsToEmails(3);

        String cid = email.embed( VALID_URL, "a");
        assertNotNull(cid);
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.2
    @Test(expected = EmailException.class)
    public void embedValidUrlWithNullEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3);
        email.embed(VALID_URL, null);
    }

    // 3.3
    @Test(expected = EmailException.class)
    public void embedValidUrlWithEmptyEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3);
        email.embed(VALID_URL, "");
    }

    // 3.7
//    public void embedValidUrlWithNullEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
//        addPartsToEmails(3);
//        String url = null;
//        String cid = email.embed(url, "a");
//    }



}


