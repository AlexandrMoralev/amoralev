package ru.job4j.mvc;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.job4j.templatedata.AppConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class, AppConfig.class);
        ctx.refresh();
        DispatcherServlet dispatcher = new DispatcherServlet(ctx);
        ServletRegistration.Dynamic registration = servletContext.addServlet("app", dispatcher);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
