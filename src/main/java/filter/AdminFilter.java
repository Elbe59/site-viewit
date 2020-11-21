package filter;

import entity.Utilisateur;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String role="lambda";
        Utilisateur utilisateur = (Utilisateur) httpRequest.getSession().getAttribute("utilisateurConnecte");
        if (utilisateur==null) {
            System.out.println("Il faut être administrateur pour accéder à cette page !");
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.sendRedirect("../accueil");
            //role="lambda";
        }
        else{
            if(utilisateur.isAdmin() == false){
                System.out.println("Il faut être administrateur pour accéder à cette page !");
                HttpServletResponse httpResponse = (HttpServletResponse) resp;
                httpResponse.sendRedirect("../accueil");
                //role="user";
            }
            else{
                //role="admin";

            }
        }
        chain.doFilter(req,resp);
        //req.setAttribute("utilisateur",role);

    }

}
