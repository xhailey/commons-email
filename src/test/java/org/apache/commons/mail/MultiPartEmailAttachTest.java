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

public class MultiPartEmailAttachTest {
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
    @Test(expected = MessagingException.class)
    public void validFileAttachmentAttachedToEmptyEmailAtRandomDispositionShouldFail() {
        try {
            final File f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "middle");
        } catch (IOException | EmailException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.3
    @Test(expected = UnsupportedEncodingException.class)
    public void invalidFileAttachmentAttachedToEmptyEmailInlineShouldFail() {
        try {
            final File f = File.createTempFile("xxx", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
        } catch (IOException | EmailException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
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
    @Test(expected = MessagingException.class)
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
    public void validEmailAttachmentCanBeAttachedToEmptyEmailInline() {
        try {
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "inline");

            assertEquals(1, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.8
    @Test(expected = MessagingException.class)
    public void validEmailAttachmentAttachedToEmptyEmailAtRandomDispositionShouldFail() {
        try {
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "middle");
        } catch (EmailException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.9
    @Test(expected = UnsupportedEncodingException.class)
    public void invalidEmailAttachmentAttachedToEmptyEmailInlineShouldFail() {
        try {
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath("xxx.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "inline");
        } catch (EmailException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
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
            addPartsToEmails(1);
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
            addPartsToEmails(1);
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
            addPartsToEmails(1);
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
            addPartsToEmails(1);
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
            addPartsToEmails(2);
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
            addPartsToEmails(2);
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
            addPartsToEmails(2);
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
            addPartsToEmails(2);
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
            addPartsToEmails(3);
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
            addPartsToEmails(3);
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
            addPartsToEmails(3);
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
            addPartsToEmails(3);
            email.attach(new URL("https://raft.github.io/raft.pdf"), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.24
    @Test
    public void validEmailAttachmentCanBeAttachedToEmptyEmailInAttachment() {
        try {
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "attachment");

            assertEquals(1, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.25
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith1PartInAttachment() {
        try {
            addPartsToEmails(1);
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "attachment");

            assertEquals(2, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.26
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith1PartInline() {
        try {
            addPartsToEmails(1);
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "inline");

            assertEquals(2, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.27
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith2PartInAttachment() {
        try {
            addPartsToEmails(2);
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "attachment");

            assertEquals(3, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.28
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith2PartInline() {
        try {
            addPartsToEmails(2);
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "inline");

            assertEquals(3, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    //1.29
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith3PartInAttachment() {
        try {
            addPartsToEmails(3);
            EmailAttachment attachment;
            attachment = new EmailAttachment();
            attachment.setName("Test Attachment");
            attachment.setDescription("Test Attachment Desc");
            attachment.setPath(TEST_RESOURCE_PATH  + "testfile.txt");
            String fileName = attachment.getPath();
            File file = new File(fileName);

            this.email.attach(
                    new FileDataSource(file),
                    attachment.getName(),
                    attachment.getDescription(),
                    "attachment");

            assertEquals(4, email.getContainer().getCount());
        } catch (EmailException | MessagingException e) {
            fail("Should not fail");
            e.printStackTrace();
        }
    }

    // 1.30
    @Test
    public void validEmailAttachmentCanBeAttachedToEmailWith3PartInline() {
        addPartsToEmails(3);
        final File f;
        try {
            f = File.createTempFile(TEST_RESOURCE_PATH  + "testfile", ".txt");
            email.attach(new FileDataSource(f), "Test Attachment", "Test Attachment Desc", "inline");
            assertEquals(4, email.getContainer().getCount());
        } catch (IOException | EmailException | MessagingException e) {
            e.printStackTrace();
        }

    }
}
