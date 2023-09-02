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


@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${links.unsubscribe}")
    private String unsubscribeLink;



    @Async
    public void sendNewProjects(String email, String name, CardToSendEmail message){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText("NEW PROJECT " + message.getTitle(), true);
            helper.setTo(email);
            helper.setSubject("ON OUR SITE WE HAVE NEW PROJECT FOR YOU");
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
            helper.setSubject("UPDATES");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendVerificationEmail(String to, String link) {
        String email = buildVerificationEmail("name",link);
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
        String email = buildVerificationEmail("name",link);
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


    private String buildVerificationEmailForUpdates(String name, String link){

        return  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\"/>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                "  <title>Document</title>\n" +
                " <style type=\"text/css\">\n" +
                "    *\n" +
                "    {\n" +
                "      padding: 0;\n" +
                "      margin: 0;\n" +
                "      box-sizing: border-box;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    .container\n" +
                "    {\n" +
                "      max-width: 480px;\n" +
                "      width: 100%;\n" +
                "      font-family: e-Ukraine, sans-serif;\n" +
                "    }\n" +
                "\n" +
                "    .header\n" +
                "    {\n" +
                "      background: #F7D67F;\n" +
                "      display: flex;\n" +
                "      padding-left: 16px;\n" +
                "      justify-content: space-between;\n" +
                "    }\n" +
                "\n" +
                "    .header__info\n" +
                "    {\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      justify-content: space-between;\n" +
                "      margin-bottom: 19px;\n" +
                "    }\n" +
                "\n" +
                "    .header__info-logo\n" +
                "    {\n" +
                "      margin-top: 16px;\n" +
                "      width: 66px;\n" +
                "      height: 15px;\n" +
                "    }\n" +
                "\n" +
                "    .header__info-text\n" +
                "    {\n" +
                "      color: #151515;\n" +
                "      font-size: 18px;\n" +
                "      font-style: normal;\n" +
                "      font-weight: 500;\n" +
                "      line-height: 155.556%;\n" +
                "      display: block;\n" +
                "      width: 129px;\n" +
                "    }\n" +
                "\n" +
                "    .hero-img\n" +
                "    {\n" +
                "      width: 171px;\n" +
                "      height: 122px;\n" +
                "      flex-shrink: 0;\n" +
                "      bottom: 0px;\n" +
                "    }\n" +
                "\n" +
                "    .message\n" +
                "    {\n" +
                "      color: #313131;\n" +
                "      padding: 24px 16px;\n" +
                "      font-size: 14px;\n" +
                "      font-style: normal;\n" +
                "      font-weight: 300;\n" +
                "      line-height: 185.714%;\n" +
                "    }\n" +
                "\n" +
                "    .message-title\n" +
                "    {\n" +
                "      color: #000;\n" +
                "      font-size: 24px;\n" +
                "      font-style: normal;\n" +
                "      font-weight: 500;\n" +
                "      line-height: 125%;\n" +
                "      margin-bottom: 16px;\n" +
                "    }\n" +
                "\n" +
                "    .message p:not(:last-child)\n" +
                "    {\n" +
                "      margin-bottom: 16px;\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (min-width: 768px)\n" +
                "    {\n" +
                "      .container\n" +
                "      {\n" +
                "        min-width: 768px;\n" +
                "      }\n" +
                "\n" +
                "      .header\n" +
                "      {\n" +
                "        min-width: 768px;\n" +
                "        padding-left: 32px;\n" +
                "      }\n" +
                "\n" +
                "      .header__info-logo\n" +
                "      {\n" +
                "        margin-top: 32px;\n" +
                "        width: 171px;\n" +
                "        height: 39px;\n" +
                "        margin-bottom: 48px ;\n" +
                "      }\n" +
                "\n" +
                "      .hero-img\n" +
                "      {\n" +
                "        bottom: 20px;\n" +
                "        width: 426px;\n" +
                "        height: 304px;\n" +
                "        margin-top: 54px;\n" +
                "      }\n" +
                "\n" +
                "      .header__info-text\n" +
                "      {\n" +
                "        font-size: 44px;\n" +
                "        font-style: normal;\n" +
                "        font-weight: 700;\n" +
                "        line-height: 130%;\n" +
                "        margin-bottom: 125px;\n" +
                "      }\n" +
                "\n" +
                ".unsubscribe {\n" +
                "  text-align: center;\n" +
                "  margin-top: 20px;\n" +
                "}\n" +
                "\n" +
                ".unsubscribe a.unsubscribe-link {\n" +
                "  font-size: 16px;\n" +
                "  color: grey;\n" +
                "  text-decoration: none;\n" +
                "  transition: color 0.3s ease-in-out;\n" +
                "}\n" +
                "\n" +
                ".unsubscribe a.unsubscribe-link:hover {\n" +
                "  color: #333;\n" +
                "  text-decoration: underline;\n" +
                "  cursor: pointer;\n" +
                "}" +
                "      .message\n" +
                "      {\n" +
                "        color: #000;\n" +
                "        font-size: 18px;\n" +
                "        font-style: normal;\n" +
                "        font-weight: 300;\n" +
                "        line-height: 155.556%;\n" +
                "        padding: 64px 32px;\n" +
                "      }\n" +
                "\n" +
                "      .message-title\n" +
                "      {\n" +
                "        color: #000;\n" +
                "        font-size: 32px;\n" +
                "        font-style: normal;\n" +
                "        font-weight: 700;\n" +
                "        line-height: 125%;\n" +
                "        margin-bottom: 32px;\n" +
                "      }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (min-width: 960px)\n" +
                "    {\n" +
                "      .container\n" +
                "      {\n" +
                "        max-width: 960px;\n" +
                "        width: 100%;\n" +
                "      }\n" +
                "\n" +
                "      .header\n" +
                "      {\n" +
                "        max-width: 960px;\n" +
                "      }\n" +
                "\n" +
                "      .header__info-logo\n" +
                "      {\n" +
                "        width: 246px;\n" +
                "        height: 56px;\n" +
                "        margin-bottom: 48px;\n" +
                "      }\n" +
                "\n" +
                "      .hero-img\n" +
                "      {\n" +
                "        max-width: 528px;\n" +
                "        max-height: 377px;\n" +
                "        margin-top: 53px;\n" +
                "        margin-left: 39px;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "  <div class=\"header\">\n" +
                "    <div class=\"header__info\">\n" +
                "      <img\n" +
                "          class=\"header__info-logo\"\n" +
                "          src=\"https://lh3.googleusercontent.com/drive-viewer/AITFw-zxqC--B883b41sQ5LtWx4IIEE8gZYfhh_P4LMMYiz1aVE7g34NyW9-9XDxoO3BdD9VjIKjLH-4UHCg58TQQczH1Zto=s2560\"\n" +
                "          width=\"246px\"\n" +
                "          height=\"56px\"\n" +
                "          alt=\"Logo\"\n" +
                "      />\n" +
                "      <h1 class=\"header__info-text\">Захистив. Захистимо.</h1>\n" +
                "    </div>\n" +
                "    <div>\n" +
                "      <img\n" +
                "          class=\"hero-img\"\n" +
                "          src=\"https://lh3.googleusercontent.com/drive-viewer/AITFw-xp0LtwkVHsQVYOS80TP-zPWzY1pzNvqoSoxeoG1713gERM5f6VeQNzLIc3JlXYUGQMXJ5d8S8c_ujBx-xny3_A_x-Z6g=s2560\"\n" +
                "          alt=\"hero\"\n" +
                "      />\n" +
                "    </div>\n" +
                "\n" +
                "  </div>\n" +
                "  <div class=\"message\">\n" +
                "    <h2 class=\"message-title\">Привіт " + name + ",</h2>\n" +
                "    <p>Вітаємо у нашій спільноті та дякуємо, що приєдналися до нас.</p>\n" +
                "    <p>Тепер ви будете одним із перших, хто дізнається про нові ветеранські проекти, благодійні ініціативи та події,\n" +
                "      спрямовані на допомогу тим, хто служив з відданістю.</p>\n" +
                "    <p>Ми прагнемо створити середовище, де ви будете знаходити інформацію, яка має значення для вас, і де ваші думки та\n" +
                "      ідеї завжди важливі. Разом ми можемо зробити більше для підтримки ветеранів та зміцнення ветеранської\n" +
                "      спільноти. </p>\n" +
                "    <p>Залишайтеся з нами та долучайтеся до нашого ветеранського шляху. Разом ми створюємо майбутнє, достойне наших\n" +
                "      героїв.</p>\n" +
                "    <p>З вдячністю та повагою, Ваш ХИСТ!</p>\n" +
                "<div class=\"unsubscribe\">\n" +
                "  <a href=\"" + link + "\" class=\"unsubscribe-link\">відписатись</a>\n" +
                "</div>" +
                "  </div>\n" +
                "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n" +
                "<script>\n" +
                "(function() {\n" +
                "  var ws = new WebSocket('ws://' + window.location.host + \n" +
                "             '/jb-server-page?reloadMode=RELOAD_ON_SAVE&'+\n" +
                "             'referrer=' + encodeURIComponent(window.location.pathname));\n" +
                "  ws.onmessage = function (msg) {\n" +
                "      if (msg.data === 'reload') {\n" +
                "          window.location.reload();\n" +
                "      }\n" +
                "      if (msg.data.startsWith('update-css ')) {\n" +
                "          var messageId = msg.data.substring(11);\n" +
                "          var links = document.getElementsByTagName('link');\n" +
                "          for (var i = 0; i < links.length; i++) {\n" +
                "              var link = links[i];\n" +
                "              if (link.rel !== 'stylesheet') continue;\n" +
                "              var clonedLink = link.cloneNode(true);\n" +
                "              var newHref = link.href.replace(/(&|\\?)jbUpdateLinksId=\\d+/, \"$1jbUpdateLinksId=\" + messageId);\n" +
                "              if (newHref !== link.href) {\n" +
                "                clonedLink.href = newHref;\n" +
                "              }\n" +
                "              else {\n" +
                "                var indexOfQuest = newHref.indexOf('?');\n" +
                "                if (indexOfQuest >= 0) {\n" +
                "                  // to support ?foo#hash \n" +
                "                  clonedLink.href = newHref.substring(0, indexOfQuest + 1) + 'jbUpdateLinksId=' + messageId + '&' + \n" +
                "                                    newHref.substring(indexOfQuest + 1);\n" +
                "                }\n" +
                "                else {\n" +
                "                  clonedLink.href += '?' + 'jbUpdateLinksId=' + messageId;\n" +
                "                }\n" +
                "              }\n" +
                "              link.replaceWith(clonedLink);\n" +
                "          }\n" +
                "      }\n" +
                "  };\n" +
                "})();\n" +
                "</script>";

    }





    private String buildVerificationEmail(String name, String link) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
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
