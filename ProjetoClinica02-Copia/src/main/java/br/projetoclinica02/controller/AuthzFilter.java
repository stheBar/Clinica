package br.projetoclinica02.controller;

import br.projetoclinica02.model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Set;
@WebFilter("/*")
public class AuthzFilter implements Filter {

    //caminhos EXATOS liberados (comparação com equals)
    private static final Set<String> PUBLIC_EXACT = Set.of(
            "/",                // se quiser que somente a raiz seja pública
            "/index.jsp",
            "/login",
            "/usuario"
    );

    // Prefixos liberados (recursos estáticos), arquivos para front etc...
    private static final String[] PUBLIC_PREFIX = {
            "/css/", "/js/", "/img/"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI().substring(request.getContextPath().length());

        // ROTAS PÚBLICAS
        if (isPublic(uri)) {
            chain.doFilter(req, resp);
            return;
        }

        //LOGIN OBRIGATÓRIO
        HttpSession s = request.getSession(false);
        Usuario user  = (s != null) ? (Usuario) s.getAttribute("usuarioLogado") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        //AUTORIZAÇÃO POR PAPEL
        if (uri.startsWith("/WEB-INF/pages/medico") || uri.startsWith("/medico")) {
            if (!(user instanceof Medico)) { deny(response, uri); return; }
        } else if (uri.startsWith("/WEB-INF/pages/paciente") || uri.startsWith("/paciente")) {
            if (!(user instanceof Paciente)) { deny(response, uri); return; }
        } else if (uri.startsWith("/WEB-INF/pages/admin") || uri.startsWith("/admin")) {
            if (!(user instanceof Admin)) { deny(response, uri); return; }
        }

        chain.doFilter(req, resp); // autorizado
    }

    //auxiliares

    private boolean isPublic(String uri) {
        if (PUBLIC_EXACT.contains(uri)) return true;
        for (String p : PUBLIC_PREFIX) {
            if (uri.startsWith(p)) return true;
        }
        return false;
    }

    private void deny(HttpServletResponse resp, String uri) throws IOException {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Acesso negado para: " + uri);
    }
}
