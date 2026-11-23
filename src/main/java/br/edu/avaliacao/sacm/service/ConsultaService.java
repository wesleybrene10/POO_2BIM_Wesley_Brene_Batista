package br.edu.avaliacao.sacm.service;
import br.edu.avaliacao.sacm.enums.StatusConsulta;
import br.edu.avaliacao.sacm.exception.*;
import br.edu.avaliacao.sacm.interfaces.Agendavel;
import br.edu.avaliacao.sacm.model.*;
import br.edu.avaliacao.sacm.repository.*;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class ConsultaService implements Agendavel {
 // Constantes explicadas no relatorio: parametros imutaveis compartilhados.
 public static final int LIMITE_MOTIVO=500;
 public static final int ANTECEDENCIA_MINIMA_MINUTOS=1;
 private final MedicoRepository medicoRepository;
 private final PacienteRepository pacienteRepository;
 private final ConsultaRepository consultaRepository;
 public ConsultaService(MedicoRepository medicoRepository,PacienteRepository pacienteRepository,ConsultaRepository consultaRepository){this.medicoRepository=medicoRepository;this.pacienteRepository=pacienteRepository;this.consultaRepository=consultaRepository;}
 @Override @Transactional public Consulta agendarConsulta(long medicoId,long pacienteId,LocalDateTime dataHora,String motivo){
  if(dataHora==null||!dataHora.isAfter(LocalDateTime.now().plusMinutes(ANTECEDENCIA_MINIMA_MINUTOS)))throw new RegraNegocioException("Informe data/hora futura (pelo menos 1 minuto de antecedencia).");
  if(motivo==null||motivo.isBlank()||motivo.length()>LIMITE_MOTIVO)throw new RegraNegocioException("Motivo obrigatorio com ate 500 caracteres.");
  // Bloqueia a linha da pessoa-medico dentro da transacao; impede duas reservas concorrentes no mesmo medico.
  medicoRepository.bloquearParaAgendamento(medicoId);
  pacienteRepository.bloquearParaAgendamento(pacienteId);
  Medico medico=medicoRepository.buscar(medicoId).orElseThrow(()->new RegraNegocioException("Medico nao encontrado."));
  Paciente paciente=pacienteRepository.buscar(pacienteId).orElseThrow(()->new RegraNegocioException("Paciente nao encontrado."));
  if(!medico.podeAtender())throw new RegraNegocioException("Medico inativo ou indisponivel.");
  if(!paciente.podeAgendar())throw new RegraNegocioException("Paciente inativo.");
  if(verificarConflitoHorario(medicoId,pacienteId,dataHora))throw new ConflitoHorarioException("Conflito: medico ou paciente ja tem consulta nesse horario.");
  Consulta c=new Consulta(null,medico,paciente,dataHora,motivo,StatusConsulta.AGENDADA,null,null);
  long id=consultaRepository.salvar(c);
  return consultaRepository.buscar(id).orElseThrow(()->new IllegalStateException("Consulta gravada nao encontrada."));
 }
 @Override @Transactional public void cancelarConsulta(long consultaId){Consulta c=obter(consultaId);c.cancelar();consultaRepository.atualizarStatus(consultaId,c.getStatus());}
 @Transactional public void confirmarConsulta(long consultaId){Consulta c=obter(consultaId);c.confirmar();consultaRepository.atualizarStatus(consultaId,c.getStatus());}
 @Transactional public void finalizarConsulta(long consultaId){Consulta c=obter(consultaId);c.finalizar();consultaRepository.atualizarStatus(consultaId,c.getStatus());}
 public List<Consulta> listarConsultasPorMedico(long medicoId){return consultaRepository.listarPorMedico(medicoId);}
 public List<Consulta> listarConsultasPorPaciente(long pacienteId){return consultaRepository.listarPorPaciente(pacienteId);}
 public List<Consulta> listarTodas(){return consultaRepository.listarTodas();}
 public boolean verificarConflitoHorario(long medicoId,long pacienteId,LocalDateTime dataHora){return consultaRepository.existeConflito(medicoId,pacienteId,dataHora);}
 private Consulta obter(long id){return consultaRepository.buscar(id).orElseThrow(()->new RegraNegocioException("Consulta nao encontrada."));}
}
