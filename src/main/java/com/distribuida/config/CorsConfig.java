package com.distribuida.config;


import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
@Provider // Anotación para registrar como proveedor de JAX-RS
@WebFilter("/*")
public class CorsConfig implements Filter {
    @Inject
    @ConfigProperty(name="allow.origin")
    private  String allowOrigin;
    @Inject
    @ConfigProperty(name="allow.methods")
    private  String allowMethods;

    @Inject
    @ConfigProperty(name="allow.headers")
    private String allowHeadres;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", allowMethods );
        response.setHeader("Access-Control-Allow-Headers", allowHeadres);
        chain.doFilter(req, res);
    }


}



