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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.apache.commons.mail.util.TestUtils.addPartsToEmails;

public class MultiPartEmailAttachTest {
    MultiPartEmail email;

    final String TEST_RESOURCE_PATH = "./src/test/resources/t2resource/";

    final String VALID_URL = "https://raft.github.io/raft.pdf";

    final String INVALID_URL = "https://xxxxxxxxx.xx";

    private void testAttachValidFile(File file, int emailSize) throws EmailException, MessagingException {
        addPartsToEmails(emailSize, email);
        email.attach(file);

        assertEquals(emailSize+1, email.getContainer().getCount());
    }

    private void testAttachValidEmailAttachmentFile(String filePath, String name, String description, String disposition, int emailSize) throws EmailException, MessagingException {
        addPartsToEmails(emailSize, email);

        EmailAttachment attachment = new EmailAttachment();
        attachment.setName(name);
        attachment.setDescription(description);
        attachment.setPath(filePath);
        attachment.setDisposition(disposition);
        this.email.attach(attachment);

        assertEquals(emailSize+1, email.getContainer().getCount());
    }

    private void testAttachValidEmailAttachmentUrl(String url, String name, String description, String disposition, int emailSize) throws EmailException, MessagingException, MalformedURLException {
        addPartsToEmails(emailSize, email);

        EmailAttachment attachment = new EmailAttachment();
        attachment.setName(name);
        attachment.setDescription(description);
        attachment.setDisposition(disposition);
        attachment.setURL(new URL(url));
        this.email.attach(attachment);

        assertEquals(emailSize + 1, email.getContainer().getCount());
    }

    private void testAttachValidUrl(String url, String name, String description, String disposition, int emailSize) throws EmailException, MessagingException, MalformedURLException {
        addPartsToEmails(emailSize, email);

        this.email.attach(new URL(url), name, description, disposition);

        assertEquals(emailSize + 1, email.getContainer().getCount());
    }


    @Before
    public void setupEmail() {
        email = new MultiPartEmail();
    }

    //C1.1
    @Test
    public void canAttachAValidUrlToEmail() throws MalformedURLException, EmailException, MessagingException {
        email.attach(new URL(VALID_URL), "a", "a");
        assertEquals(1, email.getContainer().getCount());
    }

    //C1.2
    @Test
    public void canAttachAValidDataSourceToEmail() throws MalformedURLException, EmailException, MessagingException {
        email.attach(new URLDataSource(new URL(VALID_URL)), "a", "a");
        assertEquals(1, email.getContainer().getCount());
    }

    //C1.3
    @Test(expected = EmailException.class)
    public void canNotAttachNullDataSourceToEmail() throws EmailException, MessagingException {
        email.attach((DataSource)null, "a", "a");
    }

    //C1.4
    @Test(expected = EmailException.class)
    public void canAttachInvalidDataSourceToEmail() throws MalformedURLException, EmailException, MessagingException {
        email.attach(new URLDataSource(new URL(INVALID_URL)), "a", "a");
    }

    //1.1
    @Test
    public void canAttachExistingFileWith3PartEmail() throws MessagingException, EmailException {
        testAttachValidFile(new File(TEST_RESOURCE_PATH  + "testfile.txt"), 3);
    }

    //1.2
    @Test(expected = EmailException.class)
    public void attachNonExistingFileWith3PartEmailShouldFail() throws EmailException, MessagingException {
        testAttachValidFile(new File("testfile.txt"), 3);
    }

    //1.3
    @Test(expected = NullPointerException.class)
    public void attachNullFileWith3PartEmailShouldFail() throws EmailException, MessagingException {
        final File f = null;
        testAttachValidFile(f, 3);
    }

    //1.4
    @Test
    public void canAttachExistingFileWithEmptyEmail() throws MessagingException, EmailException {
        testAttachValidFile(new File(TEST_RESOURCE_PATH  + "testfile.txt"), 0);
    }

    //1.5
    @Test
    public void canAttachExistingFileWithOneEmail() throws MessagingException, EmailException {
        testAttachValidFile(new File(TEST_RESOURCE_PATH  + "testfile.txt"), 1);
    }

