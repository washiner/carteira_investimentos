# 💼 Carteira de Investimentos API

API REST para controle de carteira de investimentos pessoais. Permite gerenciar usuários, carteiras, ativos e categorias com todos os tipos de relacionamento JPA.

---

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 25.0.1 |
| Spring Boot | 4.0.6 |
| PostgreSQL | 16.13 |
| Flyway | Migration |
| Lombok | Completo |
| SpringDoc OpenAPI | Swagger UI |
| Docker | Compose v3.8 |
| Hibernate | 7.2.12.Final |

---

## 📐 Relacionamentos

```
Usuario  (1) ──── (1)  Carteira    @OneToOne
Carteira (1) ──── (N)  Ativo       @OneToMany / @ManyToOne
Ativo    (N) ──── (N)  Categoria   @ManyToMany
```

**Regra das FKs:**
- `carteiras.usuario_id` → FK do `@OneToOne`
- `ativos.carteira_id` → FK do `@ManyToOne`
- `ativos_categorias` → tabela intermediária do `@ManyToMany`

---

## ⚙️ Como Rodar

### Pré-requisitos
- Docker instalado
- Java 21+ instalado
- Maven instalado

### 1. Clone o repositório
```bash
git clone https://github.com/washiner/carteira-investimentos
cd carteira-investimentos
```

### 2. Suba o banco de dados
```bash
docker-compose up -d
```

### 3. Execute a aplicação
```bash
./mvnw spring-boot:run
```

### 4. Acesse o Swagger
```
http://localhost:8080/swagger-ui.html
```

---

## 🗄️ Configuração do Banco

```properties
URL:      jdbc:postgresql://localhost:5432/carteira_db
Usuário:  carteira_user
Senha:    carteira123
```

As tabelas são criadas automaticamente pelo **Flyway** na primeira execução:

| Migration | Descrição |
|---|---|
| V1 | Tabela `usuarios` |
| V2 | Tabela `carteiras` |
| V3 | Tabela `categorias` + dados iniciais |
| V4 | Tabela `ativos` |
| V5 | Tabela intermediária `ativos_categorias` |

---

## 📡 Endpoints

### Usuários
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Criar usuário |
| `GET` | `/usuarios` | Listar com paginação |
| `GET` | `/usuarios/{id}` | Buscar por ID |
| `PUT` | `/usuarios/{id}` | Atualizar |
| `DELETE` | `/usuarios/{id}` | Deletar |

### Carteiras
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/carteiras` | Criar carteira |
| `GET` | `/carteiras/{id}` | Buscar por ID |
| `GET` | `/carteiras/usuario/{usuarioId}` | Buscar pelo usuário |

### Ativos
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/ativos` | Adicionar ativo |
| `GET` | `/ativos/carteira/{carteiraId}` | Listar por carteira |
| `GET` | `/ativos/{id}` | Buscar por ID |
| `PUT` | `/ativos/{id}` | Atualizar |
| `DELETE` | `/ativos/{id}` | Remover |

### Categorias
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/categorias` | Criar categoria |
| `GET` | `/categorias` | Listar com paginação |
| `GET` | `/categorias/{id}` | Buscar por ID |
| `GET` | `/categorias/tipo/{tipo}` | Filtrar por tipo |

---

## 📦 ENUMs

**TipoAtivo**
```
ACAO | FII | ETF | CDB | TESOURO_DIRETO | CRIPTO
```

**TipoCategoria**
```
RENDA_VARIAVEL | RENDA_FIXA | FUNDO_IMOBILIARIO | FUNDO_INTERNACIONAL | RESERVA_EMERGENCIA
```

---

## 🗂️ Estrutura do Projeto

```
src/main/java/com/washiner/carteira_investimentos/
├── controller/       # Camada HTTP — recebe e responde requisições
├── service/          # Regras de negócio
├── repository/       # Acesso ao banco via Spring Data JPA
├── model/            # Entidades JPA mapeadas
├── dto/
│   ├── request/      # O que entra na API (com validações)
│   └── response/     # O que sai da API
├── enums/            # TipoAtivo e TipoCategoria
└── exception/        # GlobalExceptionHandler e ErroResponse
```

---

## 🧪 Exemplo de Uso

### Criar usuário
```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "Washiner", "email": "washiner@email.com", "senha": "senha123"}'
```

### Criar carteira
```bash
curl -X POST http://localhost:8080/carteiras \
  -H "Content-Type: application/json" \
  -d '{"nome": "Minha Carteira", "usuarioId": 1}'
```

### Adicionar ativo com categorias
```bash
curl -X POST http://localhost:8080/ativos \
  -H "Content-Type: application/json" \
  -d '{
    "ticker": "PETR4",
    "nome": "Petrobras PN",
    "tipo": "ACAO",
    "quantidade": 100,
    "precoMedio": 38.50,
    "valorAtual": 41.20,
    "carteiraId": 1,
    "categoriaIds": [1, 5]
  }'
```

---

## 👤 Autor

**Washiner** — Desenvolvedor Java & Spring Boot  
Projeto de estudo desenvolvido no Japão 🇯🇵 com objetivo de preparação para concurso público.

---

*Fase 1 concluída — CRUD completo com os 4 relacionamentos JPA.*
