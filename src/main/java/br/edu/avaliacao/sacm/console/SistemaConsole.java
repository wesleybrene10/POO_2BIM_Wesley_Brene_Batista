package br.edu.avaliacao.sacm.console;
import br.edu.avaliacao.sacm.enums.*;
import br.edu.avaliacao.sacm.exception.RegraNegocioException;
import br.edu.avaliacao.sacm.model.*;
import br.edu.avaliacao.sacm.repository.*;
import br.edu.avaliacao.sacm.service.ConsultaService;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component public class SistemaConsole {
 private final ConsultaService consultaService;
 private final MedicoRepository medicos;
 private final PacienteRepository pacientes;
 private final Scanner scanner=new Scanner(System.in);
 private static final DateTimeFormatter FORMATO=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
 public SistemaConsole(ConsultaService consultas,MedicoRepository medicos,PacienteRepository pacientes){this.consultaService=consultas;this.medicos=medicos;this.pacientes=pacientes;}
 public void exibirMenu(){while(true){System.out.println("\n=== SACM - CONSULTAS ===");System.out.println("1 Cadastrar medico | 2 Cadastrar paciente | 3 Listar pessoas");System.out.println("4 Agendar | 5 Listar consultas | 6 Confirmar | 7 Cancelar | 8 Finalizar");System.out.println("9 Alterar disponibilidade/status | 0 Sair");String op=ler("Opcao");try{switch(op){case "1"->cadastrarMedico();case "2"->cadastrarPaciente();case "3"->listarPessoas();case "4"->agendarConsulta();case "5"->listarConsultas();case "6"->alterarStatusConsulta("confirmar");case "7"->alterarStatusConsulta("cancelar");case "8"->alterarStatusConsulta("finalizar");case "9"->alterarCadastro();case "0"->{encerrar();return;}default->System.out.println("Opcao invalida.");}}catch(RegraNegocioException|IllegalArgumentException e){System.out.println("ERRO DE REGRA: "+e.getMessage());}catch(DataAccessException e){System.err.println("FALHA SQL: "+e.getMostSpecificCause().getMessage());}catch(Exception e){System.err.println("ERRO: "+e.getMessage());}}}
 private String ler(String campo){System.out.print(campo+": ");return scanner.nextLine().trim();}
 private long numero(String campo){return Long.parseLong(ler(campo));}
 private String[] dadosPessoa(){return new String[]{ler("Nome"),ler("CPF (11 digitos, sem pontuacao)"),ler("Nascimento (AAAA-MM-DD)"),ler("Email"),ler("Telefone"),ler("Cidade"),ler("Estado (UF)")};}
 @Transactional public void cadastrarMedico(){String[] x=dadosPessoa();String crm=ler("CRM");System.out.println("Especialidades: "+java.util.Arrays.toString(Especialidade.values()));Especialidade especialidade=Especialidade.valueOf(ler("Especialidade").toUpperCase());BigDecimal valor=new BigDecimal(ler("Valor da consulta (ex: 150.00)"));Medico m=new Medico(null,x[0],x[1],LocalDate.parse(x[2]),x[3],x[4],x[5],x[6],null,StatusPessoa.ATIVO,crm,especialidade,DisponibilidadeMedico.DISPONIVEL,valor);System.out.println("Medico cadastrado com ID "+medicos.salvar(m));}
 @Transactional public void cadastrarPaciente(){String[] x=dadosPessoa();Paciente p=new Paciente(null,x[0],x[1],LocalDate.parse(x[2]),x[3],x[4],x[5],x[6],null,StatusPessoa.ATIVO,ler("Numero do prontuario"),ler("Convenio (ou PARTICULAR)"),ler("Nome contato emergencia"),ler("Telefone emergencia"));System.out.println("Paciente cadastrado com ID "+pacientes.salvar(p));}
 public void listarPessoas(){System.out.println("MEDICOS:");medicos.listar().forEach(m->System.out.println(m.getId()+" - "+m.getDescricao()));System.out.println("PACIENTES:");pacientes.listar().forEach(p->System.out.println(p.getId()+" - "+p.getDescricao()));}
 public void agendarConsulta(){long m=numero("ID medico");long p=numero("ID paciente");LocalDateTime dt=LocalDateTime.parse(ler("Data e hora (dd/MM/aaaa HH:mm)"),FORMATO);Consulta c=consultaService.agendarConsulta(m,p,dt,ler("Motivo"));System.out.println("AGENDAMENTO OK: "+c.getResumo());}
 public void listarConsultas(){System.out.println("1 Todas | 2 Por medico | 3 Por paciente");String modo=ler("Filtro");var dados=switch(modo){case "2"->consultaService.listarConsultasPorMedico(numero("ID medico"));case "3"->consultaService.listarConsultasPorPaciente(numero("ID paciente"));default->consultaService.listarTodas();};if(dados.isEmpty())System.out.println("Nenhuma consulta.");else dados.forEach(c->System.out.println(c.getResumo()));}
 public void alterarStatusConsulta(String acao){long id=numero("ID da consulta");switch(acao){case "confirmar"->consultaService.confirmarConsulta(id);case "cancelar"->consultaService.cancelarConsulta(id);case "finalizar"->consultaService.finalizarConsulta(id);default->throw new RegraNegocioException("Acao invalida.");}System.out.println("Consulta atualizada no banco.");}
 public void alterarCadastro(){System.out.println("1 Disponibilidade medico | 2 Status medico | 3 Status paciente");String modo=ler("Opcao");long id=numero("ID");switch(modo){case "1"->medicos.alterarDisponibilidade(id,DisponibilidadeMedico.valueOf(ler("DISPONIVEL ou INDISPONIVEL").toUpperCase()));case "2"->medicos.alterarStatus(id,StatusPessoa.valueOf(ler("ATIVO ou INATIVO").toUpperCase()));case "3"->pacientes.alterarStatus(id,StatusPessoa.valueOf(ler("ATIVO ou INATIVO").toUpperCase()));default->throw new RegraNegocioException("Opcao invalida.");}System.out.println("Cadastro atualizado.");}
 public void encerrar(){System.out.println("Sistema encerrado. Dados mantidos no MySQL.");}
}
