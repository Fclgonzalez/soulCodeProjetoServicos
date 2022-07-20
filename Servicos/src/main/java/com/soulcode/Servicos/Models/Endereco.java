package com.soulcode.Servicos.Models;

import javax.persistence.*;

@Entity
public class Endereco {

    @Id
    private Integer idEndereco;

    @Column (nullable = false, length = 100)
    private String rua;

    @Column (nullable = false, length = 100)
    private String bairro;

    @Column (nullable = false, length = 50)
    private String cidade;

    @Column (nullable = false, length = 2)
    private String uf;


    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
