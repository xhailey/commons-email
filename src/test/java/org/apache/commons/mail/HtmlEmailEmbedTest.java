package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class HtmlEmailEmbedTest {
    HtmlEmail email;

    final String VALID_URL = "http://www.google.com";
    final String TEST_RESOURCE_PATH = "./src/test/resources/t2resource/";

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

    // 3.20
    @Test
    public void canEmbedvalidFileWithEmptyCIDIntoEmailWith3Part() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.21
    @Test
    public void canEmbedvalidFileWithLenTwoCIDIntoEmailWith3Part() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "ab");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.22
    @Test
    public void canEmbedvalidFileWithMedLenCIDIntoEmailWith3Part() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "To be, or not to be, that is the question");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.23
    @Test
    public void canEmbedvalidFileWithVeryLongCIDIntoEmailWith3Part() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "To be, or not to be, that is the question:\n" +
                    "Whether 'tis nobler in the mind to suffer\n" +
                    "The slings and arrows of outrageous fortune,\n" +
                    "Or to take arms against a sea of troubles\n" +
                    "And by opposing end them. To die—to sleep,\n");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.24
    @Test(expected = UnsupportedEncodingException.class)
    public void embedNonExistingFileWithLenOneCIDIntoEmailWith3PartShouldFail() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile("xxx", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.25
    @Test(expected = UnsupportedEncodingException.class)
    public void embedNonReadableFileWithLenOneCIDIntoEmailWith3PartShouldFail() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            f.setReadable(false, false);
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.26
    @Test(expected = UnsupportedEncodingException.class)
    public void embedInvalidFileWithLenOneCIDIntoEmailWith3PartShouldFail() {
        addPartsToEmails(3);

        try {
            final File f = File.createTempFile("testfile", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.27
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmptyEmail() {
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(1, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.28
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmailWith1Part() {
        addPartsToEmails(1);
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(2, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.29
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmptyWith2Part() {
        addPartsToEmails(2);
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(3, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }
}


