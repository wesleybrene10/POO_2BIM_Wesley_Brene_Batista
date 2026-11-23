package br.edu.avaliacao.sacm.model;
import br.edu.avaliacao.sacm.enums.*;
import br.edu.avaliacao.sacm.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.time.*;
public class Medico extends Pessoa {
 private final String crm;
 private final Especialidade especialidade;
 private DisponibilidadeMedico disponibilidade;
 private final BigDecimal valorConsulta;
 public Medico(Long id,String nome,String cpf,LocalDate nascimento,String email,String telefone,String cidade,String estado,LocalDateTime cadastro,StatusPessoa status,String crm,Especialidade especialidade,DisponibilidadeMedico disponibilidade,BigDecimal valorConsulta){
  super(id,nome,cpf,nascimento,email,telefone,cidade,estado,cadastro,status);
  if(crm==null||crm.isBlank()||especialidade==null||disponibilidade==null||valorConsulta==null||valorConsulta.signum()<0) throw new RegraNegocioException("CRM, especialidade, disponibilidade e valor valido sao obrigatorios.");
  this.crm=crm.trim(); this.especialidade=especialidade; this.disponibilidade=disponibilidade;this.valorConsulta=valorConsulta;
 }
 public void alterarDisponibilidade(DisponibilidadeMedico nova){if(nova==null)throw new RegraNegocioException("Disponibilidade invalida."); disponibilidade=nova;}
 public boolean isDisponivel(){return disponibilidade==DisponibilidadeMedico.DISPONIVEL;}
 public boolean podeAtender(){return isAtivo()&&isDisponivel();}
 @Override public String getDescricao(){return "Medico "+getNome()+" | CRM "+crm+" | "+especialidade+" | "+disponibilidade;}
 public String getCrm(){return crm;} public Especialidade getEspecialidade(){return especialidade;}
 public DisponibilidadeMedico getDisponibilidade(){return disponibilidade;} public BigDecimal getValorConsulta(){return valorConsulta;}
}
