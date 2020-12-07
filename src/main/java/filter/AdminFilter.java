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
        Utilisateur utilisateur = (Utilisateur) httpRequest.getSession().getAttribute("utilisateurConnecte");
        if (utilisateur==null) {
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.sendRedirect("../accueil");
        }
        else{
            if(utilisateur.isAdmin() == false){
                HttpServletResponse httpResponse = (HttpServletResponse) resp;
                httpResponse.sendRedirect("../accueil");
            }
        }
        chain.doFilter(req,resp);
    }
}
