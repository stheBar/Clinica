package br.projetoclinica02.controller;

import br.projetoclinica02.model.*;
import br.projetoclinica02.service.ConsultaService;
import br.projetoclinica02.service.PacienteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/medico")
public class MedicoServlet extends HttpServlet {

    private final ConsultaService consultaService = new ConsultaService();
    private final PacienteService pacienteService = new PacienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Medico medico = (session != null) ? (Medico) session.getAttribute("usuarioLogado") : null;

        if (medico == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        try {
            if ("editar".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Consulta consulta = consultaService.buscarPorId(id);
                if (consulta != null && consulta.getMedico().getId() == medico.getId()) {
                    req.setAttribute("consulta", consulta);
                    req.getRequestDispatcher("/WEB-INF/pages/medico/editar_consulta.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Consulta não autorizada.");
                }

            } else if ("meusPacientes".equals(action)) {
                List<Paciente> pacientes = consultaService.listarPacientesAtendidosPorMedico(medico.getId());
                req.setAttribute("pacientes", pacientes);
                req.getRequestDispatcher("/WEB-INF/pages/medico/meus_pacientes.jsp").forward(req, resp);

            } else if ("prontuario".equals(action)) {
                int pacienteId = Integer.parseInt(req.getParameter("id"));
                Paciente paciente = pacienteService.buscarPorId(pacienteId);
                List<Consulta> consultas = consultaService.listarPorPaciente(pacienteId);

                //segurança: só acessa se tiver consulta com esse médico
                boolean possuiConsultaComMedico = false;
                for (Consulta c : consultas) {
                    if (c.getMedico().getId() == medico.getId()) {
                        possuiConsultaComMedico = true;
                        break;
                    }
                }

                if (!possuiConsultaComMedico) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso não autorizado ao prontuário.");
                    return;
                }

                //filtrar somente consultas com este medico
                List<Consulta> historico = new ArrayList<>();
                for (Consulta c : consultas) {
                    if (c.getMedico().getId() == medico.getId()) {
                        historico.add(c);
                    }
                }

                req.setAttribute("paciente", paciente);
                req.setAttribute("consultas", historico);
                req.getRequestDispatcher("/WEB-INF/pages/medico/prontuario.jsp").forward(req, resp);

            } else {
                // Padrão: listar consultas
                List<Consulta> consultas = consultaService.listarPorMedico(medico.getId());
                req.setAttribute("consultas", consultas);
                req.getRequestDispatcher("/WEB-INF/pages/medico/medico_home.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException("Erro no módulo médico", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Medico medico = (session != null) ? (Medico) session.getAttribute("usuarioLogado") : null;

        if (medico == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("salvar".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String status = req.getParameter("status");
                String observacao = req.getParameter("observacao");

                Consulta consulta = consultaService.buscarPorId(id);

                if (consulta != null && consulta.getMedico().getId() == medico.getId()) {
                    consulta.setStatus(status);
                    consulta.setPbservacao(observacao);
                    consultaService.atualizarConsulta(consulta);
                    resp.sendRedirect(req.getContextPath() + "/medico?action=consultas");
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Consulta não autorizada.");
                }

            } catch (Exception e) {
                throw new ServletException("Erro ao salvar consulta", e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida.");
        }
    }
}
