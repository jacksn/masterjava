package ru.javaops.masterjava.export;

import org.thymeleaf.TemplateEngine;
import ru.javaops.masterjava.common.web.ThymeleafUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafListener implements ServletContextListener {

    public static TemplateEngine engine;

    public void contextInitialized(ServletContextEvent sce) {
        engine = ThymeleafUtil.getTemplateEngine(sce.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
