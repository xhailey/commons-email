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

    // 4.2
    @Test
    public void canBuildAnEmailWithNullSubject() throws EmailException, MessagingException, IOException {
        testBuildEmail(null, "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.3
    @Test
    public void canBuildAnEmailWithEmptySubject() throws EmailException, MessagingException, IOException {
        testBuildEmail("", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.4
    @Test
    public void canBuildAnEmailWithLenOneSubject() throws EmailException, MessagingException, IOException {
        testBuildEmail("a", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.5
    @Test
    public void canBuildAnEmailWithLenTwoSubject() throws EmailException, MessagingException, IOException {
        testBuildEmail("ab", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.6
    @Test
    public void canBuildAnEmailWithVeryLongSubject() throws EmailException, MessagingException, IOException {
        testBuildEmail("To be, or not to be, that is the question:\n" +
                        "Whether 'tis nobler in the mind to suffer\n" +
                        "The slings and arrows of outrageous fortune,\n" +
                        "Or to take arms against a sea of troubles\n" +
                        "And by opposing end them. To die—to sleep,", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.7
    @Test
    public void canBuildAnEmailWithIntContentObjectType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", 1, "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }


    // 4.8
    @Test
    public void canBuildAnEmailWithFloatContentObjectType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", 1.1, "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.9
    @Test
    public void canBuildAnEmailWithNullContentType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", null,
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.10
    @Test
    public void canBuildAnEmailWithEmptyContentType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.11
    @Test
    public void canBuildAnEmailWithTextHtmlContentType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/html",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    // 4.11
    @Test
    public void canBuildAnEmailWithRandomStringContentType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "xxx",
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
