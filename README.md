# CadastroClienteAPI
API para cadastro de usuários para ser usada em formulários de cadastro de clientes.

## 1. CadastroClienteApplication
O objetivo da aplicação backend, é disponibilizar um endpoint para que outra aplicação frontend possa através de um formulário, preencher e cadastrar dados de usuários.

## 2. Tecnologias Utilizadas
- Linguagem: Java 21
- Framework Principal: Spring Boot 4.x
- Persistência de Dados: Spring Data JPA
- Banco de Dados: PostgreSQL
- Gerenciador de Dependências: Maven
- Documentação: Swagger (OpenAPI)

## 3. Estrutura do Projeto
- Arquitetura em camadas
```mermaid
  src/main/java/com/viratech/cadastrocliente
  │
  ├── controller/       <-- @RestController (Recebe DTO)
  ├── service/          <-- @Service (Lógica de negócio + uso do Mapper)
  ├── repository/       <-- @Repository (Interface JpaRepository)
  └── model/
      ├── entity/       <-- @Entity (Espelho do banco)
      ├── dto/          <-- Records ou Class (O que vai no JSON)
      ├── mapper/       <-- Interfaces @Mapper (MapStruct)
      └── exceptions/   <-- @ControllerAdvice e Custom Exceptions
````
## 4. Como Rodar a Aplicação
O guia passo a passo para clonar e executar o projeto.

### Pré-requisitos
- JDK 21 ou superior instalado.
- Maven instalado.

## Instalação e Execução
1. ### Clone o repositório:
```bash
  https://github.com/LoriJr/cadastrocliente.git
```
2. ### Configure as variáveis de ambiente:
    O exemplo de variáveis de ambiente é baseado no application.properties.
    ```properties
   # NOME DA APLICAÇÃO
    spring.application.name=cadastrocliente
   
   # PORTA DA APLICAÇÃO
    server.port = 8081
   
   # CONFIGURACAO DO BANCO
    spring.datasource.url=jdbc:postgresql://localhost:5432/NOME_DO_SEU_BANCO
    spring.datasource.username=USUARIO
    spring.datasource.password=SENHA
    spring.datasource.driver-class-name=org.postgresql.Driver

    # JPA / HIBERNATE
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update

    # MOSTRAR SQL NO CONSOLE
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # CONFIGURAÇÃO DAS MENSAGENS PERSONALIZADAS
    spring.messages.basename=messages
    spring.messages.encoding=UTF-8
    spring.messages.fallback-to-system-locale=true
   ```

3. ### Execute o Projeto:
```bash
    ./mvnw spring-boot:run
```

## 5. Endpoints Principais
-- TODO

