# SACM — Avaliação Prática POO — 2º Bimestre

Projeto de console Java 21 + Spring Boot + Spring JDBC/JdbcTemplate + MySQL 8, desenvolvido conforme enunciado do professor. Sem interface web, JPA ou endpoints REST.

## Personalização obrigatória

1. Renomeie a pasta **POO_2BIM_seu_nome** usando seu nome e sobrenome em minúsculas, por exemplo `POO_2BIM_joao_silva`.
2. No `pom.xml`, altere `<artifactId>` e `<name>` com o mesmo nome. Se preferir, use a substituição global do VS Code para `POO_2BIM_seu_nome`.
3. Preencha a identificação e as evidências do relatório e declare o uso real de IA.

## Pré-requisitos

- JDK **21** (`java -version`)
- Maven 3.9+ (`mvn -version`) ou Maven Wrapper, se instalado separadamente
- MySQL Server **8.x**, VS Code com extensões Java/Spring (opcional)

> Este ZIP não contém Maven Wrapper (`mvnw`) nem o Maven instalado. Use Maven local.

## Banco

1. No MySQL Workbench ou no terminal MySQL, execute **`database/schema.sql`** em usuário administrador. Pode usar `mysql -u root -p < database/schema.sql` no terminal com cliente MySQL.
2. Crie um usuário com permissões SELECT, INSERT, UPDATE e DELETE apenas em `sacm_db`. Há comandos de exemplo comentados no fim de `schema.sql`; **escolha sua própria senha e não a publique**.
3. Configure variáveis de ambiente no terminal antes de executar:

   **Windows PowerShell**:
   ```powershell
   $env:DB_USER="sacm_app"
   $env:DB_PASSWORD="SUA_SENHA_LOCAL"
   mvn clean test
   mvn spring-boot:run
   ```

   **Linux/macOS**:
   ```bash
   export DB_USER=sacm_app
   export DB_PASSWORD='SUA_SENHA_LOCAL'
   mvn clean test
   mvn spring-boot:run
   ```

> Substitua SUA_SENHA_LOCAL; jamais registre a senha real no README ou em commits.

## Menu do console

1. Cadastrar médico (ativo e disponível por padrão)
2. Cadastrar paciente (ativo por padrão)
3. Listar cadastros e IDs
4. Agendar consulta (data `dd/MM/aaaa HH:mm`, no futuro)
5. Listar todas as consultas ou filtrar por pessoa
6. Confirmar consulta
7. Cancelar consulta
8. Finalizar consulta
9. Alterar disponibilidade do médico ou ativar/desativar pessoa
0. Sair

O horário deve ter no mínimo 1 minuto de antecedência; CPF tem 11 dígitos sem pontuação. A agenda compara o mesmo horário exato (não calcula duração ou sobreposição de intervalos).

## Demonstração obrigatória (executar no seu computador)

1. Cadastre um médico ativo/disponível e um paciente ativo; registre os IDs.
2. Agende uma consulta para um dia futuro; anote o ID da consulta.
3. Tente agendar outra consulta para o mesmo médico no mesmo dia/hora; deve exibir conflito.
4. Cadastre outro médico e tente agendar **o mesmo paciente** no mesmo dia/hora; deve exibir conflito.
5. Cancele a primeira consulta e agende outra no horário liberado; deve funcionar.
6. Confirme a nova consulta, finalize e tente cancelar: estado final não aceita mudança.
7. Saia e reinicie o aplicativo; verifique que os registros continuam lá.
8. Execute os comandos de **`database/evidencias_select.sql`** no MySQL e tire prints do terminal e dos resultados SELECT.
9. Desative um paciente ou marque um médico como indisponível e tente agendar; deve ocorrer uma mensagem de regra de negócio.

## Arquitetura

- `model`: Pessoa abstrata, Medico, Paciente, Consulta.
- `enums`: StatusPessoa, DisponibilidadeMedico, Especialidade, StatusConsulta.
- `interfaces`: Agendavel.
- `repository`: JdbcTemplate e SQL para persistência.
- `service`: ConsultaService com validações e transações.
- `exception`: erros de negócio e conflitos.
- `console`: SistemaConsole com menu e Scanner.

Em `ConsultaService`, `@Transactional` protege gravação e mudança de status. Para agendamento simultâneo, bloqueia as linhas de médico e paciente com `SELECT ... FOR UPDATE` antes da consulta de conflitos. CPF único é garantido pela coluna `pessoa.cpf UNIQUE` compartilhada entre médicos e pacientes, e CRM por `medico.crm UNIQUE`.

## Testes

Execute `mvn test`. Os testes em `ConsultaServiceTest` usam mocks e verificam regras sem precisar de MySQL; **não substituem** a demonstração real da persistência MySQL nem os prints requeridos. Não declare que testes passaram sem executá-los localmente.

## Evidências, relatório e apresentação

- Leia `RELATORIO_TECNICO_RASCUNHO.md`, complete dados pessoais e substitua os campos de prints/resultados conforme execução real.
- Roteiro de defesa em `ROTEIRO_APRESENTACAO.md`.
- Coloque prints em `evidencias/` (a pasta inclui `.gitkeep`).
- Entregue ZIP/repositório **sem** `target/`, `.env`, senhas, arquivos pessoais ou logs.

## IA e responsabilidade acadêmica

Parte do projeto foi produzida com auxílio de **ChatGPT (OpenAI)**.
