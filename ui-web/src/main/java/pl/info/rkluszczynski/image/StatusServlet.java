package pl.info.rkluszczynski.image;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//@WebServlet(name = "statusServlet", urlPatterns = "/")
//@WebServlet(name = "statusServlet", value = "/", loadOnStartup = 1)
public class StatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().write("OK".getBytes());
    }
}
