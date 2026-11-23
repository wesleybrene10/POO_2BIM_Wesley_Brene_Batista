USE sacm_db;
-- Substitua os IDs pelos exibidos no terminal.
SELECT p.id, p.nome, p.cpf, p.status, m.crm, m.disponibilidade
FROM pessoa p JOIN medico m ON p.id=m.id ORDER BY p.id;
SELECT p.id, p.nome, p.status, a.numero_prontuario
FROM pessoa p JOIN paciente a ON p.id=a.id ORDER BY p.id;
SELECT c.id, c.medico_id, c.paciente_id, c.data_hora, c.motivo, c.status,
       c.data_agendamento FROM consulta c ORDER BY c.id;
-- Para ver cancelamento e nova consulta no horario liberado:
SELECT id, medico_id, paciente_id, data_hora, status FROM consulta
ORDER BY data_hora, id;
-- Reinicie o programa e execute estas consultas novamente para comprovar RN08.
