package br.edu.avaliacao.sacm.repository;
import br.edu.avaliacao.sacm.enums.*;
import br.edu.avaliacao.sacm.model.Medico;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
@Repository public class MedicoRepository {
 private final JdbcTemplate jdbc;
 public MedicoRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @org.springframework.transaction.annotation.Transactional public long salvar(Medico m){long id=PessoaJdbc.inserir(jdbc,m);jdbc.update("INSERT INTO medico(id,crm,especialidade,disponibilidade,valor_consulta) VALUES (?,?,?,?,?)",id,m.getCrm(),m.getEspecialidade().name(),m.getDisponibilidade().name(),m.getValorConsulta());return id;}
 static final RowMapper<Medico> MAPPER=(r,n)->new Medico(r.getLong("id"),r.getString("nome"),r.getString("cpf"),r.getDate("data_nascimento").toLocalDate(),r.getString("email"),r.getString("telefone"),r.getString("cidade"),r.getString("estado"),r.getTimestamp("data_cadastro").toLocalDateTime(),StatusPessoa.valueOf(r.getString("status")),r.getString("crm"),Especialidade.valueOf(r.getString("especialidade")),DisponibilidadeMedico.valueOf(r.getString("disponibilidade")),r.getBigDecimal("valor_consulta"));
 private static final String SQL="SELECT p.*,m.crm,m.especialidade,m.disponibilidade,m.valor_consulta FROM pessoa p JOIN medico m ON m.id=p.id";
 public Optional<Medico> buscar(long id){return jdbc.query(SQL+" WHERE p.id=?",MAPPER,id).stream().findFirst();}
 public List<Medico> listar(){return jdbc.query(SQL+" ORDER BY p.id",MAPPER);}
 public void alterarDisponibilidade(long id,DisponibilidadeMedico valor){jdbc.update("UPDATE medico SET disponibilidade=? WHERE id=?",valor.name(),id);}
 public void alterarStatus(long id,StatusPessoa valor){jdbc.update("UPDATE pessoa SET status=? WHERE id=? AND EXISTS(SELECT 1 FROM medico WHERE medico.id=pessoa.id)",valor.name(),id);}
 public void bloquearParaAgendamento(long id){jdbc.queryForList("SELECT id FROM pessoa WHERE id=? FOR UPDATE",id);}
}
