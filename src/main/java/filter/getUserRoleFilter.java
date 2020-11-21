/*package filter;

import entity.Utilisateur;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class getUserRoleFilter implements Filter{

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String role="lambda";
        Utilisateur utilisateur = (Utilisateur) httpRequest.getSession().getAttribute("utilisateurConnecte");
        if (utilisateur==null) {
            System.out.println("Personne n'est connecté");
            role="lambda";
        }
        else{
            if(utilisateur.isAdmin() == false){
                System.out.println("Vous êtes simple utilisateur");
                role="user";
            }
            else{
                System.out.println("Vous êtes admin suprême");
                role="admin";
            }
        }
        chain.doFilter(req,resp);
        httpRequest.getSession().setAttribute("user_role",role);
    }


}
*/