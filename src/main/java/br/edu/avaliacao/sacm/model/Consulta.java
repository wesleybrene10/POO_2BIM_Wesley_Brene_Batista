package br.edu.avaliacao.sacm.model;
import br.edu.avaliacao.sacm.enums.StatusConsulta;
import br.edu.avaliacao.sacm.exception.RegraNegocioException;
import java.time.*;
public class Consulta {
 private final Long id;
 private final Medico medico;
 private final Paciente paciente;
 private final LocalDateTime dataHora;
 private final String motivo;
 private StatusConsulta status;
 private final LocalDateTime dataAgendamento;
 private String observacao;
 public Consulta(Long id,Medico medico,Paciente paciente,LocalDateTime dataHora,String motivo,StatusConsulta status,LocalDateTime dataAgendamento,String observacao){
  if(medico==null||paciente==null||dataHora==null||motivo==null||motivo.isBlank())throw new RegraNegocioException("Consulta precisa de medico, paciente, data e motivo.");
  this.id=id;this.medico=medico;this.paciente=paciente;this.dataHora=dataHora;this.motivo=motivo.trim();this.status=status==null?StatusConsulta.AGENDADA:status;
  this.dataAgendamento=dataAgendamento==null?LocalDateTime.now():dataAgendamento;this.observacao=observacao;
 }
 public void confirmar(){if(status!=StatusConsulta.AGENDADA)throw new RegraNegocioException("Somente consulta AGENDADA pode ser confirmada.");status=StatusConsulta.CONFIRMADA;}
 public void cancelar(){if(!isAtiva())throw new RegraNegocioException("Consulta finalizada/cancelada nao pode ser cancelada.");status=StatusConsulta.CANCELADA;}
 public void finalizar(){if(status!=StatusConsulta.CONFIRMADA)throw new RegraNegocioException("Somente consulta CONFIRMADA pode ser finalizada.");status=StatusConsulta.FINALIZADA;}
 public boolean isAtiva(){return status==StatusConsulta.AGENDADA||status==StatusConsulta.CONFIRMADA;}
 public String getResumo(){return "#"+id+" | "+dataHora+" | "+medico.getNome()+" / "+paciente.getNome()+" | "+status+" | "+motivo;}
 public Long getId(){return id;} public Medico getMedico(){return medico;}public Paciente getPaciente(){return paciente;}
 public LocalDateTime getDataHora(){return dataHora;}public String getMotivo(){return motivo;}public StatusConsulta getStatus(){return status;}
 public LocalDateTime getDataAgendamento(){return dataAgendamento;}public String getObservacao(){return observacao;}
}
