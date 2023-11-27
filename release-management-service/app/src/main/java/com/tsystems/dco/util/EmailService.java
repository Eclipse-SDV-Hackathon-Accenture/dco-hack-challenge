package com.tsystems.dco.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String username;


  public String sendMail(String recipient, String metaTrack, String track) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper;
    try {
      mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(username);
      mimeMessageHelper.setTo(recipient);
      mimeMessageHelper.setSubject("Quality gate approval is pending");

      String msgbopdy = "<h2>Dear Approver <h2>" +
        "<p> The quality gate for " + track + " , " + metaTrack + " is waiting for the approval, </p>" +
        "<p> please approve. </p>";

      mimeMessageHelper.setText(msgbopdy, true);

      javaMailSender.send(mimeMessage);
      return "Mail sent Successfully";
    } catch (MessagingException e) {
      return "Error while sending mail!!!";
    }
  }
}

