package ru.javaops.masterjava.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet("/upload")
@MultipartConfig(location = "/tmp")
public class FileUploadServlet extends HttpServlet {
    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    private static TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process("fileUpload", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Part> parts = request.getParts();
        Iterator<Part> partsIterator = parts.iterator();
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Set<User> users = new TreeSet<>(USER_COMPARATOR);
        if (partsIterator.hasNext()) {
            try (InputStream stream = partsIterator.next().getInputStream();
                 StaxStreamProcessor processor = new StaxStreamProcessor(stream)) {
                while (processor.startElement("User", null)) {
                    User user = new User();
                    user.setFlag(FlagType.fromValue(processor.getAttribute("flag")));
                    user.setEmail(processor.getAttribute("email"));
                    user.setValue(processor.getText());
                    users.add(user);
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        if (!users.isEmpty()) {
            ctx.setVariable("users", users);
            templateEngine.process("users", ctx, response.getWriter());
        } else {
            response.sendRedirect("/upload");
        }
    }
}