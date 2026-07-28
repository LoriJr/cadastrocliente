# 📋 CadastroCliente API

API RESTful para cadastro e gerenciamento de clientes, com autenticação JWT, verificação de e-mail e arquitetura em camadas.

> Desenvolvida com **Java 21** e **Spring Boot 3.3**, utilizando boas práticas de segurança, validação e containerização.

---

## 📑 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Modelo de Dados](#-modelo-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Autenticação e Segurança](#-autenticação-e-segurança)
- [Como Rodar a Aplicação](#-como-rodar-a-aplicação)
- [Docker](#-docker)
- [Kubernetes](#-kubernetes)
- [CI/CD](#-cicd)
- [Documentação Swagger](#-documentação-swagger)
- [Internacionalização (i18n)](#-internacionalização-i18n)
- [Autor](#-autor)

---

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 3.3.5 | Framework base |
| **Spring Data JPA** | — | Persistência de dados |
| **Spring Security** | — | Autenticação e autorização |
| **Spring Mail** | — | Envio de e-mails |
| **PostgreSQL** | 15 | Banco de dados relacional |
| **Flyway** | — | Migração e versionamento do banco |
| **JWT (Auth0)** | 4.5.2 | Tokens de autenticação |
| **MapStruct** | 1.5.5 | Mapeamento entre DTOs e Entidades |
| **Lombok** | 1.18.32 | Redução de boilerplate |
| **Springdoc OpenAPI** | 2.5.0 | Documentação Swagger |
| **Maven** | — | Gerenciamento de dependências |
| **Docker** | — | Containerização |
| **Kubernetes** | — | Orquestração de containers |
| **GitHub Actions** | — | CI/CD |

---

## 🏗 Arquitetura do Projeto

O projeto segue uma **arquitetura em camadas** com separação clara de responsabilidades:

```
src/main/java/com/viratech/cadastrocliente/
│
├── controller/              ← @RestController (Endpoints da API)
│   ├── UserController
│   ├── AuthenticationController
│   └── UserCredentialController
│
├── service/                 ← @Service (Lógica de negócio)
│   ├── UserService
│   ├── UserCredentialService
│   ├── UserVerificationService
│   └── EmailService
│
├── repository/              ← @Repository (Interface JpaRepository)
│   ├── UserRepository
│   ├── UserCredentialRepository
│   └── UserVerificationTokenRepository
│
├── model/
│   ├── entity/              ← @Entity (Espelho do banco)
│   │   ├── User
│   │   ├── Address (Embeddable)
│   │   ├── UserCredential (UserDetails)
│   │   └── UserVerificationToken
│   ├── enums/               ← Enumerações
│   │   └── UserStatus
│   ├── mapper/              ← @Mapper (MapStruct)
│   │   ├── UserMapper
│   │   ├── AddressMapper
│   │   └── UserCredentialMapper
│   └── exceptions/          ← @ControllerAdvice e Custom Exceptions
│       ├── GlobalExceptionHandler
│       ├── ResourceNotFoundException
│       ├── InvalidLoginException
│       ├── CustomValidationException
│       └── ApiResponseError
│
├── dto/                     ← Records (Entrada/Saída da API)
│   ├── UserRequestDTO
│   ├── UserResponseDTO
│   ├── AddressDTO
│   ├── UserCredentialRequestDTO
│   └── UserCredentialResponseDTO
│
├── authentication/          ← Serviço de tokens JWT
│   ├── TokenService
│   └── RoleBusinessException
│
└── security/                ← Configuração Spring Security
    ├── ConfigSecurity
    ├── FilterAccessToken
    ├── AuthToken (record)
    └── RefreshToken (record)
```

---

## 📊 Modelo de Dados

### Entidade `User`

| Campo | Tipo | Restrição |
|---|---|---|
| `id` | `Long` | PK, auto-increment |
| `name` | `String` | NOT NULL |
| `email` | `String` | NOT NULL, UNIQUE |
| `phone` | `String` | NOT NULL |
| `cpf` | `String` | NOT NULL, UNIQUE |
| `rg` | `String` | NOT NULL, UNIQUE |
| `birthDate` | `LocalDate` | NOT NULL |
| `createdAt` | `LocalDateTime` | NOT NULL (default: `now()`) |
| `address` | `Address` | Embedded |
| `status` | `UserStatus` | Enum (`PENDING_VERIFICATION`, `ACTIVE`, `BLOCKED`) |

### Endereço (Embeddable `Address`)

| Campo | Tipo | Restrição |
|---|---|---|
| `zipCode` | `String` | NOT NULL |
| `addressLine1` | `String` | NOT NULL |
| `number` | `String` | NOT NULL |
| `addressLine2` | `String` | Opcional |
| `neighborhood` | `String` | NOT NULL |
| `city` | `String` | NOT NULL |
| `state` | `String` | NOT NULL |

### Migrações Flyway

| Versão | Descrição |
|---|---|
| `V1` | Criação da tabela `users` |
| `V2` | Criação da tabela `user_credentials` |
| `V3` | Criação da tabela `user_verification_token` |
| `V4` | Alteração na tabela `users` |
| `V5` | Remoção da coluna `user_status` |
| `V6` | Alteração na tabela `user_verification_token` |

---

## 🔌 Endpoints da API

### 👤 Usuários — `/users`

| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `GET` | `/users` | Lista todos os usuários | 🔒 Sim |
| `GET` | `/users/{email}` | Busca usuário por e-mail | 🔒 Sim |
| `POST` | `/users` | Cadastra um novo usuário | 🔓 Não |
| `PATCH` | `/users/{id}` | Atualiza dados de um usuário | 🔒 Sim |
| `DELETE` | `/users/{email}` | Remove um usuário pelo e-mail | 🔒 Sim |

#### Request Body — `POST /users`

```json
{
  "name": "João da Silva",
  "email": "joao@email.com",
  "phone": "11999998888",
  "cpf": "123.456.789-00",
  "rg": "1234567",
  "birthDate": "1990-05-15",
  "address": {
    "zipCode": "01001-000",
    "addressLine1": "Rua Exemplo",
    "number": "100",
    "addressLine2": "Apto 42",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP"
  }
}
```

#### Response Body — `201 Created`

```json
{
  "id": 1,
  "name": "João da Silva",
  "email": "joao@email.com",
  "cpf": "123.456.789-00",
  "rg": "1234567",
  "birthDate": "1990-05-15",
  "address": {
    "zipCode": "01001-000",
    "addressLine1": "Rua Exemplo",
    "number": "100",
    "addressLine2": "Apto 42",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP"
  },
  "createdAt": "2026-07-28T18:00:00"
}
```

---

### 🔐 Autenticação — `/api/v1/auth`

| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Registra credenciais de acesso | 🔓 Não |
| `POST` | `/api/v1/auth/login` | Realiza login (retorna JWT) | 🔓 Não |
| `POST` | `/api/v1/auth/refresh-token` | Renova o token de acesso | 🔓 Não |
| `GET` | `/api/v1/auth/verify?token=...` | Verifica e ativa conta via e-mail | 🔓 Não |

#### Request Body — `POST /api/v1/auth/login`

```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

#### Response Body — `200 OK`

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

---

## 🔒 Autenticação e Segurança

A aplicação implementa um sistema completo de segurança com **Spring Security + JWT**:

- **Autenticação Stateless** — Sem sessão no servidor; cada requisição carrega seu próprio token
- **BCrypt** — Senhas criptografadas com `BCryptPasswordEncoder`
- **Access Token + Refresh Token** — Dois tokens JWT para controle de sessão
- **Verificação por E-mail** — Ao registrar credenciais, o usuário recebe um e-mail com link de ativação
- **Filtro JWT** — `FilterAccessToken` intercepta requisições e valida o token antes de chegar ao controller
- **CORS configurável** — Via propriedade `app.cors.allowed-origin`

### Fluxo de Cadastro e Autenticação

```
1. POST /users               → Cadastra dados pessoais (status: PENDING_VERIFICATION)
2. POST /api/v1/auth/register → Cria credenciais (email + senha) + envia e-mail de verificação
3. GET  /api/v1/auth/verify   → Usuário clica no link do e-mail → status: ACTIVE
4. POST /api/v1/auth/login    → Autentica e recebe access + refresh token
5. Requisições autenticadas   → Header: Authorization: Bearer <accessToken>
```

### Status do Usuário

| Status | Descrição |
|---|---|
| `PENDING_VERIFICATION` | Aguardando confirmação por e-mail |
| `ACTIVE` | Conta ativada, login permitido |
| `BLOCKED` | Conta bloqueada, login negado |

---

## 🚀 Como Rodar a Aplicação

### Pré-requisitos

- **JDK 21** ou superior
- **Maven** instalado (ou use o wrapper `./mvnw`)
- **PostgreSQL 15** em execução

### 1. Clone o repositório

```bash
git clone https://github.com/LoriJr/cadastrocliente.git
cd cadastrocliente
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e ajuste conforme necessário:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Edite o `application.properties` com suas credenciais:

```properties
# BANCO DE DADOS
spring.datasource.url=jdbc:postgresql://localhost:5432/clientedb
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA

# E-MAIL (Gmail)
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app

# CORS
app.cors.allowed-origin=http://localhost:3000

# JWT (defina um secret seguro)
api.security.token.secret=SEU_SECRET_JWT
```

### 3. Crie o banco de dados

```sql
CREATE DATABASE clientedb;
```

> As tabelas serão criadas automaticamente pelo **Flyway** na primeira execução.

### 4. Execute o projeto

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: **`http://localhost:8081`**

---

## 🐳 Docker

### Docker Compose (desenvolvimento rápido)

Sobe a aplicação + PostgreSQL com um único comando:

```bash
docker-compose up -d
```

Isso criará:

| Container | Porta | Descrição |
|---|---|---|
| `postgres-db` | `5432` | PostgreSQL 15 com banco `clientedb` |
| `cadastrocliente-app` | `8081` | Aplicação Spring Boot |

### Build manual da imagem

```bash
docker build -t cadastrocliente:latest .
```

O **Dockerfile** utiliza multi-stage build:
1. **Build stage** — Maven + JDK 21 para compilar o projeto
2. **Run stage** — JDK 21 slim para executar o JAR

---

## ☸️ Kubernetes

O diretório `k8s/` contém os manifestos para deploy em um cluster Kubernetes:

| Arquivo | Descrição |
|---|---|
| `postgres-pvc.yaml` | PersistentVolumeClaim para dados do PostgreSQL |
| `postgres-deployment.yaml` | Deployment do PostgreSQL |
| `postgres-service.yaml` | Service interno do PostgreSQL |
| `cadastrocliente-deployment.yaml` | Deployment da aplicação |
| `cadastrocliente-service.yaml` | Service da aplicação |

### Deploy no cluster

```bash
kubectl apply -f k8s/
```

---

## ⚙️ CI/CD

O projeto possui um pipeline automatizado com **GitHub Actions** (`.github/workflows/docker.yml`):

### Pipeline atual (Docker Hub)

```
Push na branch main
    → Checkout do código
    → Setup Java 21 (Temurin)
    → Build Maven (sem testes)
    → Login no Docker Hub
    → Build da imagem Docker
    → Push para Docker Hub
```

> O pipeline também possui configuração comentada para deploy no **Google Cloud (GKE + Artifact Registry)**, pronta para ser habilitada.

### Secrets necessários no GitHub

| Secret | Descrição |
|---|---|
| `DOCKER_USERNAME` | Usuário do Docker Hub |
| `DOCKER_PASSWORD` | Senha/token do Docker Hub |

---

## 📖 Documentação Swagger

Com a aplicação rodando, acesse a documentação interativa da API:

- **Swagger UI**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## 🌍 Internacionalização (i18n)

As mensagens de validação e erro suportam múltiplos idiomas:

| Arquivo | Idioma |
|---|---|
| `messages.properties` | Padrão (inglês) |
| `messages_en.properties` | Inglês |
| `messages_pt_BR.properties` | Português (Brasil) |

---

## 👨‍💻 Autor

**Lou Junior** — [@LoriJr](https://github.com/LoriJr)

---

<p align="center">
  Feito com ☕ e Spring Boot
</p>
