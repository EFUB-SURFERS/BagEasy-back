package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;  //TODO : 메일 발송 기능 (MailService로 분리)

    public void sendMessage(Member receiver, Member sender) throws MessagingException {
        String emailSubject = "[BagEasy] 읽지 않은 채팅이 있습니다.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

        messageHelper.setTo(receiver.getEmail());
        messageHelper.setSubject(emailSubject);
        messageHelper.setText(setContext(receiver.getNickname(),sender.getNickname()), true);
        javaMailSender.send(message);
    }

    private String setContext(String receiver, String sender){
        Context context = new Context();
        context.setVariable("receiver", receiver);
        context.setVariable("sender", sender);
        return templateEngine.process("email-form.html", context);
    }
}
