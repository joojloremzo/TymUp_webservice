package br.furb.webservice.exception;

public class BancoDeDadosException extends RuntimeException {
    public BancoDeDadosException(String mensagem) {
        super(mensagem);
    }
}