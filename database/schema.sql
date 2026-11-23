-- Execute com usuario administrador no MySQL 8.x.
CREATE DATABASE IF NOT EXISTS sacm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sacm_db;
CREATE TABLE IF NOT EXISTS pessoa (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nome VARCHAR(120) NOT NULL,
 cpf CHAR(11) NOT NULL UNIQUE,
 data_nascimento DATE NOT NULL,
 email VARCHAR(160) NOT NULL,
 telefone VARCHAR(30) NOT NULL,
 cidade VARCHAR(100) NOT NULL,
 estado CHAR(2) NOT NULL,
 data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 status VARCHAR(15) NOT NULL DEFAULT 'ATIVO'
);
CREATE TABLE IF NOT EXISTS medico (
 id BIGINT PRIMARY KEY,
 crm VARCHAR(30) NOT NULL UNIQUE,
 especialidade VARCHAR(30) NOT NULL,
 disponibilidade VARCHAR(20) NOT NULL,
 valor_consulta DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_medico_pessoa FOREIGN KEY (id) REFERENCES pessoa(id)
);
CREATE TABLE IF NOT EXISTS paciente (
 id BIGINT PRIMARY KEY,
 numero_prontuario VARCHAR(40) NOT NULL UNIQUE,
 convenio VARCHAR(100) NOT NULL,
 contato_emergencia VARCHAR(120) NOT NULL,
 telefone_emergencia VARCHAR(30) NOT NULL,
 CONSTRAINT fk_paciente_pessoa FOREIGN KEY (id) REFERENCES pessoa(id)
);
CREATE TABLE IF NOT EXISTS consulta (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 medico_id BIGINT NOT NULL,
 paciente_id BIGINT NOT NULL,
 data_hora DATETIME NOT NULL,
 motivo VARCHAR(500) NOT NULL,
 status VARCHAR(20) NOT NULL,
 data_agendamento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 observacao VARCHAR(500),
 CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES medico(id),
 CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
 INDEX idx_consulta_medico_hora (medico_id, data_hora, status),
 INDEX idx_consulta_paciente_hora (paciente_id, data_hora, status)
);
-- Para criar uma conta restrita, execute como administrador, substituindo a senha:
-- CREATE USER 'sacm_app'@'localhost' IDENTIFIED BY 'SENHA_ESCOLHIDA_LOCALMENTE';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON sacm_db.* TO 'sacm_app'@'localhost';
