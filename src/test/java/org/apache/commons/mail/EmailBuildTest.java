package org.apache.commons.mail;

import org.apache.commons.mail.mocks.MockEmailConcrete;
import org.junit.Before;
import org.junit.Test;

import javax.mail.Address;
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

        if (fromEmail != null) {
            email.setFrom(fromEmail);
        }

        this.addNToEmails(toListSize);
        this.addNCcEmails(ccListSize);
        this.addNBccEmails(bccListSize);
        email.setSentDate(date);

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();
        Address[] toList = message.getRecipients(MimeMessage.RecipientType.TO);
        Address[] ccList = message.getRecipients(MimeMessage.RecipientType.CC);
        Address[] bccList = message.getRecipients(MimeMessage.RecipientType.BCC);


        assertEquals(EmailUtils.isEmpty(subject) ? null : subject, message.getSubject());
        assertEquals(content, message.getContent());
        assertEquals(fromEmail, message.getFrom()[0].toString());
        assertEquals(toListSize, toList == null ? 0 : toList.length);
        assertEquals(ccListSize,  ccList == null ? 0 : ccList.length);
        assertEquals(bccListSize,  bccList == null ? 0 : bccList.length);
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
        testBuildEmail("To be, or not to be, that is the question: " +
                        "Whether 'tis nobler in the mind to suffer " +
                        "The slings and arrows of outrageous fortune, " +
                        "Or to take arms against a sea of troubles " +
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

    // 4.12
    @Test
    public void canBuildAnEmailWithRandomStringContentType() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "xxx",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 1);
    }

    //4.13
    @Test
    public void canBuildAnEmailWithSizeZeroToEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 1, 1);
    }

    //4.14
    @Test
    public void canBuildAnEmailWithSizeTwoToEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 1, 1);
    }

    //4.15
    @Test
    public void canBuildAnEmailWithSizeFiveToEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 1, 1);
    }

    //4.16
    @Test
    public void canBuildAnEmailWithSizeZeroCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 0, 1);
    }

    //4.17
    @Test
    public void canBuildAnEmailWithSizeTwoCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 2, 1);
    }

    //4.18
    @Test
    public void canBuildAnEmailWithSizeFiveCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 5, 1);
    }

    //4.19
    @Test
    public void canBuildAnEmailWithSizeZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 0);
    }

    //4.20
    @Test
    public void canBuildAnEmailWithSizeTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 2);
    }

    //4.21
    @Test
    public void canBuildAnEmailWithSizeFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 1, 5);
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

    // 4.25
    @Test
    public void canBuildAnEmailWithPastDate() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2000, 3, 21), 1, 1, 1);
    }

    // 4.26
    @Test
    public void canBuildAnEmailWithFutureDate() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2050, 11, 4), 1, 1, 1);
    }


    // 4.27
    @Test(expected = EmailException.class)
    public void whenRecipientsListIsEmptyShouldFail() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 0, 0);
    }

    // 4.28
    @Test
    public void canBuildAnEmailWithOneToEmailListAndZeroCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 0, 0);
    }

    // 4.29
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndZeroCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 0, 0);
    }

    // 4.30
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndZeroCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 0, 0);
    }

    // 4.31
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndOneCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 1, 0);
    }

    // 4.32
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndOneCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 1, 0);
    }

    // 4.33
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndOneCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 1, 0);
    }

    // 4.34
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndTwoCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 2, 0);
    }

    // 4.35
    @Test
    public void canBuildAnEmailWithOneToEmailListAndTwoCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 2, 0);
    }

    // 4.36
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndTwoCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 2, 0);
    }

    // 4.37
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndTwoCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 2, 0);
    }

    // 4.38
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndFiveCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 5, 0);
    }

    // 4.39
    @Test
    public void canBuildAnEmailWithOneToEmailListAndFiveCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 5, 0);
    }

    // 4.40
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndFiveCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 5, 0);
    }

    // 4.41
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndFiveCCEmailListAndZeroBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 5, 0);
    }

    // 4.42
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndZeroCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 0, 1);
    }

    // 4.43
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndZeroCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 0, 1);
    }

    // 4.44
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndZeroCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 0, 1);
    }

    // 4.45
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndTwoCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 2, 1);
    }

    // 4.46
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndTwoCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 2, 1);
    }

    // 4.47
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndTwoCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 2, 1);
    }

    // 4.48
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndFiveCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 5, 1);
    }

    // 4.49
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndFiveCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 5, 1);
    }

    // 4.50
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndFiveCCEmailListAndOneBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 5, 1);
    }

    // 4.51
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndZeroCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 0, 2);
    }

    // 4.52
    @Test
    public void canBuildAnEmailWithOneToEmailListAndZeroCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 0, 2);
    }

    // 4.53
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndZeroCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 0, 2);
    }

    // 4.54
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndZeroCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 0, 2);
    }

    // 4.55
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndOneCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 1, 2);
    }

    // 4.56
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndOneCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 1, 2);
    }

    // 4.57
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndOneCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 1, 2);
    }

    // 4.58
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndTwoCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 2, 2);
    }

    // 4.59
    @Test
    public void canBuildAnEmailWithOneToEmailListAndTwoCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 2, 2);
    }

    // 4.60
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndTwoCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 2, 2);
    }

    // 4.61
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndTwoCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 2, 2);
    }

    // 4.62
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndFiveCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 5, 2);
    }

    // 4.63
    @Test
    public void canBuildAnEmailWithOneToEmailListAndFiveCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 5, 2);
    }

    // 4.64
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndFiveCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 5, 2);
    }

    // 4.65
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndFiveCCEmailListAndTwoBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 5, 2);
    }

    // 4.66
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndZeroCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 0, 5);
    }

    // 4.67
    @Test
    public void canBuildAnEmailWithOneToEmailListAndZeroCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 0, 5);
    }

    // 4.68
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndZeroCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 0, 5);
    }

    // 4.69
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndZeroCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 0, 5);
    }

    // 4.70
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndOneCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 1, 5);
    }

    // 4.71
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndOneCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 1, 5);
    }

    // 4.72
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndOneCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 1, 5);
    }

    // 4.73
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndTwoCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 2, 5);
    }

    // 4.74
    @Test
    public void canBuildAnEmailWithOneToEmailListAndTwoCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 2, 5);
    }

    // 4.75
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndTwoCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 2, 5);
    }

    // 4.76
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndTwoCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 2, 5);
    }

    // 4.77
    @Test
    public void canBuildAnEmailWithZeroToEmailListAndFiveCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 0, 5, 5);
    }

    // 4.78
    @Test
    public void canBuildAnEmailWithOneToEmailListAndFiveCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 1, 5, 5);
    }

    // 4.79
    @Test
    public void canBuildAnEmailWithTwoToEmailListAndFiveCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 2, 5, 5);
    }

    // 4.80
    @Test
    public void canBuildAnEmailWithFiveToEmailListAndFiveCCEmailListAndFiveBCCEmailList() throws EmailException, MessagingException, IOException {
        testBuildEmail("softwaretest", "abc", "text/plain",
                "bilun.zhang@west.cmu.edu", new Date(2022, 3, 21), 5, 5, 5);
    }

}
