package br.projetoclinica02.model;

public class Medico extends Usuario{

    private String especialidade;
    private String crm;

    public Medico(){

    }

    public Medico(int id, String email, String senha, String nome, String cpf, boolean admin, String especialidade, String crm) {
        super(id, email, senha, nome, cpf, admin);
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
}
