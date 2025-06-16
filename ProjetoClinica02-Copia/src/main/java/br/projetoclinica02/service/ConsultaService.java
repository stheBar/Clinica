package br.projetoclinica02.service;

import br.projetoclinica02.dao.ConsultaDAO;
import br.projetoclinica02.model.Consulta;
import br.projetoclinica02.model.Paciente;

import java.util.List;
import java.time.LocalDateTime;

public class ConsultaService {
    private final ConsultaDAO dao = new ConsultaDAO();

    public List<Consulta> listarTodas() throws Exception {
        return dao.findAll();
    }


    public Consulta buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }


    public Consulta criarConsulta(Consulta consulta) throws Exception {
        return dao.create(consulta);
    }


    public boolean atualizarConsulta(Consulta consulta) throws Exception {
        return dao.update(consulta);
    }

    public boolean removerConsulta(int id) throws Exception {
        return dao.delete(id);
    }


    public List<Consulta> listarPorMedico(int medicoId) throws Exception {
        return dao.findByMedicoId(medicoId);
    }

    public List<Consulta> listarPorPaciente(int pacienteId) throws Exception {
        return dao.findByPacienteId(pacienteId);
    }

    public boolean existeConflitoHorario(int medicoId, LocalDateTime dataHorario, Integer ignorarId) throws Exception {
        return dao.existeConflitoHorario(medicoId, dataHorario, ignorarId);
    }

    public boolean existeConflitoHorarioPaciente(int pacienteId, LocalDateTime dataHorario, Integer ignorarId) throws Exception {
        return dao.existeConflitoHorarioPaciente(pacienteId, dataHorario, ignorarId);
    }

    // ConsultaService.java
    public List<Paciente> listarPacientesAtendidosPorMedico(int medicoId) throws Exception {
        return dao.findPacientesAtendidosPorMedico(medicoId);
    }


}
