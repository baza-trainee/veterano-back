package com.zdoryk.email;


import com.zdoryk.util.CardToSendEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${links.unsubscribe}")
    private String unsubscribeLink;

    @Value("${links.site}")
    private String redirectToProjectLink;



    @Async
    public void sendNewProjects(
            String email,
            String name,
            CardToSendEmail message
    ){
        String emailToSend = buildEmailAboutNewProject(
                name,
                redirectToProjectLink+message.getUrl(),
                message.getTitle(),
                email
        );

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailToSend, true);
            helper.setTo(email);
            helper.setSubject("На нашому сайті новий проект");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendNotificationVerificationEmail(String email,String name){
            String html = buildVerificationEmailForUpdates(name,unsubscribeLink + email);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(html, true);
            helper.setTo(email);
            helper.setSubject("Дякуємо за підписку");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendVerificationEmail(String to, String link) {
        String email = buildVerificationEmail(link);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }


    @Async
    public void sendResetPasswordEmail(String to, String link){
        String email = buildVerificationEmail(link);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Reset Password");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }



    private String buildEmailAboutNewProject(String name,
                                             String projectLink,
                                             String projectName,
                                             String email
    ) {
        return  "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">" +
                "<head>" +

                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<meta name=\"x-apple-disable-message-reformatting\">" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "<title></title>" +

                "<style type=\"text/css\">" +
                "@media only screen and (min-width: 620px) {" +
                ".u-row {width: 600px !important;}" +
                ".u-row .u-col {vertical-align: top;}" +

                ".u-row .u-col-100 {width: 600px !important;}" +

                "}" +

                "@media (max-width: 620px) {" +
                ".u-row-container {max-width: 100% !important; padding-left: 0px !important; padding-right: 0px !important;}" +
                ".u-row .u-col {min-width: 320px !important; max-width: 100% !important; display: block !important;}" +
                ".u-row {width: 100% !important;}" +
                ".u-col {width: 100% !important;}" +
                ".u-col > div {margin: 0 auto;}" +
                "}" +
                "body {margin: 0; padding: 0;}" +

                "table," +
                "tr," +
                "td {vertical-align: top; border-collapse: collapse;}" +

                "p {margin: 0;}" +

                ".ie-container table," +
                ".mso-container table {table-layout: fixed;}" +

                "* {line-height: inherit;}" +

                "a[x-apple-data-detectors='true'] {" +
                "color: inherit !important;" +
                "text-decoration: none !important;" +
                "}" +

                "@media (min-width: 481px) and (max-width: 768px) {}" +

                "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_text_2 .v-container-padding-padding { padding: 30px 10px 10px !important; } #u_content_button_1 .v-size-width { width: 65% !important; } #u_content_button_1 .v-text-align { text-align: left !important; } #u_content_button_1 .v-border-radius { border-radius: 4px !important;-webkit-border-radius: 4px !important; -moz-border-radius: 4px !important; } #u_content_text_5 .v-container-padding-padding { padding: 10px 10px 30px !important; } }" +
                "</style>" +


                "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Raleway:400,700\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->" +

                "</head>" +

                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">" +


                "<table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\" cellpadding=\"0\" cellspacing=\"0\">" +
                "<tbody>" +
                "<tr style=\"vertical-align: top\">" +
                "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">" +


                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +

                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"height: 100%;width: 100% !important;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->" +

                "<table style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "<tr>" +
                "<td class=\"v-text-align\" style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">" +

                "<img align=\"center\" border=\"0\" src=\"https://lh3.googleusercontent.com/drive-viewer/AITFw-wxZtQwf78YqvU_9991hek7V3SctQ7EhwE0sJuP0E9e9mIU5_fgP61JO5menWmc9uk93NFoXs9E33UiOEJ8qUTFpGhn=s1600\" alt=\"image\" title=\"image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 600px;\" width=\"600\"/>" +

                "</td>" +
                "</tr>" +
                "</table>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +

                "</div>" +
                "</div>" +
                "</div>" +



                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">" +

                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"background-color: #f7f7f8;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->" +

                "<table id=\"u_content_text_2\" style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 50px 30px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<div class=\"v-text-align\" style=\"font-size: 14px; line-height: 140%; text-align: justify; word-wrap: break-word;\">" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 22.4px;\"><strong>Привіт, " + name+ "</strong></span></p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\">Ми додали новий проєкт на нашому сайті <span style=\"color: #34495e; line-height: 19.6px;\"><a rel=\"noopener\" href=\"https://hyst.site\" target=\"_blank\" style=\"color: #34495e;\">hyst.site</a></span>:</p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; font-size: 14px; line-height: 19.6px;\">" + projectName + "                                                   " + (LocalDate.now().getMonth().getValue() < 10 ? "0" + LocalDate.now().getMonth().getValue() : LocalDate.now().getMonth().getValue()) + "." + LocalDate.now().getYear() + "</span></p><br/><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 14px; line-height: 19.6px; font-family: 'Open Sans', sans-serif;\"><span style=\"font-size: 14px; line-height: 19.6px;\">З вдячністю та повагою, Ваш ХИСТ!</span></span><span style=\"font-family: 'Open Sans', sans-serif; font-size: 14px; line-height: 19.6px;\"><span style=\"font-size: 14px; line-height: 19.6px;\"></span></span></p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\">" +
                "<p style=\"line-height: 140%; font-size: 14px;\">" +
                "<span style=\"font-size: 14px; line-height: 19.6px; font-family: 'Open Sans', sans-serif;\">" +
                "<a href=\"mailto:info@baza-trainee.tech\" style=\"color: gray;\"> ✉️   info@baza-trainee.tech</a>" +
                "</span>" +
                "</p>" +
                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +

                "<table id=\"u_content_button_1\" style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<div class=\"v-text-align\" align=\"center\">" +

                "<a href=\"" + projectLink + "\" target=\"_blank\" class=\"v-button v-size-width v-border-radius\" style=\"box-sizing: border-box;display: inline-block;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #000000; border-radius: 25px;-webkit-border-radius: 25px; -moz-border-radius: 25px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;border-top-color: #CCC; border-top-style: solid; border-top-width: 0px; border-left-color: #CCC; border-left-style: solid; border-left-width: 0px; border-right-color: #CCC; border-right-style: solid; border-right-width: 0px; border-bottom-color: #CCC; border-bottom-style: solid; border-bottom-width: 0px;font-size: 14px;\">" +
                "<span style=\"display:block;padding:10px 20px;line-height:110%;\"><strong><span style=\"font-size: 14px; line-height: 15.4px;\">Детальніше</span></strong></span>" +
                "</a>" +

                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +

                "</div>" +
                "</div>" +
                "</div>" +



                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">" +

                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->" +

                "<table style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 0px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">" +
                "<tbody>" +
                "<tr style=\"vertical-align: top\">" +
                "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">" +
                "<span>&#160;</span>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +

                "<table id=\"u_content_text_5\" style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 40px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<div class=\"v-text-align\" style=\"font-size: 14px; line-height: 160%; text-align: center; word-wrap: break-word;\">" +
                "<p style=\"font-size: 14px; line-height: 160%;\"><span style=\"color: #95a5a6; line-height: 22.4px;\"><a rel=\"noopener\" href=\"" + unsubscribeLink+email + "\" target=\"_blank\" style=\"color: #95a5a6;\">Відписатись</a></span></p><br/>" +
                "<p style=\"font-size: 14px; line-height: 160%;\"> </p>" +
                "<p style=\"font-size: 14px; line-height: 160%;\">Розробка Baza Trainee Ukraine 2023 @ Всі права захищені</p>" +
                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +


                "</div>" +
                "</div>" +
                "</div>" +



                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</body>" +

                "</html>";

    }


    private String buildVerificationEmailForUpdates(String name, String link) {

        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">" +
                "<head>" +

                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<meta name=\"x-apple-disable-message-reformatting\">" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "<title></title>" +

                "<style type=\"text/css\">" +
                "@media only screen and (min-width: 620px) {" +
                ".u-row {width: 600px !important;}" +
                ".u-row .u-col {vertical-align: top;}" +

                ".u-row .u-col-100 {width: 600px !important;}" +

                "}" +

                "@media (max-width: 620px) {" +
                ".u-row-container {max-width: 100% !important; padding-left: 0px !important; padding-right: 0px !important;}" +
                ".u-row .u-col {min-width: 320px !important; max-width: 100% !important; display: block !important;}" +
                ".u-row {width: 100% !important;}" +
                ".u-col {width: 100% !important;}" +
                ".u-col > div {margin: 0 auto;}" +
                "}" +
                "body {margin: 0; padding: 0;}" +

                "table," +
                "tr," +
                "td {vertical-align: top; border-collapse: collapse;}" +

                "p {margin: 0;}" +

                ".ie-container table," +
                ".mso-container table {table-layout: fixed;}" +

                "* {line-height: inherit;}" +

                "a[x-apple-data-detectors='true'] {" +
                "color: inherit !important;" +
                "text-decoration: none !important;" +
                "}" +

                "@media (min-width: 481px) and (max-width: 768px) {}" +

                "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_text_2 .v-container-padding-padding { padding: 30px 10px 10px !important; } #u_content_text_5 .v-container-padding-padding { padding: 10px 10px 30px !important; } }" +
                "</style>" +


                "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Raleway:400,700\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->" +

                "</head>" +

                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">" +


                "<table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\" cellpadding=\"0\" cellspacing=\"0\">" +
                "<tbody>" +
                "<tr style=\"vertical-align: top\">" +
                "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">" +


                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">" +

                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"height: 100%;width: 100% !important;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->" +

                "<table style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "<tr>" +
                "<td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">" +

                "<img align=\"center\" border=\"0\" src=\"https://lh3.googleusercontent.com/drive-viewer/AITFw-yFHax_uE30FRpqzp9tA0VwYx4nCJ2UNRMU902FtEkaabP4OAFlkPZwlAFGjYudCpK69n_LBHzdBAbLr-4t3F1nIZhOhw=s1600\" alt=\"image\" title=\"image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 600px;\" width=\"600\"/>" +

                "</td>" +
                "</tr>" +
                "</table>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +

                "</div>" +
                "</div>" +
                "</div>" +


                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">" +

                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"background-color: #f7f7f8;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->" +

                "<table id=\"u_content_text_2\" style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 50px 30px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<div style=\"font-size: 14px; line-height: 140%; text-align: justify; word-wrap: break-word;\">" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; font-size: 16px; line-height: 22.4px;\"><strong>Привіт, " + name + "!</strong></span></p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"color: #e03e2d; font-size: 16px; line-height: 22.4px; font-family: 'Open Sans', sans-serif;\"><strong><span style=\"line-height: 22.4px; font-size: 16px;\"> </span></strong></span></p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; line-height: 19.6px;\">Вітаємо у нашій спільноті та дякуємо що приєдналися до нас.</span></p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; font-size: 14px; line-height: 19.6px;\">Тепер ви будете одним із перших, хто дізнається про нові ветеранські проєкти, благодійні ініціативи та події, спрямовані на допомогу тим, хто служив з відданістю.<br /><br />Ми прагнемо створити середовище, де ви будете знаходити інформацію, яка має значення для вас, і де ваші думки та ідеї завжди важливі. Разом ми можемо зробити більше для підтримки ветеранів та зміцнення ветеранської спільноти.<br /><br />Залишайтеся з нами та долучайтеся до нашого ветеранського шляху. Разом ми створюємо майбутнє, достойне наших героїв. </span></p><br/>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"> </p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-size: 14px; line-height: 19.6px; font-family: 'Open Sans', sans-serif;\"><strong><span style=\"font-size: 14px; line-height: 19.6px;\">З вдячністю та повагою</span></strong></span></p>" +
                "<p style=\"line-height: 140%; font-size: 14px;\"><span style=\"font-family: 'Open Sans', sans-serif; line-height: 19.6px;\"><strong>Ваш ХИСТ!</strong></span></p>" +
                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +

                "</div>" +
                "</div>" +
                "</div>" +


                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">" +
                "<div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">" +
                "<!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->" +

                "<table style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 0px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">" +
                "<tbody>" +
                "<tr style=\"vertical-align: top\">" +
                "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">" +
                "<span>&#160;</span>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +

                "<table id=\"u_content_text_5\" style=\"font-family:'Raleway',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">" +
                "<tbody>" +
                "<tr>" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 40px;font-family:'Raleway',sans-serif;\" align=\"left\">" +

                "<div style=\"font-size: 14px; line-height: 160%; text-align: center; word-wrap: break-word;\">" +
                "<p style=\"font-size: 14px; line-height: 160%;\"><span style=\"color: #95a5a6; line-height: 22.4px;\"><a rel=\"noopener\" href=\""+link+"\" target=\"_blank\" style=\"color: #95a5a6;\"><span style=\"background-color: #ecf0f1; line-height: 22.4px;\">Відписатись</span></a></span></p>" +
                "<p style=\"font-size: 14px; line-height: 160%;\"> </p>" +
                "<p style=\"font-size: 14px; line-height: 160%;\"><span style=\"color: #000000; font-family: e-Ukraine, sans-serif; font-size: 12px; font-weight: 300; text-align: left; white-space: normal; background-color: #ffffff; text-decoration-line: underline; float: none; display: inline; line-height: 19.2px;\">Розробка Baza Trainee Ukraine 2023 @ Всі права захищені</span></p>" +
                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +


                "</div>" +
                "</div>" +


                "</div>" +
                "</div>" +
                "</div>" +

                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +
                "</body>" +

                "</html>";

    }



    private String buildVerificationEmail(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi" + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}

