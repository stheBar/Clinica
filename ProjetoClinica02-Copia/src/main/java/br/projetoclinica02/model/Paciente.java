package br.projetoclinica02.model;

public class Paciente extends Usuario{

    private String historico;

    public Paciente(){

    }

    public Paciente(int id, String email, String senha, String nome, String cpf, boolean admin, String historico) {
        super(id, email, senha, nome, cpf, admin);
        this.historico = historico;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
