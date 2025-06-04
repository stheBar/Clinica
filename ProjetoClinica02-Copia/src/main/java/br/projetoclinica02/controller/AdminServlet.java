package br.projetoclinica02.controller;

import br.projetoclinica02.model.Consulta;
import br.projetoclinica02.model.Medico;
import br.projetoclinica02.model.Paciente;
import br.projetoclinica02.service.ConsultaService;
import br.projetoclinica02.service.MedicoService;
import br.projetoclinica02.service.PacienteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final ConsultaService consultaService = new ConsultaService();
    private final MedicoService medicoService = new MedicoService();
    private final PacienteService pacienteService = new PacienteService();

    private static final DateTimeFormatter FORM_LOCAL = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter FORM_LABEL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();  // exemplo: /consulta, /medico, /paciente

        if (path == null || path.equals("/")) {
            // Redirecionamento aqui
            resp.sendRedirect(req.getContextPath() + "/admin/consulta");
            return;
        }

        try {
            switch (path) {
                case "/consulta":
                    handleConsultaGet(req, resp);
                    break;
                case "/medico":
                    handleMedicoGet(req, resp);
                    break;
                case "/paciente":
                    handlePacienteGet(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erro: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        try {
            switch (path) {
                case "/consulta":
                    handleConsultaPost(req, resp);
                    break;
                case "/medico":
                    handleMedicoPost(req, resp);
                    break;
                case "/paciente":
                    handlePacientePost(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erro ao processar: " + e.getMessage());
            doGet(req, resp);
        }
    }

    // ─────────────── CONSULTA ───────────────
    private void handleConsultaGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");

        if ("new".equals(action)) {
            req.setAttribute("medicos", medicoService.listarTodos());
            req.setAttribute("pacientes", pacienteService.listarTodos());
            req.setAttribute("dataHorarioValue", "");
            req.getRequestDispatcher("/WEB-INF/pages/admin/consulta_form.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Consulta c = consultaService.buscarPorId(id);
            req.setAttribute("consulta", c);
            req.setAttribute("medicos", medicoService.listarTodos());
            req.setAttribute("pacientes", pacienteService.listarTodos());
            req.setAttribute("dataHorarioValue", c.getDataHorario().format(FORM_LOCAL));
            req.getRequestDispatcher("/WEB-INF/pages/admin/consulta_form.jsp").forward(req, resp);
        } else {
            List<Consulta> list = consultaService.listarTodas();
            Map<Integer, String> horarioMap = new HashMap<>();
            for (Consulta c : list) {
                horarioMap.put(c.getId(), c.getDataHorario().format(FORM_LABEL));
            }
            req.setAttribute("consultas", list);
            req.setAttribute("horarioMap", horarioMap);
            req.getRequestDispatcher("/WEB-INF/pages/admin/consulta.jsp").forward(req, resp);
        }
    }

    private void handleConsultaPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");

        if ("create".equals(action) || "update".equals(action)) {
            Consulta c = populaConsulta(req);
            Integer ignorarId = "update".equals(action) ? Integer.parseInt(req.getParameter("id")) : null;

            boolean conflitoMedico = consultaService.existeConflitoHorario(c.getMedico().getId(), c.getDataHorario(), ignorarId);
            boolean conflitoPaciente = consultaService.existeConflitoHorarioPaciente(c.getPaciente().getId(), c.getDataHorario(), ignorarId);

            if (conflitoMedico || conflitoPaciente) {
                String erro = conflitoMedico
                        ? "Já existe uma consulta marcada para este médico nesse mesmo horário."
                        : "O paciente já possui uma consulta nesse mesmo horário.";
                req.setAttribute("error", erro);
                req.setAttribute("consulta", c);
                req.setAttribute("medicos", medicoService.listarTodos());
                req.setAttribute("pacientes", pacienteService.listarTodos());
                req.setAttribute("dataHorarioValue", c.getDataHorario().format(FORM_LOCAL));
                req.getRequestDispatcher("/WEB-INF/pages/admin/consulta_form.jsp").forward(req, resp);
                return;
            }

            if ("create".equals(action)) consultaService.criarConsulta(c);
            else {
                c.setId(Integer.parseInt(req.getParameter("id")));
                consultaService.atualizarConsulta(c);
            }
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            consultaService.removerConsulta(id);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/consulta");
    }

    private Consulta populaConsulta(HttpServletRequest req) throws Exception {
        Consulta c = new Consulta();
        c.setStatus(req.getParameter("status"));
        c.setPbservacao(req.getParameter("observacao"));
        c.setTipoConsulta(req.getParameter("tipoConsulta"));
        c.setDataHorario(java.time.LocalDateTime.parse(req.getParameter("dataHorario"), FORM_LOCAL));
        c.setMedico(medicoService.buscarPorId(Integer.parseInt(req.getParameter("medicoId"))));
        c.setPaciente(pacienteService.buscarPorId(Integer.parseInt(req.getParameter("pacienteId"))));
        return c;
    }

    // ─────────────── MÉDICO ───────────────
    private void handleMedicoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");

        if ("new".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/pages/admin/medico_form.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Medico m = medicoService.buscarPorId(id);
            req.setAttribute("medico", m);
            req.getRequestDispatcher("/WEB-INF/pages/admin/medico_form.jsp").forward(req, resp);
        } else {
            req.setAttribute("medicos", medicoService.listarTodos());
            req.getRequestDispatcher("/WEB-INF/pages/admin/medico.jsp").forward(req, resp);
        }
    }

    private void handleMedicoPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");
        Medico m = populaMedico(req);

        if ("create".equals(action)) {
            medicoService.criarMedico(m);
        } else if ("update".equals(action)) {
            m.setId(Integer.parseInt(req.getParameter("id")));
            medicoService.atualizarMedico(m);
        } else if ("delete".equals(action)) {
            medicoService.removerMedico(Integer.parseInt(req.getParameter("id")));
        }

        resp.sendRedirect(req.getContextPath() + "/admin/medico");
    }

    private Medico populaMedico(HttpServletRequest req) {
        Medico m = new Medico();
        m.setEmail(req.getParameter("email"));
        m.setSenha(req.getParameter("senha"));
        m.setCpf(req.getParameter("cpf"));
        m.setNome(req.getParameter("nome"));
        m.setCrm(req.getParameter("crm"));
        m.setEspecialidade(req.getParameter("especialidade"));
        m.setAdmin(false);
        return m;
    }

    // ─────────────── PACIENTE ───────────────
    private void handlePacienteGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");

        if ("new".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/pages/admin/paciente_form.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Paciente p = pacienteService.buscarPorId(id);
            req.setAttribute("paciente", p);
            req.getRequestDispatcher("/WEB-INF/pages/admin/paciente_form.jsp").forward(req, resp);
        } else {
            req.setAttribute("pacientes", pacienteService.listarTodos());
            req.getRequestDispatcher("/WEB-INF/pages/admin/paciente.jsp").forward(req, resp);
        }
    }

    private void handlePacientePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = req.getParameter("action");
        Paciente p = populaPaciente(req);

        if ("create".equals(action)) {
            pacienteService.criarPaciente(p);
        } else if ("update".equals(action)) {
            p.setId(Integer.parseInt(req.getParameter("id")));
            pacienteService.atualizarPaciente(p);
        } else if ("delete".equals(action)) {
            pacienteService.removerPaciente(Integer.parseInt(req.getParameter("id")));
        }

        resp.sendRedirect(req.getContextPath() + "/admin/paciente");
    }

    private Paciente populaPaciente(HttpServletRequest req) {
        Paciente p = new Paciente();
        p.setEmail(req.getParameter("email"));
        p.setSenha(req.getParameter("senha"));
        p.setCpf(req.getParameter("cpf"));
        p.setNome(req.getParameter("nome"));
        p.setHistorico(req.getParameter("historico"));
        p.setAdmin(false);
        return p;
    }
}
