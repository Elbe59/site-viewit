package controller;

import entity.Film;
import entity.Genre;
import entity.Utilisateur;
import exception.FilmAlreadyExistingException;
import exception.UserAlreadyExistingException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import service.FilmService;
import service.UtilisateurService;
import utils.MotDePasseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@MultipartConfig
@WebServlet("/admin/ajoutuser")
public class AddUserServlet extends ServletGenerique {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nom = req.getParameter("nom");
		String prenom = req.getParameter("prenom");
		String password = req.getParameter("mdp");
		String email = req.getParameter("mail");
		String passwordHash= password;

		Utilisateur user=new Utilisateur(1,prenom,nom,email,password,passwordHash,false);
		try {
			UtilisateurService.getInstance().addUser(user);
		} catch (UserAlreadyExistingException e) {
			e.printStackTrace();
		}

		resp.sendRedirect("gestionuser");
	}

}
