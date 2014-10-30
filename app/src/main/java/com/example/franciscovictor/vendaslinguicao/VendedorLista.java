package com.example.franciscovictor.vendaslinguicao;

/**
 * Encapsulates information about a news entry
 */
public final class VendedorLista {

    private final String nome;
    private final String frase;
    private final String distancia;

    public VendedorLista(final String nome, final String frase, final String distancia)
    {
        this.nome = nome;
        this.frase = frase;
        this.distancia = distancia;
    }

    public String getNome() {
        return nome;
    }

    public String getFrase() {
        return frase;
    }

    public String getDistancia() {
        return distancia;
    }

}