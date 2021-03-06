package filter;

import entity.Utilisateur;
import exception.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UtilisateurService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class GetUserRoleFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LogManager.getLogger();

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