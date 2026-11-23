package br.edu.avaliacao.sacm.repository;
import br.edu.avaliacao.sacm.enums.StatusConsulta;
import br.edu.avaliacao.sacm.model.Consulta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.Repository;
@Repository public class ConsultaRepository {
 private final JdbcTemplate jdbc;
 private final MedicoRepository medicos;
 private final PacienteRepository pacientes;
 public ConsultaRepository(JdbcTemplate jdbc,MedicoRepository medicos,PacienteRepository pacientes){this.jdbc=jdbc;this.medicos=medicos;this.pacientes=pacientes;}
 public long salvar(Consulta c){KeyHolder kh=new GeneratedKeyHolder();jdbc.update(con->{PreparedStatement ps=con.prepareStatement("INSERT INTO consulta(medico_id,paciente_id,data_hora,motivo,status,data_agendamento,observacao) VALUES(?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);ps.setLong(1,c.getMedico().getId());ps.setLong(2,c.getPaciente().getId());ps.setTimestamp(3,Timestamp.valueOf(c.getDataHora()));ps.setString(4,c.getMotivo());ps.setString(5,c.getStatus().name());ps.setTimestamp(6,Timestamp.valueOf(c.getDataAgendamento()));ps.setString(7,c.getObservacao());return ps;},kh);return kh.getKey().longValue();}
 private Consulta map(ResultSet r,int n)throws SQLException{return new Consulta(r.getLong("id"),medicos.buscar(r.getLong("medico_id")).orElseThrow(),pacientes.buscar(r.getLong("paciente_id")).orElseThrow(),r.getTimestamp("data_hora").toLocalDateTime(),r.getString("motivo"),StatusConsulta.valueOf(r.getString("status")),r.getTimestamp("data_agendamento").toLocalDateTime(),r.getString("observacao"));}
 public Optional<Consulta> buscar(long id){return jdbc.query("SELECT * FROM consulta WHERE id=?",this::map,id).stream().findFirst();}
 public List<Consulta> listarPorMedico(long id){return jdbc.query("SELECT * FROM consulta WHERE medico_id=? ORDER BY data_hora",this::map,id);}
 public List<Consulta> listarPorPaciente(long id){return jdbc.query("SELECT * FROM consulta WHERE paciente_id=? ORDER BY data_hora",this::map,id);}
 public List<Consulta> listarTodas(){return jdbc.query("SELECT * FROM consulta ORDER BY data_hora",this::map);}
 public boolean existeConflito(long medicoId,long pacienteId,LocalDateTime dataHora){Integer n=jdbc.queryForObject("SELECT COUNT(*) FROM consulta WHERE data_hora=? AND status IN ('AGENDADA','CONFIRMADA') AND (medico_id=? OR paciente_id=?)",Integer.class,Timestamp.valueOf(dataHora),medicoId,pacienteId);return n!=null&&n>0;}
 public void atualizarStatus(long id,StatusConsulta valor){jdbc.update("UPDATE consulta SET status=? WHERE id=?",valor.name(),id);}
}
