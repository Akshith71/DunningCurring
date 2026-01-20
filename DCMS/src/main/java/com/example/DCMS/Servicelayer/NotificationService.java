package com.example.DCMS.Servicelayer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningRule;
import com.example.DCMS.Entity.Notification;
import com.example.DCMS.Repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	@Autowired
    private  NotificationRepository notificationRepository;

	  public void sendNotification(Customer customer, DunningRule rule) {

	        String message = buildMessage(customer, rule);

	        switch (rule.getChannel()) {

	            case "SMS":
	                sendSms(customer.getMobile(), message);
	                break;

	            case "EMAIL":
	                sendEmail(customer.getEmail(), message);
	                break;

	            case "WHATSAPP":
	                sendWhatsapp(customer.getMobile(), message);
	                break;

	            default:
	                // No notification
	                break;
	        }
	    }

	    private String buildMessage(Customer customer, DunningRule rule) {
	        return "Dear " + customer.getName()
	                + ", your account is overdue by "
	                + customer.getOverdueDays()
	                + " days. Action taken: "
	                + rule.getAction()
	                + ". Please make payment to avoid disruption.";
	    }

	    // 🔽 Mock implementations (safe for now)

	    private void sendSms(String mobile, String message) {
	        System.out.println(" SMS to " + mobile + " → " + message);
	    }

	    private void sendEmail(String email, String message) {
	        System.out.println(" EMAIL to " + email + " → " + message);
	    }

	    private void sendWhatsapp(String mobile, String message) {
	        System.out.println(" WHATSAPP to " + mobile + " → " + message);
	    }
    public List<Notification> getCustomerNotifications(Long customerId) {
        return notificationRepository.findByCustomerId(customerId);
    }
}

