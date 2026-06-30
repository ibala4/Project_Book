package com.example.booklibrary.service;

import com.example.booklibrary.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    // Optional: the app starts fine even if no JavaMailSender bean exists
    // (i.e. no mail config is present). In that case this stays null and
    // sendPurchaseConfirmation() just logs instead of failing the purchase.
    @Autowired(required = false)
    private JavaMailSender mailSender;

    /**
     * Sends a purchase confirmation email if mail is configured.
     * If mail isn't configured, or sending fails for any reason, this
     * logs the issue instead of throwing — a purchase should never fail
     * just because the receipt email couldn't go out.
     */
    public void sendPurchaseConfirmation(String toEmail, String buyerName, Book book, int quantity) {
        if (mailSender == null) {
            log.warn("Mail is not configured (MAIL_USERNAME/MAIL_PASSWORD not set) — " +
                      "skipping confirmation email to {} for \"{}\".", toEmail, book.getTitle());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Purchase Confirmation — " + book.getTitle());
            message.setText(
                "Hi " + buyerName + ",\n\n" +
                "Thank you for your purchase! Here are your order details:\n\n" +
                "Book:      " + book.getTitle() + "\n" +
                "Author:    " + book.getAuthor() + "\n" +
                "Genre:     " + book.getGenre() + "\n" +
                "Quantity:  " + quantity + "\n\n" +
                "Remaining copies in stock: " + book.getCopies() + "\n\n" +
                "Thanks for shopping with us!\n" +
                "— The Library Bookstore"
            );
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Could not send confirmation email to {}: {}", toEmail, e.getMessage());
        }
    }
}
