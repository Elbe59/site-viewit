package controller;

import service.FileStorageService;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/filmimage")
public class DisplayImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = LogManager.getLogger();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idFilm = Integer.parseInt(request.getParameter("id"));
        LOGGER.debug("loading image of movie "+idFilm);
        response.setContentType("image/jpeg");
        ServletOutputStream out;
        out = response.getOutputStream();
        FileInputStream fin= FileStorageService.displayImage(idFilm);
        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int ch = 0;
        ;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fin.close();
        bout.close();
        out.close();
    }
}