    //1.6
    @Test
    public void canAttachExistingFileWithTwoEmail() throws EmailException, MessagingException {
        testAttachValidFile(new File(TEST_RESOURCE_PATH  + "testfile.txt"), 2);
    }

    //1.7
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndMidLenDescAnd3PartEmail() throws EmailException, MessagingException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.8
    @Test
    public void canAttachEmailAttachmentWithValidFileAndMidLenNameAndMidLenDescAnd3PartEmail() throws EmailException, MessagingException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt", "To be, or not to be, that is the question",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.9
    @Test(expected = EmailException.class)
    public void attachNullEmailAttachmentShouldFail() throws EmailException {
        EmailAttachment attachment = null;
        this.email.attach(attachment);
    }

    //1.10
    @Test(expected = EmailException.class)
    public void canAttachEmailAttachmentWithInvalidFileShouldFail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile("testfile.txt", "To be, or not to be, that is the question",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.11
    @Test(expected = EmailException.class)
    public void canAttachEmailAttachmentWithInvalidUrlShouldFail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(INVALID_URL, "To be, or not to be, that is the question",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.12
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndNullNameAndMidLenDescAnd3PartEmail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(VALID_URL, null,
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.13
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndEmptyNameAndMidLenDescAnd3PartEmail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(VALID_URL, null,
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.14
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndLenOneNameAndMidLenDescAnd3PartEmail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "a",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.15
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndLenTwoNameAndMidLenDescAnd3PartEmail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "ab",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.16
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndLongLenNameAndMidLenDescAnd3PartEmail() throws MalformedURLException, MessagingException, EmailException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question:\n" +
                        "Whether 'tis nobler in the mind to suffer\n" +
                        "The slings and arrows of outrageous fortune,\n" +
                        "Or to take arms against a sea of troubles\n" +
                        "And by opposing end them. To die—to sleep\n",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.17
    @Test
    public void canAttachEmailAttachmentWithValidFileAndNullNameAndMidLenDescAnd3PartEmail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                null, "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.18
    @Test
    public void canAttachEmailAttachmentWithValidFileAndEmptyNameAndMidLenDescAnd3PartEmail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.19
    @Test
    public void canAttachEmailAttachmentWithValidFileAndLenOneNameAndMidLenDescAnd3PartEmail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "a", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.20
    @Test
    public void canAttachEmailAttachmentWithValidFileAndLen2NameAndMidLenDescAnd3PartEmail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "ab", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.21
    @Test
    public void canAttachEmailAttachmentWithValidFileAndLongLenNameAndMidLenDescAnd3PartEmail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "To be, or not to be, that is the question:\n" +
                        "Whether 'tis nobler in the mind to suffer\n" +
                        "The slings and arrows of outrageous fortune,\n" +
                        "Or to take arms against a sea of troubles\n" +
                        "And by opposing end them. To die—to sleep\n",
                "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.22
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndNullDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL,
                "To be, or not to be, that is the question", null, "attachment", 3);
    }

    //1.23
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndEmptyDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL,
                "To be, or not to be, that is the question", "", "attachment", 3);
    }

