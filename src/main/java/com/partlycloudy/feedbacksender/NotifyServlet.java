/*
 * Copyright (C) 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.partlycloudy.feedbacksender;


import java.io.*;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Handles the notifications sent back from subscriptions
 *
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 */
public class NotifyServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(NotifyServlet.class.getSimpleName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    protected void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Properties mailServerProperties;
            Session getMailSession;
            MimeMessage generateMailMessage;
//Step1
            System.out.println("\n 1st ===> setup Mail Server Properties..");
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            System.out.println("Mail Server Properties have been setup successfully..");

//Step2
            System.out.println("\n\n 2nd ===> get Mail Session..");
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("pavlocherkashyn@gmail.com"));
            generateMailMessage.setFrom(new InternetAddress("pavlocherkashyn@gmail.com"));
            generateMailMessage.setSubject("Greetings from Feedback Sender...");
            String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
            generateMailMessage.setContent(emailBody, "text/html");
            System.out.println("Mail Session has been created successfully..");

//Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");
// Enter your correct gmail UserID and Password (XXXa.shah@gmail.com)
            transport.connect("smtp.gmail.com", "pavlocherkashyn", "01$puxnY3321");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
            response.getWriter().write("{\"status\":\"OK\"}");
            response.getWriter().flush();
        } catch (Exception e) {
            response.getWriter().write("{\"status\":\"fail\"}");
            response.getWriter().flush();
        }

    }
}
