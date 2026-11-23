package br.edu.avaliacao.sacm.interfaces;
import br.edu.avaliacao.sacm.model.Consulta;
import java.time.LocalDateTime;
public interface Agendavel {
 Consulta agendarConsulta(long medicoId,long pacienteId,LocalDateTime dataHora,String motivo);
 void cancelarConsulta(long consultaId);
}
