package filter;

import dao.impl.FileStorageProvider;
import entity.Utilisateur;
import exception.UserNotFoundException;
import service.UtilisateurService;

import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class GetUserRoleFilter extends HttpFilter {

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String role="0";
        String user="";
        int id = 0;
        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
        if (utilisateur==null) {
            role="0";
            user="Personne Personne";
        }
        else{
        	id=utilisateur.getId();
            if(utilisateur.isAdmin() == false){
                role="1";
            }
            else{
                role="2";
            }
            try {
				user=UtilisateurService.getInstance().getUser(id).getPrenom() + " " + UtilisateurService.getInstance().getUser(id).getNom();
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}  
        }
        req.setAttribute("role_value",role);
        req.setAttribute("utilisateur",user);
        req.setAttribute("userCoId",id);
        super.doFilter(req,resp,chain);
    }
}