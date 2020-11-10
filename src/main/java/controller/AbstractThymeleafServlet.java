package controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public abstract class AbstractThymeleafServlet extends HttpServlet {

    /**
     * Serial Id
     */
    private static final long serialVersionUID = -7342768479603279018L;

    /**
     * Code commun aux servlets thymeleaf, on l'externalise dans une classe abstraite.
     *
     * @param context : le context de la requete
     * @return le moteur de template initialisé
     */
    protected TemplateEngine createTemplateEngine(ServletContext context) {
        // conf du resolver pour retrouver les templates
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(context);
        resolver.setPrefix("/WEB-INF/Templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);

        // moteur timeleaf
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.addDialect(new Java8TimeDialect());

        return engine;
    }
}