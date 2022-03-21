package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.FileDataSource;
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

    @Before
    public void setupEmail() {
        email = new MultiPartEmail();
    }

    //1.1
    @Test
    public void validFileAttachmentCanBeAttachedToEmptyEmailInline() {
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(1, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.2
    @Test(expected = EmailException.class)
    public void validFileAttachmentAttachedToEmptyEmailAtRandomDispositionShouldFail() throws EmailException{
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "middle");
        } catch (IOException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.3
    @Test(expected = EmailException.class)
    public void invalidFileAttachmentAttachedToEmptyEmailInlineShouldFail() throws EmailException {
        final File f = new File("xxx.txt");
        email.attach(f);
        email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
    }

    //1.4
    @Test
    public void validUrlAttachmentAttachedCanBeAttachedToEmptyEmailInline() {
        try {
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(1, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.5
    @Test(expected = EmailException.class)
    public void validUrlAttachmentAttachedAttachedToEmptyEmailAtRandomDispositionShouldFail() {
        try {
            email.attach(new URL("https://www.google.com"), "Test Attachment", "Test Attachment Desc", "middle");
        } catch (IOException | EmailException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.6
    @Test(expected = EmailException.class)
    public void invalidUrlAttachmentAttachedAttachedToEmptyEmailInlineShouldFail() throws EmailException {
        try {
            email.attach(new URL("https://www.xxx.xx"), "Test Attachment", "Test Attachment Desc", "inline");
        } catch (MalformedURLException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.7
    @Test
    public void validEmailAttachmentCanBeAttachedToEmptyEmailInline() throws EmailException, MessagingException {
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("inline");
        this.email.attach(attachment);
        assertEquals(1, email.getContainer().getCount());
    }

    //1.8
    @Test(expected = EmailException.class)
    public void validEmailAttachmentAttachedToEmptyEmailAtRandomDispositionShouldFail() throws EmailException {
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("middle");
        this.email.attach(attachment);
    }

    //1.9
    @Test(expected = EmailException.class)
    public void invalidEmailAttachmentAttachedToEmptyEmailInlineShouldFail() throws EmailException {
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath("xxx.txt");
        attachment.setDisposition("inline");

        this.email.attach(attachment);
    }

    //1.10
    @Test
    public void validFileAttachmentCanBeAttachedToEmptyEmailAtTheEnd() {
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(1, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.11
    @Test
    public void validUrlAttachmentAttachedCanBeAttachedToEmptyEmailAtTheEnd() {
        try {
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(1, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.12
    @Test
    public void validFileAttachmentCanBeAttachedToAnEmailWithOnePartAtTheEnd() {
        try {
            addPartsToEmails(1, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(2, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.13
    @Test
    public void validUrlAttachmentAttachedCanBeAttachedToAnEmailWithOnePartAtTheEnd() {
        try {
            addPartsToEmails(1, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(2, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.14
    @Test
    public void validFileAttachmentCanBeAttachedToAnEmailWithOnePartInline() {
        try {
            addPartsToEmails(1, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(2, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.15
    @Test
    public void validUrlAttachmentAttachedCanBeAttachedToAnEmailWithOnePartInline() {
        try {
            addPartsToEmails(1, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(2, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.16
    @Test
    public void validFileAttachmentCanBeAttachedToAnEmailWithTwoPartAtTheEnd() {
        try {
            addPartsToEmails(2, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(3, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.17
    @Test
    public void validUrlAttachmentAttachedCanBeAttachedToAnEmailWithTwoPartAtTheEnd() {
        try {
            addPartsToEmails(2, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(3, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.18
    @Test
    public void validFileAttachmentCanBeAttachedToAnEmailWithTwoPartInline() {
        try {
            addPartsToEmails(2, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(3, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.19
    @Test
    public void validUrlAttachmentCanBeAttachedToEmailWith2PartInline() {
        try {
            addPartsToEmails(2, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(3, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.20
    @Test
    public void validFileAttachmentCanBeAttachedToEmailWith3PartInAttachment() {
        try {
            addPartsToEmails(3, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.21
    @Test
    public void validUrlAttachmentCanBeAttachedToEmailWith3PartInAttachment() {
        try {
            addPartsToEmails(3, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "attachment");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.22
    @Test
    public void validFileAttachmentCanBeAttachedToEmailWith3PartInline() {
        try {
            addPartsToEmails(3, email);
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.23
    @Test
    public void validUrlAttachmentCanBeAttachedToEmailWith3PartInline() {
        try {
            addPartsToEmails(3, email);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.24
    @Test
    public void validEmailAttachmentCanBeAttachedToEmptyEmailInAttachment() throws EmailException, MessagingException {
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("attachment");

        this.email.attach(attachment);
        assertEquals(1, email.getContainer().getCount());
    }

    //1.25
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith1PartInAttachment() throws EmailException, MessagingException {
        addPartsToEmails(1, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("attachment");

        this.email.attach(attachment);
        assertEquals(2, email.getContainer().getCount());
    }

    //1.26
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith1PartInline() throws EmailException, MessagingException {
        addPartsToEmails(1, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("inline");

        this.email.attach(attachment);
        assertEquals(2, email.getContainer().getCount());
    }

    //1.27
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith2PartInAttachment() throws EmailException, MessagingException {
        addPartsToEmails(2, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("attachment");

        this.email.attach(attachment);
        assertEquals(3, email.getContainer().getCount());
    }

    //1.28
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith2PartInline() throws EmailException, MessagingException {
        addPartsToEmails(2, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("inline");

        this.email.attach(attachment);
        assertEquals(3, email.getContainer().getCount());
    }

    //1.29
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith3PartInAttachment() throws EmailException, MessagingException {
        addPartsToEmails(3, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("attachment");

        this.email.attach(attachment);
        assertEquals(4, email.getContainer().getCount());
    }

    // 1.30
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith3PartInline() throws EmailException, MessagingException {
        addPartsToEmails(3, email);
        EmailAttachment attachment;
        attachment = new EmailAttachment();
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
        attachment.setDisposition("inline");

        this.email.attach(attachment);
        assertEquals(4, email.getContainer().getCount());

    }
}
