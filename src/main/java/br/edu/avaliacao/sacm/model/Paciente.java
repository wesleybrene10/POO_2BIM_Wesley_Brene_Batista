package br.edu.avaliacao.sacm.model;
import br.edu.avaliacao.sacm.enums.StatusPessoa;
import br.edu.avaliacao.sacm.exception.RegraNegocioException;
import java.time.*;
public class Paciente extends Pessoa {
 private final String numeroProntuario;
 private String convenio, contatoEmergencia, telefoneEmergencia;
 public Paciente(Long id,String nome,String cpf,LocalDate nascimento,String email,String telefone,String cidade,String estado,LocalDateTime cadastro,StatusPessoa status,String prontuario,String convenio,String contato,String telefoneEmergencia){
  super(id,nome,cpf,nascimento,email,telefone,cidade,estado,cadastro,status);
  if(prontuario==null||prontuario.isBlank())throw new RegraNegocioException("Numero do prontuario obrigatorio.");
  numeroProntuario=prontuario.trim(); atualizarConvenio(convenio); atualizarContatoEmergencia(contato,telefoneEmergencia);
 }
 public void atualizarConvenio(String novo){if(novo==null||novo.isBlank())throw new RegraNegocioException("Convenio obrigatorio (use PARTICULAR)."); convenio=novo.trim();}
 public void atualizarContatoEmergencia(String nome,String telefone){if(nome==null||nome.isBlank()||telefone==null||telefone.isBlank())throw new RegraNegocioException("Contato de emergencia obrigatorio."); contatoEmergencia=nome.trim();telefoneEmergencia=telefone.trim();}
 public boolean podeAgendar(){return isAtivo();}
 @Override public String getDescricao(){return "Paciente "+getNome()+" | prontuario "+numeroProntuario+" | "+getStatus();}
 public String getNumeroProntuario(){return numeroProntuario;} public String getConvenio(){return convenio;}
 public String getContatoEmergencia(){return contatoEmergencia;} public String getTelefoneEmergencia(){return telefoneEmergencia;}
}
