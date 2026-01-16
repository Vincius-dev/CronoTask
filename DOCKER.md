# CronoTask - Docker Setup

## 📦 Estrutura Docker

O projeto está dockerizado com os seguintes componentes:

- **PostgreSQL 15**: Banco de dados
- **Spring Boot Application**: API REST

## 🚀 Como executar

### Pré-requisitos

- Docker instalado
- Docker Compose instalado

### Passos para executar

1. **Clone o repositório** (se ainda não tiver):
```bash
git clone <url-do-repositorio>
cd CronoTask
```

2. **Configure as variáveis de ambiente** (opcional):
```bash
cp .env.example .env
# Edite o arquivo .env conforme necessário
```

3. **Inicie os containers**:
```bash
docker-compose up -d
```

4. **Acompanhe os logs**:
```bash
# Todos os serviços
docker-compose logs -f

# Apenas a aplicação
docker-compose logs -f app

# Apenas o banco de dados
docker-compose logs -f db
```

5. **Acesse a aplicação**:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## 🛠️ Comandos úteis

### Parar os containers
```bash
docker-compose down
```

### Parar e remover volumes (limpa o banco de dados)
```bash
docker-compose down -v
```

### Rebuild da aplicação
```bash
docker-compose up -d --build app
```

### Ver status dos containers
```bash
docker-compose ps
```

### Executar comandos dentro do container
```bash
# Acessar o container da aplicação
docker exec -it cronotask_app sh

# Acessar o PostgreSQL
docker exec -it cronotask_postgres psql -U postgres -d dev
```

## 🔧 Configurações

### Variáveis de Ambiente

Você pode personalizar as seguintes variáveis no arquivo `.env`:

```env
# Database
POSTGRES_DB=dev
POSTGRES_USER=postgres
POSTGRES_PASSWORD=dev123
DB_PORT=5432

# Application
SERVER_PORT=8080
JPA_SHOW_SQL=false
```

### Portas

- **8080**: API Spring Boot
- **5432**: PostgreSQL

## 🏗️ Arquitetura Docker

### Dockerfile (Multi-stage)

- **Stage 1 - Build**: Compila o projeto usando Maven
- **Stage 2 - Runtime**: Imagem otimizada apenas com JRE

### Docker Compose

- **Rede isolada**: `cronotask-network`
- **Volumes persistentes**: `postgres_data`
- **Health checks**: Garantem que o banco esteja pronto antes da aplicação iniciar
- **Restart policy**: Containers reiniciam automaticamente em caso de falha

## 📊 Monitoramento

O container da aplicação possui um health check configurado que verifica a saúde da aplicação a cada 30 segundos.

Verifique o status:
```bash
docker inspect cronotask_app | grep -A 10 Health
```

## 🐛 Troubleshooting

### A aplicação não conecta ao banco

1. Verifique se o banco está saudável:
```bash
docker-compose ps
```

2. Verifique os logs do banco:
```bash
docker-compose logs db
```

### Porta já em uso

Se as portas 8080 ou 5432 já estiverem em uso, altere no arquivo `.env`:
```env
SERVER_PORT=8081
DB_PORT=5433
```

### Limpar tudo e recomeçar

```bash
docker-compose down -v
docker system prune -a
docker-compose up -d --build
```

## 📝 Desenvolvimento

Para desenvolvimento com hot-reload, você pode usar o perfil de desenvolvimento:

```bash
# Inicie apenas o banco de dados
docker-compose up -d db

# Execute a aplicação localmente via IDE ou Maven
./mvnw spring-boot:run
```
