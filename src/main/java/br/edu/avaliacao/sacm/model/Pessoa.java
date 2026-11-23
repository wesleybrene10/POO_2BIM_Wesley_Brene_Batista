package br.edu.avaliacao.sacm.model;
import br.edu.avaliacao.sacm.enums.StatusPessoa;
import br.edu.avaliacao.sacm.exception.RegraNegocioException;
import java.time.*;
public abstract class Pessoa {
 private final Long id;
 private final String nome, cpf;
 private final LocalDate dataNascimento;
 private String email, telefone, cidade, estado;
 private final LocalDateTime dataCadastro;
 private StatusPessoa status;
 protected Pessoa(Long id, String nome, String cpf, LocalDate nascimento, String email, String telefone, String cidade, String estado, LocalDateTime cadastro, StatusPessoa status) {
  if (nome == null || nome.isBlank() || cpf == null || !cpf.matches("\\d{11}") || nascimento == null || nascimento.isAfter(LocalDate.now())) throw new RegraNegocioException("Nome, CPF com 11 digitos e data de nascimento valida sao obrigatorios.");
  this.id=id; this.nome=nome.trim(); this.cpf=cpf; this.dataNascimento=nascimento;
  atualizarContato(email,telefone); this.cidade=valida(cidade,"Cidade"); this.estado=valida(estado,"Estado").toUpperCase();
  if(this.estado.length()!=2) throw new RegraNegocioException("Estado deve ter sigla de duas letras.");
  this.dataCadastro=cadastro==null?LocalDateTime.now():cadastro; this.status=status==null?StatusPessoa.ATIVO:status;
 }
 private static String valida(String s, String campo) { if(s==null||s.isBlank()) throw new RegraNegocioException(campo+" e obrigatorio."); return s.trim(); }
 public void atualizarContato(String email,String telefone) { this.email=valida(email,"Email"); this.telefone=valida(telefone,"Telefone"); }
 public int getIdade(){return Period.between(dataNascimento,LocalDate.now()).getYears();}
 public void ativar(){status=StatusPessoa.ATIVO;} public void desativar(){status=StatusPessoa.INATIVO;}
 public boolean isAtivo(){return status==StatusPessoa.ATIVO;}
 public abstract String getDescricao();
 public Long getId(){return id;} public String getNome(){return nome;} public String getCpf(){return cpf;}
 public LocalDate getDataNascimento(){return dataNascimento;} public String getEmail(){return email;}
 public String getTelefone(){return telefone;} public String getCidade(){return cidade;}
 public String getEstado(){return estado;} public LocalDateTime getDataCadastro(){return dataCadastro;}
 public StatusPessoa getStatus(){return status;}
}
