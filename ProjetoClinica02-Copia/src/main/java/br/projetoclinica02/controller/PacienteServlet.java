package br.projetoclinica02.controller;

import br.projetoclinica02.model.Consulta;
import br.projetoclinica02.model.Paciente;
import br.projetoclinica02.service.ConsultaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/paciente")
public class PacienteServlet extends HttpServlet {

    private final ConsultaService consultaService = new ConsultaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Paciente paciente = (session != null) ? (Paciente) session.getAttribute("usuarioLogado") : null;

        if (paciente == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        try {
            if ("prontuario".equals(action)) {
                //exibe todas as consultas do paciente com todos os médicos
                List<Consulta> consultas = consultaService.listarPorPaciente(paciente.getId());
                req.setAttribute("consultas", consultas);
                req.getRequestDispatcher("/WEB-INF/pages/paciente/prontuario_paciente.jsp").forward(req, resp);

            } else {
                //Tela padrão: consultas agendadas
                List<Consulta> consultas = consultaService.listarPorPaciente(paciente.getId());
                req.setAttribute("consultas", consultas);
                req.getRequestDispatcher("/WEB-INF/pages/paciente/paciente_home.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException("Erro ao buscar dados do paciente", e);
        }
    }
}
