package br.projetoclinica02.service;

import br.projetoclinica02.dao.PacienteDAO;
import br.projetoclinica02.model.Paciente;
import java.util.List;

public class PacienteService {
    private final PacienteDAO dao = new PacienteDAO();

    public List<Paciente> listarTodos() throws Exception {
        return dao.findAll();
    }

    public Paciente buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public Paciente criarPaciente(Paciente paciente) throws Exception {
        return dao.create(paciente);
    }

    public boolean atualizarPaciente(Paciente paciente) throws Exception {
        return dao.update(paciente);
    }

    public boolean removerPaciente(int id) throws Exception {
        return dao.delete(id);
    }
}
