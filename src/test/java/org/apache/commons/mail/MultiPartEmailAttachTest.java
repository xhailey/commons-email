package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiPartEmailAttachTest {
    MultiPartEmail email;

    final String TEST_RESOURCE_PATH = "./src/test/resources/t2resource/";

    @Before
    public void setupEmail() {
        email = new MultiPartEmail();
    }

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

}
