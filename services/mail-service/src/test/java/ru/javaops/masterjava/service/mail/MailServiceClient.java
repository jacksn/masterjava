package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: gkislin
 * Date: 28.05.2014
 */
public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException {
        QName qname = new QName("http://mail.service.masterjava.javaops.ru/", "MailServiceImplService");
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.service.masterjava.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);
        mailService.sendMail(ImmutableList.of(new Addressee("gkislin@yandex.ru", null)), null, "Subject", "Body");
    }
}
