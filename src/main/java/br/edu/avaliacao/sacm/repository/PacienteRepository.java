package br.edu.avaliacao.sacm.repository;
import br.edu.avaliacao.sacm.enums.StatusPessoa;
import br.edu.avaliacao.sacm.model.Paciente;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
@Repository public class PacienteRepository {
 private final JdbcTemplate jdbc;
 public PacienteRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @org.springframework.transaction.annotation.Transactional public long salvar(Paciente p){long id=PessoaJdbc.inserir(jdbc,p);jdbc.update("INSERT INTO paciente(id,numero_prontuario,convenio,contato_emergencia,telefone_emergencia) VALUES (?,?,?,?,?)",id,p.getNumeroProntuario(),p.getConvenio(),p.getContatoEmergencia(),p.getTelefoneEmergencia());return id;}
 static final RowMapper<Paciente> MAPPER=(r,n)->new Paciente(r.getLong("id"),r.getString("nome"),r.getString("cpf"),r.getDate("data_nascimento").toLocalDate(),r.getString("email"),r.getString("telefone"),r.getString("cidade"),r.getString("estado"),r.getTimestamp("data_cadastro").toLocalDateTime(),StatusPessoa.valueOf(r.getString("status")),r.getString("numero_prontuario"),r.getString("convenio"),r.getString("contato_emergencia"),r.getString("telefone_emergencia"));
 private static final String SQL="SELECT p.*,a.numero_prontuario,a.convenio,a.contato_emergencia,a.telefone_emergencia FROM pessoa p JOIN paciente a ON a.id=p.id";
 public Optional<Paciente> buscar(long id){return jdbc.query(SQL+" WHERE p.id=?",MAPPER,id).stream().findFirst();}
 public List<Paciente> listar(){return jdbc.query(SQL+" ORDER BY p.id",MAPPER);}
 public void bloquearParaAgendamento(long id){jdbc.queryForList("SELECT id FROM pessoa WHERE id=? FOR UPDATE",id);}
 public void alterarStatus(long id,StatusPessoa valor){jdbc.update("UPDATE pessoa SET status=? WHERE id=? AND EXISTS(SELECT 1 FROM paciente WHERE paciente.id=pessoa.id)",valor.name(),id);}
}
