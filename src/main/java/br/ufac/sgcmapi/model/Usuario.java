package br.ufac.sgcmapi.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Long id;
    private String nomeCompleto;
    private String nomeUsuario;
    private String senha;
    private boolean ativo = true;
    private EPapel papel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public EPapel getPapel() {
        return papel;
    }
    
    public void setPapel(EPapel papel) {
        this.papel = papel;
    }

}
