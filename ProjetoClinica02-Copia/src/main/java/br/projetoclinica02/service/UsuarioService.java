package br.projetoclinica02.service;

import br.projetoclinica02.dao.UsuarioDAO;
import br.projetoclinica02.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    public Usuario criarUsuario(String email,
                                String senha,
                                String nome,
                                String cpf,
                                boolean admin) throws Exception {

        Usuario u = new Usuario();
        u.setEmail(email);
        u.setSenha(senha);
        u.setNome(nome);
        u.setCpf(cpf);
        u.setAdmin(admin);
        return dao.create(u);
    }
}
