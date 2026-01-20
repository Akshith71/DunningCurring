package com.example.DCMS.Servicelayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentVerificationEmail(String toEmail, Long paymentId, Double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Action Required: Verify Payment of $" + amount);
        
        // This link hits your Backend API to verify the payment
        String verifyLink = "http://localhost:9000/api/payment/verify?paymentId=" + paymentId;
        
        message.setText("Dear Customer,\n\n" +
                "You initiated a payment of $" + amount + ".\n" +
                "Click the link below to AUTHORIZE this transaction:\n\n" +
                verifyLink + "\n\n" +
                "If you ignore this, the payment will remain PENDING.");

        mailSender.send(message);
        System.out.println("✅ Email sent to " + toEmail);
    }
}