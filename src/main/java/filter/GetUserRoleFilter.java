package filter;

import entity.Utilisateur;
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
            System.out.println("Personne n'est connecté");
            role="0";
            user="Personne Personne";
        }
        else{
            if(utilisateur.isAdmin() == false){
                System.out.println("Vous êtes simple utilisateur");
                role="1";
            }
            else{
                System.out.println("Vous êtes admin suprême");
                role="2";
            }
            user=utilisateur.getPrenom() + " " +utilisateur.getNom();
            id=utilisateur.getId();
        }
        req.setAttribute("role_value",role);
        req.setAttribute("utilisateur",user);
        req.setAttribute("userCoId",id);
        super.doFilter(req,resp,chain);

    }


}