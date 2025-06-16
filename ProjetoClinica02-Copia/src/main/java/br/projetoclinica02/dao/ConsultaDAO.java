package br.projetoclinica02.dao;

import br.projetoclinica02.model.Consulta;
import br.projetoclinica02.model.Medico;
import br.projetoclinica02.model.Paciente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    public Consulta create(Consulta consulta) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO consulta " +
                "(status, observacao, tipo_consulta, data_hora, medico_id, paciente_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, consulta.getStatus());
            ps.setString(2, consulta.getPbservacao());
            ps.setString(3, consulta.getTipoConsulta());
            ps.setTimestamp(4, Timestamp.valueOf(consulta.getDataHorario()));
            ps.setInt(5, consulta.getMedico().getId());
            ps.setInt(6, consulta.getPaciente().getId());

            int aff = ps.executeUpdate();
            if (aff == 0) throw new SQLException("Falha ao inserir consulta.");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
        }
        return consulta;
    }

    public Consulta findById(int id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT status, observacao, tipo_consulta, data_hora, medico_id, paciente_id " +
                "FROM consulta WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Consulta c = new Consulta();
                    c.setId(id);
                    c.setStatus(rs.getString("status"));
                    c.setPbservacao(rs.getString("observacao"));
                    c.setTipoConsulta(rs.getString("tipo_consulta"));
                    c.setDataHorario(rs.getTimestamp("data_hora").toLocalDateTime());

                    int medId = rs.getInt("medico_id");
                    int pacId = rs.getInt("paciente_id");
                    Medico m = medicoDAO.findById(medId);
                    Paciente p = pacienteDAO.findById(pacId);

                    c.setMedico(m);
                    c.setPaciente(p);
                    return c;
                }
            }
        }
        return null;
    }

    public List<Consulta> findAll() throws ClassNotFoundException, SQLException {
        List<Consulta> list = new ArrayList<>();
        String sql = "SELECT id FROM consulta";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Consulta c = findById(rs.getInt("id"));
                if (c != null) {
                    list.add(c);
                }
            }
        }
        return list;
    }


    public boolean update(Consulta consulta) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE consulta SET status = ?, observacao = ?, tipo_consulta = ?, " +
                "data_hora = ?, medico_id = ?, paciente_id = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consulta.getStatus());
            ps.setString(2, consulta.getPbservacao());
            ps.setString(3, consulta.getTipoConsulta());
            ps.setTimestamp(4, Timestamp.valueOf(consulta.getDataHorario()));
            ps.setInt(5, consulta.getMedico().getId());
            ps.setInt(6, consulta.getPaciente().getId());
            ps.setInt(7, consulta.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM consulta WHERE id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public List<Consulta> findByMedicoId(int medicoId) throws ClassNotFoundException, SQLException {
        List<Consulta> list = new ArrayList<>();
        String sql = "SELECT id FROM consulta WHERE medico_id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = findById(rs.getInt("id"));
                    if (c != null) {
                        list.add(c);
                    }
                }
            }
        }
        return list;
    }

    public List<Consulta> findByPacienteId(int pacienteId) throws Exception {
        List<Consulta> list = new ArrayList<>();
        String sql = "SELECT id FROM consulta WHERE paciente_id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = findById(rs.getInt("id"));
                    if (c != null) {
                        list.add(c);
                    }
                }
            }
        }
        return list;
    }

    public boolean existeConflitoHorario(int medicoId, LocalDateTime dataHorario, Integer ignorarId) throws Exception {
        String sql = "SELECT COUNT(*) FROM consulta WHERE medico_id = ? AND data_hora = ?";
        if (ignorarId != null) {
            sql += " AND id <> ?";
        }

        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicoId);
            ps.setTimestamp(2, Timestamp.valueOf(dataHorario));
            if (ignorarId != null) {
                ps.setInt(3, ignorarId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existeConflitoHorarioPaciente(int pacienteId, LocalDateTime dataHorario, Integer ignorarId) throws Exception {
        String sql = "SELECT COUNT(*) FROM consulta WHERE paciente_id = ? AND data_hora = ?";
        if (ignorarId != null) {
            sql += " AND id <> ?";
        }

        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);
            ps.setTimestamp(2, Timestamp.valueOf(dataHorario));
            if (ignorarId != null) {
                ps.setInt(3, ignorarId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public List<Paciente> findPacientesAtendidosPorMedico(int medicoId) throws Exception {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT DISTINCT paciente_id FROM consulta WHERE medico_id = ?";
        try (Connection conn = ConexaoBD.conectarBancoPostgres();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int pacienteId = rs.getInt("paciente_id");
                    Paciente p = pacienteDAO.findById(pacienteId);
                    if (p != null) {
                        pacientes.add(p);
                    }
                }
            }
        }
        return pacientes;
    }


}