    //1.24
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndLenOneDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL,
                "a", "", "attachment", 3);
    }

    //1.25
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndLenTwoDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL,
                "ab", "", "attachment", 3);
    }

    //1.26
    @Test
    public void canAttachEmailAttachmentWithValidUrlAndMidLenNameAndLongLenDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL,
                "ab", "To be, or not to be, that is the question:\n" +
                        "Whether 'tis nobler in the mind to suffer\n" +
                        "The slings and arrows of outrageous fortune,\n" +
                        "Or to take arms against a sea of troubles\n" +
                        "And by opposing end them. To die—to sleep\n", "attachment", 3);
    }

    //1.27
    @Test
    public void canAttachEmailAttachmentWithValidFileAndMidLenNameAndNullDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "To be, or not to be, that is the question", null, "attachment", 3);
    }

    //1.28
    @Test
    public void canAttachEmailAttachmentWithValidFileAndMidLenNameAndEmptyDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "To be, or not to be, that is the question", "", "attachment", 3);
    }

    //1.29
    @Test
    public void canAttachEmailAttachmentWithValidFileAndMidLenNameAndLenOneDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "To be, or not to be, that is the question", "a", "attachment", 3);
    }

    // 1.30
    @Test
    public void canAttachEmailAttachmentWithValidFileAndMidLenNameAndLenTwoDescAnd3PartEmail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH + "testfile.txt",
                "To be, or not to be, that is the question", "ab", "attachment", 3);
    }

    //1.31
    @Test
    public void validFileInEmailAttachmentWithMiddleLenNameAndLongLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep\n", "attachment", 3);
    }

    //1.32
    @Test
    public void validURLInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsInline() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "inline", 3);
    }

    //1.33
    @Test(expected = EmailException.class)
    public void validURLInEmailAttachmentWithMiddleLenNameAndMiddleLenDescAttachedToEmailWithThreePartsAtOtherPosShouldFail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "middle", 3);
    }

    //1.34
    @Test
    public void validFileInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsInline() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question", "inline", 3);
    }

    //1.35
    @Test(expected = EmailException.class)
    public void validFileInEmailAttachmentWithMiddleLenNameAndMiddleLenDescAttachedToEmailWithThreePartsAtOtherPosShouldFail() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question", "middle", 3);
    }

    //1.36
    @Test
    public void validURLInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmptyEmailAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 0);
    }

    //1.37
    @Test
    public void validURLInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithOnePartAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 1);
    }

    //1.38
    @Test
    public void validURLInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithTwoPartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidEmailAttachmentUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 2);
    }

    //1.39
    @Test
    public void validFileInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithZeroPartAtTheEnd() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 0);
    }

    //1.40
    @Test
    public void validFileInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithOnePartAtTheEnd() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 1);
    }

    //1.41
    @Test
    public void validFileInEmailAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithTwoPartAtTheEnd() throws MessagingException, EmailException {
        testAttachValidEmailAttachmentFile(TEST_RESOURCE_PATH  + "testfile.txt", "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 2);
    }

    //1.42
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.43
    @Test(expected = MalformedURLException.class)
    public void nullURLInAttachmentWithMiddleLenNameAndMiddleLenDescAttachedToEmailWithThreePartsAtTheEndShouldFail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(null, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.44
    @Test(expected = EmailException.class)
    public void invalidURLInAttachmentWithMiddleLenNameAndMiddleLenDescAttachedToEmailWithThreePartsAtTheEndShouldFail() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(INVALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.45
    @Test
    public void validURLInAttachmentWithNullNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, null, "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.46
    @Test
    public void validURLInAttachmentWithEmptyNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.47
    @Test
    public void validURLInAttachmentWithLenOneNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "a", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.48
    @Test
    public void validURLInAttachmentWithLenTwoNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "ab", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.49
    @Test
    public void validURLInAttachmentWithLenLongNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep\n", "To be, or not to be, that is the question", "attachment", 3);
    }

    //1.50
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndNullDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", null, "attachment", 3);
    }

    //1.51
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndEmptyLenDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "", "attachment", 3);
    }

    //1.52
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndLenOneDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "a", "attachment", 3);
    }

    //1.53
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndLenTwoDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "ab", "attachment", 3);
    }

    //1.54
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndLenLongDescCanBeAttachedToEmailWithThreePartsAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep\n", "attachment", 3);
    }

    //1.55
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsInline() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "inline", 3);
    }

    //1.56
    @Test(expected = EmailException.class)
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithThreePartsAtOtherDisposition() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "other", 3);
    }

    //1.57
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithZeroPartAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 0);
    }

    //1.58
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithOnePartAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 1);
    }

    //1.59
    @Test
    public void validURLInAttachmentWithMiddleLenNameAndMiddleLenDescCanBeAttachedToEmailWithTwoPartAtTheEnd() throws MessagingException, EmailException, MalformedURLException {
        testAttachValidUrl(VALID_URL, "To be, or not to be, that is the question", "To be, or not to be, that is the question", "attachment", 2);
    }
}
