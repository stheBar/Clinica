package br.projetoclinica02.dao;

import br.projetoclinica02.model.Medico;
import br.projetoclinica02.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Medico create(Medico medico) throws ClassNotFoundException, SQLException {
        usuarioDAO.create(medico);  // preenche medico.id

        String sql = "INSERT INTO medico (id, especialidade, crm) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medico.getId());
            ps.setString(2, medico.getEspecialidade());
            ps.setString(3, medico.getCrm());
            ps.executeUpdate();
        }
        return medico;
    }

    //busca medico por id
    public Medico findById(int id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT especialidade, crm FROM medico WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = usuarioDAO.findById(id);
                    if (u != null) {
                        Medico m = new Medico();
                        m.setId(u.getId());
                        m.setEmail(u.getEmail());
                        m.setSenha(u.getSenha());
                        m.setNome(u.getNome());
                        m.setCpf(u.getCpf());
                        m.setAdmin(u.isAdmin());
                        m.setEspecialidade(rs.getString("especialidade"));
                        m.setCrm(rs.getString("crm"));
                        return m;
                    }
                }
            }
        }
        return null;
    }

    //lista medicos

    public List<Medico> findAll() throws ClassNotFoundException, SQLException {
        List<Medico> list = new ArrayList<>();
        String sql = "SELECT id FROM medico";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medico m = findById(rs.getInt("id"));
                if (m != null) {
                    list.add(m);
                }
            }
        }
        return list;
    }


    public boolean update(Medico medico) throws ClassNotFoundException, SQLException {
        boolean uOk = usuarioDAO.update(medico);

        String sql = "UPDATE medico SET especialidade = ?, crm = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medico.getEspecialidade());
            ps.setString(2, medico.getCrm());
            ps.setInt(3, medico.getId());
            int affected = ps.executeUpdate();
            return uOk && affected > 0;
        }
    }


    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM medico WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        return usuarioDAO.delete(id);
    }
}
