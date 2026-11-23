package br.edu.avaliacao.sacm.repository;
import br.edu.avaliacao.sacm.model.Pessoa;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.*;
import java.sql.*;
final class PessoaJdbc {
 private PessoaJdbc(){}
 static long inserir(JdbcTemplate jdbc,Pessoa p){
  KeyHolder kh=new GeneratedKeyHolder();
  jdbc.update(con->{PreparedStatement s=con.prepareStatement("INSERT INTO pessoa (nome,cpf,data_nascimento,email,telefone,cidade,estado,data_cadastro,status) VALUES (?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
   s.setString(1,p.getNome());s.setString(2,p.getCpf());s.setDate(3,Date.valueOf(p.getDataNascimento()));s.setString(4,p.getEmail());s.setString(5,p.getTelefone());s.setString(6,p.getCidade());s.setString(7,p.getEstado());s.setTimestamp(8,Timestamp.valueOf(p.getDataCadastro()));s.setString(9,p.getStatus().name());return s;},kh);
  return kh.getKey().longValue();
 }
}
