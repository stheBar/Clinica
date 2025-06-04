package br.projetoclinica02.dao;

import br.projetoclinica02.model.Paciente;
import br.projetoclinica02.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Insere um paciente (e seu usuário) e devolve o objeto com ID preenchido */
    public Paciente create(Paciente paciente) throws ClassNotFoundException, SQLException {
        // 1) cria o registro em usuario (preenche paciente.id)
        usuarioDAO.create(paciente);

        // 2) insere em paciente(id, historico)
        String sql = "INSERT INTO paciente (id, historico) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paciente.getId());
            ps.setString(2, paciente.getHistorico());
            ps.executeUpdate();
        }
        return paciente;
    }

    /** Busca um paciente por ID (já carrega o usuário base) */
    public Paciente findById(int id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT historico FROM paciente WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // obtém dados do usuário base
                    Usuario u = usuarioDAO.findById(id);
                    if (u != null) {
                        Paciente p = new Paciente();
                        p.setId(u.getId());
                        p.setEmail(u.getEmail());
                        p.setSenha(u.getSenha());
                        p.setNome(u.getNome());
                        p.setCpf(u.getCpf());
                        p.setAdmin(u.isAdmin());
                        p.setHistorico(rs.getString("historico"));
                        return p;
                    }
                }
            }
        }
        return null;
    }

    /** Lista todos os pacientes */
    public List<Paciente> findAll() throws ClassNotFoundException, SQLException {
        List<Paciente> list = new ArrayList<>();
        String sql = "SELECT id FROM paciente";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente p = findById(rs.getInt("id"));
                if (p != null) {
                    list.add(p);
                }
            }
        }
        return list;
    }

    /** Atualiza paciente e usuário base */
    public boolean update(Paciente paciente) throws ClassNotFoundException, SQLException {
        // 1) atualiza campos em usuario
        boolean uOk = usuarioDAO.update(paciente);

        // 2) atualiza historico em paciente
        String sql = "UPDATE paciente SET historico = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getHistorico());
            ps.setInt(2, paciente.getId());
            int affected = ps.executeUpdate();
            return uOk && affected > 0;
        }
    }

    /** Remove paciente e usuário base */
    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        // 1) remove de paciente
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        // 2) remove de usuario
        return usuarioDAO.delete(id);
    }


}
