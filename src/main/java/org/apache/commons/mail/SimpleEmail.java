/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.mail;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to send simple internet email messages without
 * attachments.
 *
 * @since 1.0
*/
public class SimpleEmail extends Email
{
    /**
     * Set the content of the mail.
     *
     * @param msg A String.
     * @return An Email.
     * @throws EmailException see javax.mail.internet.MimeBodyPart
     *  for definitions
     * @since 1.0
     */
    @Override
    public Email setMsg(final String msg) throws EmailException
    {
        if (EmailUtils.isEmpty(msg))
        {
            throw new EmailException("Invalid message supplied");
        }

        setContent(msg, EmailConstants.TEXT_PLAIN);
        return this;
    }

    public static class Practice {


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
                email.embed("http://www.google.com", "fdad");
            } catch (EmailException | IOException e) {
                e.printStackTrace();
            }




        }
    }
}
