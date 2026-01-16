# 🕐 CronoTask

Sistema de gerenciamento de tarefas com cronômetro integrado, desenvolvido com Spring Boot seguindo os princípios de Clean Architecture.

## 📋 Sobre o Projeto

CronoTask é uma aplicação backend para gerenciamento de tarefas pessoais com funcionalidade de cronômetro. Permite criar, atualizar e acompanhar o tempo gasto em cada tarefa.

### Principais Funcionalidades

- ✅ Gerenciamento completo de usuários (CRUD)
- ✅ Gerenciamento de tarefas por usuário
- ✅ Controle de tempo de execução das tarefas
- ✅ Iniciar/pausar cronômetro de tarefas
- ✅ API RESTful documentada com Swagger
- ✅ Tratamento de exceções personalizado
- ✅ Validações de integridade (email único, etc.)

### Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.1**
- **PostgreSQL 15**
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Docker & Docker Compose**
- **SpringDoc OpenAPI** (Swagger)

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- Java 21
- Maven
- Docker e Docker Compose
- Git

### Passo a Passo

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd CronoTask
```

2. **Configure as variáveis de ambiente**
```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o .env com suas configurações (opcional - valores padrão já funcionam)
```

3. **Suba o banco de dados PostgreSQL**
```bash
docker-compose up -d
```

4. **Execute a aplicação**
```bash
# Com Maven
mvn spring-boot:run

# Ou compile e execute o JAR
mvn clean package
java -jar target/CronoTask-0.0.1-SNAPSHOT.jar
```

5. **Acesse a aplicação**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📚 Documentação da API

### Swagger UI (Recomendado)

Acesse a documentação interativa completa em:

```
http://localhost:8080/swagger-ui.html
```

Ou a especificação OpenAPI JSON:

```
http://localhost:8080/v3/api-docs
```

### Coleção Bruno API Client

Para testar a API, importe a coleção do Bruno localizada em:

```
/API de Usuários
```

> 💡 **Bruno** é um cliente de API open-source alternativo ao Postman. Baixe em: https://www.usebruno.com/

**Como importar no Bruno:**
1. Abra o Bruno
2. Clique em "Open Collection"
3. Navegue até a pasta `/API de Usuários` do projeto
4. Abra a coleção completa com todos os endpoints de Tasks e Users

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **SOLID**:

```
src/
├── main/java/vinicius/dev/CronoTask/
│   ├── controller/          # Camada de apresentação (REST)
│   ├── domain/
│   │   ├── entities/        # Entidades de domínio
│   │   ├── repositories/    # Interfaces de repositórios
│   │   └── usecases/        # Casos de uso (lógica de negócio)
│   ├── dto/                 # Data Transfer Objects
│   └── infra/
│       ├── entities/        # Entidades JPA
│       ├── exceptions/      # Tratamento de exceções
│       ├── mappers/         # MapStruct mappers
│       └── repositories/    # Implementações de repositórios
```

## 🔑 Endpoints Principais

### Usuários (`/api/users`)
- `POST /api/users` - Criar usuário
- `GET /api/users/{id}` - Buscar por ID
- `GET /api/users/email/{email}` - Buscar por email
- `PUT /api/users/{id}` - Atualizar completo
- `PATCH /api/users/{id}` - Atualizar parcial
- `DELETE /api/users/{id}` - Deletar

### Tarefas (`/api/tasks`)
- `POST /api/tasks` - Criar tarefa
- `GET /api/tasks/{id}` - Buscar por ID
- `GET /api/tasks/user/{userId}` - Listar tarefas do usuário
- `PUT /api/tasks/{id}` - Atualizar completo
- `PATCH /api/tasks/{id}` - Atualizar parcial (incluindo start/stop do cronômetro)
- `DELETE /api/tasks/{id}` - Deletar

## 🗄️ Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para configuração. Edite o arquivo `.env`:

```env
# Application
SPRING_APPLICATION_NAME=CronoTask
SERVER_PORT=8080

# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=dev
DB_USERNAME=postgres
DB_PASSWORD=dev123

# JPA/Hibernate
JPA_HIBERNATE_DDL_AUTO=update
JPA_SHOW_SQL=false
```

## 🐳 Docker

### Apenas Banco de Dados
```bash
docker-compose up -d
```

### Parar os containers
```bash
docker-compose down
```

### Remover dados do banco
```bash
docker-compose down -v
```

## 📝 Exemplos de Requisições

### Criar Usuário
```json
POST /api/users
{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

### Criar Tarefa
```json
POST /api/tasks
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Estudar Spring Boot",
  "description": "Revisar conceitos de Clean Architecture"
}
```

### Iniciar/Pausar Cronômetro
```json
PATCH /api/tasks/{id}
{
  "isRunning": true
}
```

## 🛠️ Desenvolvimento

### Compilar o projeto
```bash
mvn clean install
```

### Executar testes
```bash
mvn test
```

### Gerar o JAR
```bash
mvn clean package
```

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

Desenvolvido com ☕ e Spring Boot
