package br.edu.avaliacao.sacm.service;
import br.edu.avaliacao.sacm.enums.*;
import br.edu.avaliacao.sacm.exception.*;
import br.edu.avaliacao.sacm.model.*;
import br.edu.avaliacao.sacm.repository.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;
class ConsultaServiceTest {
 MedicoRepository medicos=mock(MedicoRepository.class);
 PacienteRepository pacientes=mock(PacienteRepository.class);
 ConsultaRepository consultas=mock(ConsultaRepository.class);
 ConsultaService service=new ConsultaService(medicos,pacientes,consultas);
 LocalDateTime horario=LocalDateTime.now().plusDays(2);
 Medico medico(){return new Medico(1L,"Ana","12345678901",LocalDate.of(1980,1,1),"ana@email.com","9999","Londrina","PR",null,StatusPessoa.ATIVO,"CRM1",Especialidade.CARDIOLOGIA,DisponibilidadeMedico.DISPONIVEL,new BigDecimal("150"));}
 Paciente paciente(){return new Paciente(2L,"Beto","98765432100",LocalDate.of(1990,1,1),"beto@email.com","8888","Londrina","PR",null,StatusPessoa.ATIVO,"PR1","PARTICULAR","Contato","7777");}
 void preparar(Medico m,Paciente p){when(medicos.buscar(1)).thenReturn(Optional.of(m));when(pacientes.buscar(2)).thenReturn(Optional.of(p));}
 @Test void agendamentoValido(){preparar(medico(),paciente());when(consultas.salvar(any())).thenReturn(5L);when(consultas.buscar(5)).thenReturn(Optional.of(new Consulta(5L,medico(),paciente(),horario,"Rotina",StatusConsulta.AGENDADA,null,null)));assertEquals(StatusConsulta.AGENDADA,service.agendarConsulta(1,2,horario,"Rotina").getStatus());verify(consultas).salvar(any());}
 @Test void conflitoMedicoOuPaciente(){preparar(medico(),paciente());when(consultas.existeConflito(1,2,horario)).thenReturn(true);assertThrows(ConflitoHorarioException.class,()->service.agendarConsulta(1,2,horario,"Rotina"));verify(consultas,never()).salvar(any());}
 @Test void pacienteInativo(){Paciente p=paciente();p.desativar();preparar(medico(),p);assertThrows(RegraNegocioException.class,()->service.agendarConsulta(1,2,horario,"Rotina"));}
 @Test void medicoIndisponivel(){Medico m=medico();m.alterarDisponibilidade(DisponibilidadeMedico.INDISPONIVEL);preparar(m,paciente());assertThrows(RegraNegocioException.class,()->service.agendarConsulta(1,2,horario,"Rotina"));}
 @Test void dataPassada(){assertThrows(RegraNegocioException.class,()->service.agendarConsulta(1,2,LocalDateTime.now().minusDays(1),"Rotina"));}
 @Test void cancelarELiberarHorario(){Consulta c=new Consulta(8L,medico(),paciente(),horario,"Rotina",StatusConsulta.AGENDADA,null,null);when(consultas.buscar(8)).thenReturn(Optional.of(c));service.cancelarConsulta(8);assertEquals(StatusConsulta.CANCELADA,c.getStatus());verify(consultas).atualizarStatus(8,StatusConsulta.CANCELADA);preparar(medico(),paciente());when(consultas.existeConflito(1,2,horario)).thenReturn(false);when(consultas.salvar(any())).thenReturn(9L);when(consultas.buscar(9)).thenReturn(Optional.of(new Consulta(9L,medico(),paciente(),horario,"Retorno",StatusConsulta.AGENDADA,null,null)));assertEquals(9,service.agendarConsulta(1,2,horario,"Retorno").getId());}
 @Test void transicoesDeStatus(){Consulta c=new Consulta(3L,medico(),paciente(),horario,"Retorno",StatusConsulta.AGENDADA,null,null);assertThrows(RegraNegocioException.class,c::finalizar);c.confirmar();c.finalizar();assertThrows(RegraNegocioException.class,c::cancelar);}
 @Test void cpfInvalido(){assertThrows(RegraNegocioException.class,()->new Medico(null,"Ana","123",LocalDate.of(1980,1,1),"a@b.com","123","Londrina","PR",null,StatusPessoa.ATIVO,"CRM",Especialidade.CARDIOLOGIA,DisponibilidadeMedico.DISPONIVEL,new BigDecimal("100")));}
}
