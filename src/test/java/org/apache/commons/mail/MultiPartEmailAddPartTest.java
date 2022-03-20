package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.apache.commons.mail.util.TestUtils.addPartsToEmails;

public class MultiPartEmailAddPartTest {
    MultiPartEmail email;

    @Before
    public void setupEmail() {
        email = new MultiPartEmail();
    }

    private void testAddPartWithIndex(String content, String contentType, int index, int expectedSize) {
        try {
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent(content, contentType);
            m.addBodyPart(plainTextPart);

            email.addPart(m, index);
            assertEquals(expectedSize, email.getContainer().getCount());

            MimeMultipart c = (MimeMultipart) email.getContainer().getBodyPart(index).getContent();
            String addedContent  = (String) c.getBodyPart(0).getContent();
            assertEquals(content, addedContent);

        } catch (EmailException | IOException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 2.1
    @Test
    public void canAddTextPlainLenOneContentAtIndexZeroInEmailWith3Part() throws MessagingException {
        addPartsToEmails(3, email);
        testAddPartWithIndex("a", "text/plain", 0, 4);
    }

    // 2.2
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addTextPlainLenOneContentAtIndexMinusOneInEmailWith3PartShouldFail() throws MessagingException {
        addPartsToEmails(3, email);
        MimeMultipart m = new MimeMultipart("subtype/123");
        MimeBodyPart plainTextPart = new MimeBodyPart();
        plainTextPart.setContent("a", "text/plain");
        m.addBodyPart(plainTextPart);
        try {
            email.addPart(m, -1);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    // 2.3
    @Test
    public void canAddTextPlainLenOneContentAtIndexOneInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("a", "text/plain", 1, 4);
    }

    // 2.4
    @Test
    public void canAddTextPlainLenOneContentAtIndexTwoInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("a", "text/plain", 2, 4);
    }

    // 2.5
    @Test
    public void canAddTextPlainLenOneContentAtIndexThreeInEmailWith3Part() throws EmailException, MessagingException, IOException {
        addPartsToEmails(3, email);
        email.addPart("a", "text/plain");
        assertEquals(4, email.getContainer().getCount());

        String addedContent = (String) email.getContainer().getBodyPart(3).getContent();
        assertEquals("a", addedContent);
    }

    // 2.6
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addTextPlainLenOneContentAtIndexFiveInEmailWith3PartShouldFail() {
        try {
            addPartsToEmails(3, email);
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent("a", "text/plain");
            m.addBodyPart(plainTextPart);
            email.addPart(m, 5);
        } catch (EmailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    // 2.7
    @Test(expected = EmailException.class)
    public void addNullLenOneContentAtIndexZeroInEmailWith3PartShouldFail() {
        try {
            addPartsToEmails(3, email);
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent("a", null);
            m.addBodyPart(plainTextPart);
            email.addPart(m, 0);
        } catch (EmailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    // 2.8
    @Test(expected = EmailException.class)
    public void addEmptyLenOneContentAtIndexZeroInEmailWith3PartShouldFail() {
        try {
            addPartsToEmails(3, email);
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent("a", "");
            m.addBodyPart(plainTextPart);
            email.addPart(m, 0);
        } catch (EmailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    // 2.9
    @Test
    public void canAddTextHtmlLenOneContentAtIndexZeroInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("a", "text/html", 0, 4);
    }

    // 2.10
    @Test(expected = EmailException.class)
    public void addRandomStringLenOneContentAtIndexZeroInEmailWith3PartShouldFail() {
        try {
            addPartsToEmails(3, email);
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent("a", "xxx");
            m.addBodyPart(plainTextPart);
            email.addPart(m, 0);
        } catch (EmailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    // 2.11
    @Test(expected = EmailException.class)
    public void addTextPlainNullContentAtIndexZeroInEmailWith3PartShouldFail() {
        try {
            addPartsToEmails(3, email);
            MimeMultipart m = new MimeMultipart("subtype/123");
            MimeBodyPart plainTextPart = new MimeBodyPart();
            plainTextPart.setContent(null, "text/plain");
            m.addBodyPart(plainTextPart);
            email.addPart(m, 0);
        } catch (EmailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    // 2.12
    @Test
    public void canAddTextPlainEmptyContentAtIndexZeroInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("", "text/plain", 0, 4);
    }

    // 2.13
    @Test
    public void canAddTextPlainLenTwoContentAtIndexZeroInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("ab", "text/plain", 0, 4);
    }

    // 2.14
    @Test
    public void canAddTextPlainMediumLenContentAtIndexZeroInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("To be, or not to be, that is the question", "text/plain", 0, 4);
    }

    // 2.15
    @Test
    public void canAddTextPlainVeryLongContentAtIndexZeroInEmailWith3Part() {
        addPartsToEmails(3, email);
        testAddPartWithIndex("To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep,\n", "text/plain", 0, 4);
    }

    // 2.16
    @Test
    public void canAddTextPlainLenOneContentAtIndexZeroInEmptyEmail() throws MessagingException, IOException, EmailException {
        email.addPart("a", "text/plain");
        assertEquals(1, email.getContainer().getCount());

        String addedContent = (String) email.getContainer().getBodyPart(0).getContent();
        assertEquals("a", addedContent);
    }

    // 2.17
    @Test
    public void canAddTextPlainLenOneContentAtIndexZeroInEmailWith1Part() {
        addPartsToEmails(1, email);
        testAddPartWithIndex("a", "text/plain", 0, 2);
    }

    // 2.18
    @Test
    public void canAddTextPlainLenOneContentAtIndexZeroInEmailWith2Part() {
        addPartsToEmails(2, email);
        testAddPartWithIndex("a", "text/plain", 0, 3);
    }

    // 2.19
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void canAddTextPlainLenOneContentAtIndexOneInEmptyEmail() {
        testAddPartWithIndex("a", "text/plain", 1, 1);
    }

    // 2.20
    @Test
    public void canAddTextPlainLenOneContentAtIndexOneInEmailWith1Part() throws EmailException, MessagingException, IOException {
        addPartsToEmails(1, email);
        email.addPart("a", "text/plain");
        assertEquals(2, email.getContainer().getCount());

        String addedContent = (String) email.getContainer().getBodyPart(1).getContent();
        assertEquals("a", addedContent);
    }

    // 2.21
    @Test
    public void canAddTextPlainLenOneContentAtIndexOneInEmailWith2Part() {
        addPartsToEmails(2, email);
        testAddPartWithIndex("a", "text/plain", 1, 3);
    }

    // 2.22
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addTextPlainLenOneContentAtIndexTwoInEmptyEmailShouldFail() {
        testAddPartWithIndex("a", "text/plain", 2, 3);
    }

    // 2.23
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void addTextPlainLenOneContentAtIndexTwoInEmailWith1PartShouldFail() {
        addPartsToEmails(1, email);
        testAddPartWithIndex("a", "text/plain", 2, 3);
    }

    // 2.24
    @Test
    public void canAddTextPlainLenOneContentAtIndexTwoInEmailWith2Part() throws EmailException, MessagingException, IOException {
        addPartsToEmails(2, email);

        email.addPart("a", "text/plain");
        assertEquals(3, email.getContainer().getCount());

        String addedContent = (String) email.getContainer().getBodyPart(2).getContent();
        assertEquals("a", addedContent);
    }
}
