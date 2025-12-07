package com.bms.common.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    
    @Value("${email.sendgrid.api-key}")
    private String sendGridApiKey;
    
    @Value("${email.sendgrid.from-email}")
    private String fromEmail;
    
    /**
     * Sends verification code to user's email using SendGrid
     * 
     * @param email User's email address
     * @param code Verification code to send
     */
    public void sendVerificationCode(String email, String code) {
        try {
            // Create email
            Email from = new Email(fromEmail);
            Email to = new Email(email);
            String subject = "🔐 Password Reset Verification Code";
            
            String htmlContent = getPasswordResetEmailTemplate(code);
            Content content = new Content("text/html", htmlContent);
            
            Mail mail = new Mail(from, subject, to, content);
            
            // Send email via SendGrid
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email sent successfully to: " + email);
            } else {
                System.err.println("Failed to send email. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }
        } catch (IOException e) {
            System.err.println("Error sending email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sends email verification code to newly registered user
     * 
     * @param email User's email address
     * @param code Verification code to send
     */
    public void sendEmailVerificationCode(String email, String code) {
        try {
            // Create email
            Email from = new Email(fromEmail);
            Email to = new Email(email);
            String subject = "✨ Welcome to BookMyShow - Verify Your Email";
            
            String htmlContent = getEmailVerificationTemplate(code);
            Content content = new Content("text/html", htmlContent);
            
            Mail mail = new Mail(from, subject, to, content);
            
            // Send email via SendGrid
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email verification code sent successfully to: " + email);
            } else {
                System.err.println("Failed to send email verification code. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }
        } catch (IOException e) {
            System.err.println("Error sending email verification code to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sends welcome email to newly registered user (after verification)
     * 
     * @param email User's email address
     */
    public void sendWelcomeEmail(String email) {
        try {
            // Create email
            Email from = new Email(fromEmail);
            Email to = new Email(email);
            String subject = "🎬 Welcome to BookMyShow!";
            
            String htmlContent = getWelcomeEmailTemplate();
            Content content = new Content("text/html", htmlContent);
            
            Mail mail = new Mail(from, subject, to, content);
            
            // Send email via SendGrid
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Welcome email sent successfully to: " + email);
            } else {
                System.err.println("Failed to send welcome email. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }
        } catch (IOException e) {
            System.err.println("Error sending welcome email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sends account deletion confirmation email
     * 
     * @param email User's email address
     */
    public void sendAccountDeletionEmail(String email) {
        try {
            Email from = new Email(fromEmail);
            Email to = new Email(email);
            String subject = "👋 Account Deleted - BookMyShow";
            
            String htmlContent = getAccountDeletionTemplate();
            Content content = new Content("text/html", htmlContent);
            
            Mail mail = new Mail(from, subject, to, content);
            
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Account deletion email sent successfully to: " + email);
            } else {
                System.err.println("Failed to send account deletion email. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }
        } catch (IOException e) {
            System.err.println("Error sending account deletion email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // HTML Email Templates
    
    private String getPasswordResetEmailTemplate(String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .code-box { background: white; border: 2px dashed #667eea; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px; }
                    .code { font-size: 32px; font-weight: bold; color: #667eea; letter-spacing: 5px; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Password Reset</h1>
                    </div>
                    <div class="content">
                        <p>Hello! 👋</p>
                        <p>We received a request to reset your password. Use the verification code below:</p>
                        <div class="code-box">
                            <div class="code">%s</div>
                        </div>
                        <p><strong>⏰ This code will expire in 10 minutes.</strong></p>
                        <p>If you didn't request this code, please ignore this email. Your password will remain unchanged.</p>
                        <p>Stay secure! 🛡️</p>
                        <p>Best regards,<br><strong>BookMyShow Team</strong> 🎬</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
    
    private String getEmailVerificationTemplate(String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .code-box { background: white; border: 2px dashed #f5576c; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px; }
                    .code { font-size: 32px; font-weight: bold; color: #f5576c; letter-spacing: 5px; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✨ Welcome to BookMyShow!</h1>
                    </div>
                    <div class="content">
                        <p>Hello! 👋</p>
                        <p>Thank you for creating an account with us! 🎉</p>
                        <p>To activate your account, please verify your email address using the code below:</p>
                        <div class="code-box">
                            <div class="code">%s</div>
                        </div>
                        <p><strong>⏰ This code will expire in 10 minutes.</strong></p>
                        <p>Once verified, you'll be able to log in and start booking your favorite movies! 🎬</p>
                        <p>If you didn't create an account, please ignore this email.</p>
                        <p>Happy watching! 🍿</p>
                        <p>Best regards,<br><strong>BookMyShow Team</strong> 🎬</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
    
    private String getWelcomeEmailTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .success-box { background: white; border: 2px solid #4CAF50; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px; }
                    .success-icon { font-size: 48px; margin-bottom: 10px; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✨ Welcome to BookMyShow!</h1>
                    </div>
                    <div class="content">
                        <p>Hello! 👋</p>
                        <p>Thank you for verifying your email! 🎉</p>
                        <div class="success-box">
                            <div class="success-icon">✅</div>
                            <p style="font-size: 18px; font-weight: bold; color: #4CAF50; margin: 0;">Your account is now active!</p>
                        </div>
                        <p>You can now log in and start booking your favorite movies! 🎬</p>
                        <p>We're excited to have you with us. Get ready for an amazing movie experience! 🍿</p>
                        <p>Happy watching! 🎥</p>
                        <p>Best regards,<br><strong>BookMyShow Team</strong> 🎬</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
    
    private String getAccountDeletionTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>👋 Account Deleted</h1>
                    </div>
                    <div class="content">
                        <p>Hello! 👋</p>
                        <p>We're sorry to see you go! 😢</p>
                        <p>Your account has been <strong>successfully deleted</strong> from BookMyShow.</p>
                        <p>All your data has been permanently removed from our system. 🔒</p>
                        <p>If you change your mind, you can always create a new account with us anytime! 🎬</p>
                        <p>Thank you for being part of the BookMyShow community. We hope to see you again soon! 🍿</p>
                        <p>Best regards,<br><strong>BookMyShow Team</strong> 🎬</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}

