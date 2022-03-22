package org.apache.commons.mail;

import org.apache.commons.mail.mocks.MockEmailConcrete;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import static org.junit.Assert.*;

public class EmailBuildTest {

    Email email;

    final String HOST_NAME = "localhost";
    final int SMTP_PORT = 3000;

    @Before
    public void setup() {
        email = new MockEmailConcrete();
        email.setHostName(HOST_NAME);
        email.setSmtpPort(SMTP_PORT);
    }

    private void addNToEmails(int n) throws EmailException {
        for (int i = 0; i < n; i++) {
            email.addTo("bilun.zhang@west.cmu.edu");
        }
    }
    private void addNCcEmails(int n) throws EmailException {
        for (int i = 0; i < n; i++) {
            email.addCc("bilun.zhang@west.cmu.edu");
        }
    }

    private void addNBccEmails(int n) throws EmailException {
        for (int i = 0; i < n; i++) {
            email.addBcc("bilun.zhang@west.cmu.edu");
        }
    }

    private void testBuildEmail(String subject, Object content,
                                String contentType, String fromEmail,
                                Date date, int toListSize, int ccListSize,
                                int bccListSize) throws EmailException, MessagingException, IOException {
        email.setSubject(subject);
        email.setContent(content, contentType);
        email.setFrom(fromEmail);

        this.addNToEmails(toListSize);
        this.addNCcEmails(ccListSize);
        this.addNBccEmails(bccListSize);
        email.setSentDate(date);

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();

        assertEquals(subject, message.getSubject());
        assertEquals(contentType, message.getContentType());
        assertEquals(content, message.getContent());
        assertEquals(fromEmail, message.getFrom()[0].toString());
        assertEquals(toListSize, message.getRecipients(MimeMessage.RecipientType.TO).length);
        assertEquals(ccListSize, message.getRecipients(MimeMessage.RecipientType.CC).length);
        assertEquals(bccListSize, message.getRecipients(MimeMessage.RecipientType.BCC).length);
        assertEquals(date.getTime(), message.getSentDate().getTime());
    }

    // 4.1
    @Test
    public void canBuildAnEmail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.22
    @Test(expected = EmailException.class)
    public void whenFromEmailAddressIsNullShouldFail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                null, new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.23
    @Test(expected = EmailException.class)
    public void whenFromEmailAddressEmptyShouldFail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.24
    @Test(expected = EmailException.class)
    public void whenFromEmailAddressIsRandomStringShouldFail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "abcdef", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.27
    @Test(expected = EmailException.class)
    public void whenRecipientsListIsEmptyShouldFail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 0, 0);
    }
}
