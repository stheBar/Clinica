package br.projetoclinica02.dao;

import br.projetoclinica02.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Insere um novo usuário no banco e retorna o usuário com o ID gerado.
     */
    public Usuario create(Usuario usuario) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO usuario (email, senha, nome, cpf, admin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getNome());
            ps.setString(4, usuario.getCpf());
            ps.setBoolean(5, usuario.isAdmin());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
        return usuario;
    }

    /**
     * Busca um usuário pelo seu ID.
     */
    public Usuario findById(int id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT id, email, senha, nome, cpf, admin FROM usuario WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapRowToUsuario(rs);
                }
            }
        }
        return usuario;
    }

    /**
     * Retorna todos os usuários cadastrados.
     */
    public List<Usuario> findAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT id, email, senha, nome, cpf, admin FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapRowToUsuario(rs));
            }
        }
        return lista;
    }

    /**
     * Atualiza os dados de um usuário existente.
     */
    public boolean update(Usuario usuario) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE usuario SET email = ?, senha = ?, nome = ?, cpf = ?, admin = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getNome());
            ps.setString(4, usuario.getCpf());
            ps.setBoolean(5, usuario.isAdmin());
            ps.setInt(6, usuario.getId());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Remove um usuário pelo seu ID.
     */
    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Converte a linha do ResultSet em um objeto Usuario.
     */
    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setSenha(rs.getString("senha"));
        u.setNome(rs.getString("nome"));
        u.setCpf(rs.getString("cpf"));
        u.setAdmin(rs.getBoolean("admin"));
        return u;
    }


    public Usuario findByEmailAndSenha(String email, String senha)
            throws ClassNotFoundException, SQLException {
        String sql = "SELECT id, email, senha, nome, cpf, admin " +
                "FROM usuario " +
                "WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUsuario(rs);
                }
            }
        }
        return null;
    }

}
