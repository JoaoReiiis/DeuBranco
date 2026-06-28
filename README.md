<div align="center">
  <img width="250" alt="Logo Deu Branco" src="https://github.com/user-attachments/assets/a0f34476-5ce3-49b5-aa7d-580fc7318300" />

  # Deu Branco

  <p>
    <img src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" alt="React" />
    <img src="https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white" alt="Vite" />
    <img src="https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript" />
    <img src="https://img.shields.io/badge/SASS-hotpink.svg?style=for-the-badge&logo=SASS&logoColor=white" alt="Sass" />
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
    <img src="https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
    <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  </p>
</div>

---

## 1. Contexto do Problema e Solução

### Problema

Estudantes que se preparam para o ENEM e vestibulares enfrentam um ciclo de estudos solitário e pouco engajante. A ausência de competição saudável e feedback imediato de desempenho torna difícil manter a motivação e identificar lacunas de conhecimento de forma objetiva. Plataformas existentes são ou excessivamente gamificadas (perdendo o foco acadêmico) ou puramente expositivas (sem interatividade).

### Solução

**Deu Branco** é uma plataforma web de estudos competitivos e gamificados. O sistema permite que estudantes entrem em arenas de conhecimento em tempo real — respondendo questões de múltipla escolha de diversas disciplinas e instituições — e disputem pontuação com outros jogadores em partidas ao vivo.

O sistema contempla:

- **Autenticação segura** de jogadores via token JWT;
- **Banco de questões** categorizado por disciplina e instituição, gerenciado por administradores;
- **Arenas multiplayer em tempo real** via WebSocket: o host cria uma sala com PIN, outros jogadores entram, e todos respondem as mesmas questões simultaneamente com cronômetro;
- **Classificação ao vivo** com pódio, pontuação e gabarito ao final de cada partida;
- **Histórico de partidas** para acompanhar a evolução ao longo do tempo;
- **Painel administrativo** exclusivo para cadastro, edição e exclusão de questões.

---

## 2. Instruções para Uso (Docker — recomendado)

> Siga este caminho se você quer apenas **rodar a aplicação** sem precisar configurar Java, Node ou PostgreSQL na sua máquina. Você só precisa do **Docker Desktop** instalado.

### Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução.

### Passo a passo

**1. Clone o repositório:**

```bash
git clone https://github.com/JoaoReiiis/DeuBranco.git
cd DeuBranco
```

**2. Suba todos os serviços com um único comando:**

```bash
docker compose up --build
```

> Na primeira execução o Docker irá baixar as imagens base e compilar o projeto. Isso pode levar alguns minutos.

**3. Acesse a aplicação:**

Abra o navegador e acesse:

```
http://localhost
```

Você verá a tela de login do **Deu Branco**. Crie uma conta clicando em *"Criar conta"* e comece a usar.

**4. Para parar a aplicação:**

```bash
docker compose down
```

Para parar **e apagar os dados do banco de dados:**

```bash
docker compose down -v
```

### Acesso ao banco de dados (opcional)

