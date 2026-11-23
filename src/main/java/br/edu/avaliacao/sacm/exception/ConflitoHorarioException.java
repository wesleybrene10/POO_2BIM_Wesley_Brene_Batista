package br.edu.avaliacao.sacm.exception;
public class ConflitoHorarioException extends RegraNegocioException {
 public ConflitoHorarioException(String mensagem) { super(mensagem); }
 public ConflitoHorarioException(String mensagem, Throwable causa) { super(mensagem, causa); }
}
