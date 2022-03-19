package org.apache.commons.mail;

import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Practice {


    public static void main(String[] args) {
//
//        try {
//            MultiPartEmail email = new MultiPartEmail();
//            email.setHostName("mail.myserver.com");
//            email.addTo("jdoe@somewhere.org", "John Doe");
//            email.setFrom("me@apache.org", "Me");
//            email.setSubject("The picture");
//            email.setMsg("Here is the picture you wanted");
//
//            final File tmpFile1 = File.createTempFile("attachment", ".eml");
//            final File tmpFile = null;
//
////            email.attach( new FileDataSource(tmpFile1),
////                    "Test Attachment",
////                    "Test Attachment Desc");
//
////            email.addPart(null, null);
//        } catch (IOException | EmailException  e) {
//            e.printStackTrace();
//        }


        try {
            // load your HTML email template
            String htmlEmailTemplate = ".... < img src=\"http://www.apache.org/images/feather.gif\"> ....";

            // define you base URL to resolve relative resource locations
//            URL url = new URL("http://www.apache.org");

            // create the email message
            HtmlEmail email = new HtmlEmail();
//            email.setDataSourceResolver(new DataSourceUrlResolver(url));
            email.setHostName("mail.myserver.com");

            email.addTo("jdoe@somewhere.org", "John Doe");
            email.setFrom("me@apache.org", "Me");
            email.setSubject("Test email with inline image");

            // set the html message
//            email.setHtmlMsg(htmlEmailTemplate);

            // set the alternative message
//            email.setTextMsg("Your email client does not support HTML messages");

            final File tmpFile1 = File.createTempFile("abc", ".pdf");

            // send the email
            email.embed("http://www.xxx.xx", "fdad");
        } catch (EmailException | IOException e) {
            e.printStackTrace();
        }




    }
}
