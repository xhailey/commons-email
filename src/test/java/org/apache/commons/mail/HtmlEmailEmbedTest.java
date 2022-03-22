package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.apache.commons.mail.util.TestUtils.addPartsToEmails;

public class HtmlEmailEmbedTest {
    HtmlEmail email;

    final String TEST_RESOURCE_PATH = "./src/test/resources/t2resource/";

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

    private void testValidUrl(int existingEmailPart, String name, int expectNumOfPart) throws EmailException, MessagingException {
        addPartsToEmails(existingEmailPart, email);

        String cid = email.embed(VALID_URL, name);
        assertNotNull(cid);
        email.buildMimeMessage();
        assertEquals(expectNumOfPart, email.getContainer().getCount());
    }

    // 3.1
    @Test
    public void canEmbedValidUrlWithLenOneEmbedNameIntoEmailWith3Part() throws EmailException, MessagingException {
        testValidUrl(3, "a", 4);
    }

    // 3.2
    @Test(expected = EmailException.class)
    public void embedValidUrlWithNullEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        email.embed(VALID_URL, null);
    }

    // 3.3
    @Test(expected = EmailException.class)
    public void embedValidUrlWithEmptyEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        email.embed(VALID_URL, "");
    }

    // 3.4
    @Test
    public void canEmbedValidUrlWithLenTwoEmbedNameIntoEmailWith3Part() throws EmailException, MessagingException {
        testValidUrl(3, "ab", 4);
    }

    // 3.5
    @Test
    public void canEmbedvalidUrlWithMedLenEmbedNameIntoEmailWith3Part() throws EmailException, MessagingException {
        testValidUrl(3, "To be, or not to be, that is the question", 4);
    }

    // 3.6
    @Test
    public void canEmbedvalidUrlWithVeryLongEmbedNameIntoEmailWith3Part() throws EmailException, MessagingException {
        String name = "To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep\n";

        testValidUrl(3, name, 4);

    }

    // 3.7
    @Test(expected = EmailException.class)
    public void embedNullUrlWithLenOneEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        String url = null;
        String cid = email.embed(url, "a");
    }

    // 3.8
    @Test(expected = EmailException.class)
    public void embedEmptyUrlWithLenOneEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        email.embed("", "a");
    }

    // 3.9
    @Test(expected = EmailException.class)
    public void embedRandomUrlStringWithLenOneEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        String cid = email.embed("abcdef", "a");
    }

    // 3.10
    @Test(expected = EmailException.class)
    public void embedUnreachableUrlWithLenOneEmbedNameIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);
        String cid = email.embed("https://www.xxx.xx", "a");
    }

    // 3.11
    @Test
    public void canEmbedValidUrlWithLenOneEmbedNameIntoEmptyEmail() throws EmailException, MessagingException {
        testValidUrl(0, "a", 1);
    }

    // 3.12
    @Test
    public void canEmbedValidUrlWithLenOneEmbedNameIntoOnePartEmail() throws EmailException, MessagingException {
        testValidUrl(1, "a", 2);
    }

    // 3.13
    @Test
    public void canEmbedValidUrlWithLenOneEmbedNameInto2PartEmail() throws EmailException, MessagingException {
        testValidUrl(2, "a", 3);
    }

    // 3.14
    @Test
    public void canEmbedValidFileInto3PartEmail() throws EmailException, MessagingException {
        addPartsToEmails(3, email);
        File f = new File(TEST_RESOURCE_PATH + "testfile.txt");
        String cid = email.embed(f, "A");
        assertEquals("A", cid);
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.15
    @Test
    public void canEmbedvalidFileWithEmptyCIDIntoEmailWith3Part() throws EmailException, MessagingException {
        addPartsToEmails(3, email);

        final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
        email.embed(f, "");
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.16
    @Test
    public void canEmbedvalidFileWithLenTwoCIDIntoEmailWith3Part() throws EmailException, MessagingException {
        addPartsToEmails(3, email);

        final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
        email.embed(f, "ab");
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.17
    @Test
    public void canEmbedvalidFileWithMedLenCIDIntoEmailWith3Part() throws EmailException, MessagingException {
        addPartsToEmails(3, email);

        final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
        email.embed(f, "To be, or not to be, that is the question");
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.18
    @Test
    public void canEmbedvalidFileWithVeryLongCIDIntoEmailWith3Part() throws EmailException, MessagingException {
        addPartsToEmails(3, email);

        final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
        email.embed(f, "To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep\n");
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.19
    @Test(expected = EmailException.class)
    public void embedNonExistingFileWithLenOneCIDIntoEmailWith3PartShouldFail() throws IOException, EmailException, MessagingException {
        addPartsToEmails(3, email);

        final File f = new File("xxx.txt");
        email.embed(f, "A");
        email.buildMimeMessage();
        assertEquals(4, email.getContainer().getCount());
    }

    // 3.20
    @Test(expected = EmailException.class)
    public void embedInvalidFileWithLenOneCIDIntoEmailWith3PartShouldFail() throws EmailException {
        addPartsToEmails(3, email);

        try {
            final File f = new File("testfile", ".txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(4, email.getContainer().getCount());
        } catch (MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.21
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmptyEmail() {
        try {
            final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(1, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.22
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmailWith1Part() {
        addPartsToEmails(1, email);
        try {
            final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(2, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.23
    @Test
    public void canEmbedvalidFileWithLenOneCIDIntoEmptyWith2Part() {
        addPartsToEmails(2, email);
        try {
            final File f = new File(TEST_RESOURCE_PATH  + "testfile.txt");
            email.embed(f, "A");
            email.buildMimeMessage();
            assertEquals(3, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 3.24
    @Test
    public void canEmbedExistingUrlWithExistingName() throws MalformedURLException, EmailException {
        final URL url = new URL("https://raft.github.io/raft.pdf");
        final String name = "abc";
        String cid1 = email.embed(url, name);
        String cid2 = email.embed(url, name);
        assertEquals(cid1, cid2);
    }

    // 3.25
    @Test(expected = EmailException.class)
    public void embedNotExistingUrlWithExistingNameShouldFail() throws MalformedURLException, EmailException {
        final URL url = new URL("https://raft.github.io/raft.pdf");
        final String name = "abc";
        email.embed(url, name);
        email.embed(new URL("https://www.google.com"), name);
    }

    // 3.26
    @Test
    public void canEmbedExistingFileWithExistingName() throws EmailException {
        final String name = "testfile.txt";
        final File file = new File(TEST_RESOURCE_PATH  + name);

        String cid1 = email.embed(file, "abc");
        String cid2 = email.embed(file, "abc");
        assertEquals(cid1, cid2);
    }

    // 3.27
    @Test(expected = EmailException.class)
    public void embedNonExistingFileWithExistingNameShouldFail() throws EmailException {
        final String name = "testfile.txt";
        final File file = new File(TEST_RESOURCE_PATH  + name);

        email.embed(file, "abc");
        email.embed(new File("xx/" + name), name);
    }

    // 3.28
    @Test(expected = EmailException.class)
    public void embedFileWithPathToDirectoryShouldFail() throws EmailException {
        final File file = new File(TEST_RESOURCE_PATH);
        email.embed(file, "abc");
    }

    // 3.29
    @Test
    public void canEmbedExistingDataSourceWithExistingName() throws EmailException, MalformedURLException {
        final URL url = new URL("https://raft.github.io/raft.pdf");
        final DataSource ds = new URLDataSource(url);

        String cid1 = email.embed(ds, "abc");
        String cid2 = email.embed(ds, "abc");
        assertEquals(cid1, cid2);
    }

    // 3.30
    @Test(expected = EmailException.class)
    public void embedNotExistingDataSourceWithExistingName() throws EmailException, MalformedURLException {
        final String name = "abc";
        final URL url = new URL("https://raft.github.io/raft.pdf");
        final DataSource ds = new URLDataSource(url);

        final File file = new File(TEST_RESOURCE_PATH + "testfile.txt");

        email.embed(ds, name);
        email.embed(new FileDataSource(file), name);
    }

    // 3.31
    @Test(expected = EmailException.class)
    public void embedDataSourceWithEmptyNameShouldFail() throws EmailException, MalformedURLException {
        final String name = "abc";
        final URL url = new URL("https://raft.github.io/raft.pdf");
        final DataSource ds = new URLDataSource(url);
        email.embed(ds, "");
    }

}


