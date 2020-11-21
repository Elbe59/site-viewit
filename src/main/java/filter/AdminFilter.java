package filter;/*package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String identifiant = (String) httpRequest.getSession().getAttribute("utilisateurConnecte");
        if (identifiant == null || "".equals(identifiant)) {
            System.out.println("Il faut être administrateur pour accéder à cette page !");
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.sendRedirect("accueil");
            return;
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}*/
