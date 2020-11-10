package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/home")
public class HomeServlet extends AbstractThymeleafServlet {
	private static final long serialVersionUID = 1L;

    public HomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = this.createTemplateEngine(req.getServletContext());

		// context pour les variables
		WebContext context = new WebContext(req, resp, req.getServletContext());

		engine.process("film", context, resp.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
