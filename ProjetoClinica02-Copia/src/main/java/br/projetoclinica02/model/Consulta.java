package br.projetoclinica02.model;

import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private String status;
    private String pbservacao;
    private String tipoConsulta;
    private LocalDateTime dataHorario;
    private Medico medico;
    private Paciente paciente;

    public Consulta() {

    }

    public Consulta(int id, String status, String pbservacao, String tipoConsulta, LocalDateTime dataHorario, Medico medico, Paciente paciente) {
        this.id = id;
        this.status = status;
        this.pbservacao = pbservacao;
        this.tipoConsulta = tipoConsulta;
        this.dataHorario = dataHorario;
        this.medico = medico;
        this.paciente = paciente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPbservacao() {
        return pbservacao;
    }

    public void setPbservacao(String pbservacao) {
        this.pbservacao = pbservacao;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public LocalDateTime getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(LocalDateTime dataHorario) {
        this.dataHorario = dataHorario;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
