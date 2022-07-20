package com.soulcode.Servicos.Models;

public enum StatusPagamento {

    LANCADO("Lancado"),
    QUITADO("Quitado");

    private String conteudo;

    StatusPagamento(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

}
