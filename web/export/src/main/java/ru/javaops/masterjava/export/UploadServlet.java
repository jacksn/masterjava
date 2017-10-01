package ru.javaops.masterjava.export;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet("/")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final int DEFAULT_CHUNK_SIZE = 10;

    private final UserExport userExport = new UserExport();
    private final UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        engine.process("export", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        try {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            Part filePart = req.getPart("fileToUpload");
            try (InputStream is = filePart.getInputStream()) {
                List<User> users = userExport.process(is);

                int chunkSize = DEFAULT_CHUNK_SIZE;

                String chunkSizeParam = req.getParameter("chunkSize");
                try {
                    chunkSize = Integer.parseInt(chunkSizeParam);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                int[] result = userDao.insertAll(users.iterator(), chunkSize);

                int i = 0;
                Iterator<User> iterator = users.iterator();
                while (iterator.hasNext()) {
                    iterator.next();
                    if (result[i] == 1) {
                        iterator.remove();
                    }
                    i++;
                }

                webContext.setVariable("users", users);
                engine.process("result", webContext, resp.getWriter());
            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }
    }
}
