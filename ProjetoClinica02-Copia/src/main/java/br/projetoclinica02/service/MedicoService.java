package br.projetoclinica02.service;

import br.projetoclinica02.dao.MedicoDAO;
import br.projetoclinica02.model.Medico;

import java.util.List;

public class MedicoService {
    private final MedicoDAO dao = new MedicoDAO();


    public List<Medico> listarTodos() throws Exception {
        return dao.findAll();
    }
    public Medico buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }



    public Medico criarMedico(Medico medico) throws Exception {
        return dao.create(medico);
    }

    public boolean atualizarMedico(Medico medico) throws Exception {
        return dao.update(medico);
    }

    public boolean removerMedico(int id) throws Exception {
        return dao.delete(id);
    }
}
