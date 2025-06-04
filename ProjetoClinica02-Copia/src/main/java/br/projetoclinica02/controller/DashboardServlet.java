package br.projetoclinica02.controller;

import br.projetoclinica02.model.*;
import br.projetoclinica02.service.ConsultaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final ConsultaService consultaService = new ConsultaService();

    private static final DateTimeFormatter FORM_LABEL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String view;

        if (usuario instanceof Admin) {
            try {
                List<Consulta> consultas = consultaService.listarTodas();
                // Map de horários formatados
                Map<Integer, String> horarioMap = new HashMap<>();
                for (Consulta c : consultas) {
                    horarioMap.put(c.getId(), c.getDataHorario().format(FORM_LABEL));
                }

                req.setAttribute("consultas", consultas);
                req.setAttribute("horarioMap", horarioMap);

            } catch (Exception e) {
                throw new ServletException("Erro ao buscar consultas do admin", e);
            }

            view = "/WEB-INF/pages/admin/consulta.jsp";

        } else if (usuario instanceof Medico) {
            try {
                List<Consulta> consultas = consultaService.listarPorMedico(usuario.getId());
                req.setAttribute("consultas", consultas);
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar consultas do médico", e);
            }

            view = "/WEB-INF/pages/medico/medico_home.jsp";

        } else if (usuario instanceof Paciente) {
            try {
                List<Consulta> consultas = consultaService.listarPorPaciente(usuario.getId());
                req.setAttribute("consultas", consultas);
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar consultas do paciente", e);
            }

            view = "/WEB-INF/pages/paciente/paciente_home.jsp";
        } else {
            view = "/WEB-INF/pages/error/403.jsp";
        }

        req.getRequestDispatcher(view).forward(req, resp);
    }
}