Se quiser inspecionar o banco de dados com uma ferramenta como [DBeaver](https://dbeaver.io/) ou [pgAdmin](https://www.pgadmin.org/), use as seguintes credenciais enquanto os containers estiverem no ar:

| Campo    | Valor       |
|----------|-------------|
| Host     | `localhost` |
| Porta    | `5432`      |
| Banco    | `deubranco` |
| Usuário  | `postgres`  |
| Senha    | `postgres`  |

---

## 3. Instruções para Devs

Siga as instruções abaixo para preparar seu ambiente e contribuir com o desenvolvimento do projeto.

### Pré-requisitos

- [Git](https://git-scm.com/)
- [Java 25 (JDK)](https://adoptium.net/) ou superior
- [Node.js 22+](https://nodejs.org/) e npm
- [PostgreSQL 16+](https://www.postgresql.org/download/) (ou use o container Docker só do banco — veja abaixo)
- IDE recomendada: [IntelliJ IDEA](https://www.jetbrains.com/idea/) para o backend e [VS Code](https://code.visualstudio.com/) para o frontend

---

### 3.1 Clone o projeto

```bash
git clone https://github.com/JoaoReiiis/DeuBranco.git
cd DeuBranco
```

Alternativamente, baixe o ZIP pelo GitHub clicando em **Code → Download ZIP** e extraia a pasta.

---

### 3.2 Configurar o banco de dados

**Opção A — usando Docker apenas para o banco (recomendado para devs):**

```bash
docker compose up db -d
```

Isso sobe apenas o PostgreSQL na porta `5432` sem buildar o frontend ou backend.

**Opção B — PostgreSQL local instalado:**

Crie um banco de dados chamado `deubranco` com usuário `postgres` e senha `postgres`. O Flyway aplicará as migrations automaticamente na primeira execução do backend.

---

### 3.3 Instalar dependências e executar

Abra **dois terminais separados** na raiz do projeto:

**Terminal 1 — Backend (Spring Boot):**

```bash
cd deu-branco-api
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

O backend estará disponível em `http://localhost:8080`.  
A documentação Swagger da API pode ser acessada em `http://localhost:8080/swagger-ui.html`.

**Terminal 2 — Frontend (React + Vite):**

```bash
cd frontend
npm install
npm run dev
```

Acesse no navegador:

```
http://localhost:5173
```

Você deverá ver a tela de login do **Deu Branco** no seu browser.

> O servidor de desenvolvimento do Vite já faz proxy das chamadas `/api` e `/ws` para `localhost:8080` automaticamente — não é necessária nenhuma configuração adicional de CORS.

---

### 3.4 Padrões de commit

Este projeto segue o padrão **Conventional Commits** com identificação do autor entre colchetes:

```
<tipo>: [Nome] <descrição curta no imperativo>
```

| Tipo       | Quando usar                                               |
|------------|-----------------------------------------------------------|
| `feat`     | Nova funcionalidade                                       |
| `fix`      | Correção de bug                                           |
| `refactor` | Mudança de código sem alterar comportamento               |
| `docs`     | Alterações em documentação                                |
| `style`    | Formatação, espaços — sem impacto em lógica               |
| `test`     | Adição ou modificação de testes                           |
| `chore`    | Ajustes de build, CI, dependências                        |

**Exemplos:**

```bash
feat: [Giovane] adicionar tela de histórico de partidas
fix: [João] corrigir cálculo de pontuação no pódio
docs: [Caio] atualizar README com instruções de Docker
```

### 3.5 Uso de branches

| Padrão de branch             | Finalidade                        |
|------------------------------|-----------------------------------|
| `feature/rf001-nome`         | Nova funcionalidade (por RF)      |
| `fix/nome-do-bug`            | Correção de bug                   |
| `hotfix/nome`                | Correção urgente em produção      |
| `refactor/nome`              | Refatoração sem nova feature      |
| `docs/nome`                  | Somente documentação              |

Cada branch deve originar um **Pull Request** referenciando a issue correspondente (`Closes #XX`) e ser mergeada em `main` após revisão.

---

## 4. Tecnologias

### Frontend

| Tecnologia | Versão | Finalidade |
|---|---|---|
| [React](https://react.dev/) | 19 | Biblioteca de interface |
| [Vite](https://vitejs.dev/) | 8 | Bundler e servidor de desenvolvimento |
| [TypeScript](https://www.typescriptlang.org/) | 6 | Tipagem estática |
| [Sass](https://sass-lang.com/) | 1.x | Estilização com design tokens |
| [React Router DOM](https://reactrouter.com/) | 7 | Roteamento client-side (SPA) |
| [Axios](https://axios-http.com/) | 1.x | Cliente HTTP com interceptores JWT |
| [@stomp/stompjs](https://stomp-js.github.io/) | 7 | WebSocket STOMP para partidas em tempo real |

### Backend

| Tecnologia | Versão | Finalidade |
|---|---|---|
| [Java](https://adoptium.net/) | 25 | Linguagem principal |
| [Spring Boot](https://spring.io/projects/spring-boot) | 4 | Framework web |
| [Spring Security + OAuth2 JWT](https://spring.io/projects/spring-security) | — | Autenticação e autorização |
| [Spring WebSocket (STOMP)](https://docs.spring.io/spring-framework/reference/web/websocket.html) | — | Comunicação em tempo real |
| [Spring Data JPA + Hibernate](https://spring.io/projects/spring-data-jpa) | — | Acesso ao banco de dados |
| [Flyway](https://flywaydb.org/) | — | Migrações de banco de dados |
| [PostgreSQL](https://www.postgresql.org/) | 16 | Banco de dados relacional |
| [Springdoc OpenAPI (Swagger)](https://springdoc.org/) | 3.x | Documentação da API |

### Infraestrutura

| Tecnologia | Finalidade |
|---|---|
| [Docker](https://www.docker.com/) + [Docker Compose](https://docs.docker.com/compose/) | Containerização e orquestração |
| [Nginx](https://nginx.org/) | Servidor web e proxy reverso (produção) |
| [Git](https://git-scm.com/) + [GitHub](https://github.com/) | Controle de versão e hospedagem do código |

---

## 5. Organização do Projeto

```
DeuBranco/
│
├── deu-branco-api/                  # Backend — Spring Boot
│   ├── src/main/java/deu_branco_api/
│   │   ├── config/                  # Configurações (Security, CORS, WebSocket)
│   │   ├── controller/              # Endpoints REST da API
│   │   ├── model/                   # Entidades JPA (Jogador, Partida, Questao...)
│   │   ├── repository/              # Interfaces Spring Data JPA
│   │   └── service/                 # Regras de negócio
│   ├── src/main/resources/
│   │   ├── application.properties   # Configuração da aplicação (env vars)
│   │   └── db/migration/            # Scripts SQL do Flyway (V1__, V2__...)
│   ├── Dockerfile                   # Build multi-stage do backend
│   └── pom.xml                      # Dependências Maven
│
├── frontend/                        # Frontend — React + Vite
│   ├── src/
│   │   ├── assets/                  # Imagens e ícones estáticos
│   │   ├── components/
│   │   │   ├── domain/              # Componentes específicos do domínio (QuizOption, Timer...)
│   │   │   ├── layout/              # AppShell (nav autenticado) e AuthShell (telas de login)
│   │   │   └── ui/                  # Primitivos reutilizáveis (Button, Input, Card, Logo...)
│   │   ├── contexts/                # AuthContext e MatchContext (estado global)
│   │   ├── hooks/                   # useAuth, useMatch, useMatchSocket (WebSocket)
│   │   ├── pages/
│   │   │   ├── admin/               # Telas administrativas (Gerir, Registrar, Editar questões)
│   │   │   ├── match/               # Telas de partida (Lobby, Jogo, Pódio, Histórico...)
│   │   │   ├── Home/                # Tela inicial do jogador autenticado
│   │   │   ├── Login/               # Tela de autenticação
│   │   │   └── Register/            # Tela de cadastro
│   │   ├── router/                  # Guards de rota (AuthGuard, AdminGuard)
│   │   ├── services/                # Clientes de API (authService, partidaService, questaoService...)
│   │   ├── styles/                  # Design tokens Sass (_tokens.scss, _reset.scss...)
│   │   └── types/                   # Tipagens TypeScript (partida.ts, questao.ts, auth.ts...)
│   ├── nginx.conf                   # Configuração do Nginx (proxy /api e /ws)
│   ├── Dockerfile                   # Build multi-stage do frontend
│   └── vite.config.ts               # Configuração Vite (proxy de dev + Sass tokens)
│
├── docs/                            # Documentação do projeto
│   ├── Requisitos/                  # ERS — Especificação de Requisitos de Software
│   └── design/                      # Padrão de design (cores, tipografia, componentes)
│
├── docker-compose.yml               # Orquestração: db + api + frontend
└── README.md                        # Este arquivo
```

---

## Equipe de Desenvolvimento

Projeto desenvolvido para fins acadêmicos na **Universidade Federal de Lavras (UFLA)**.

| Nome | GitHub |
|---|---|
| Giovane Felipe Godoi Oliveira | [@giogodoi](https://github.com/giogodoi) |
| João Vitor Reis Alvarenga | [@JoaoReiiis](https://github.com/JoaoReiiis) |
| Caio Teixeira Ladeira | — |
