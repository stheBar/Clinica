package br.projetoclinica02.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private int id;
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private boolean admin;

    private List<Consulta> consultas = new ArrayList<>();

    public Usuario(){

    }

    public Usuario(int id, String email, String senha, String nome, String cpf, boolean admin) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.cpf = cpf;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
