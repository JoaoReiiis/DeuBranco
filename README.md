<div align="center">
  <img width="250" alt="Preview Deu Branco" src="https://github.com/user-attachments/assets/a0f34476-5ce3-49b5-aa7d-580fc7318300" />
</div>

# Deu Branco

<p align="left">
  <img src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" alt="React" />
  <img src="https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white" alt="Vite" />
  <img src="https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript" />
  <img src="https://img.shields.io/badge/SASS-hotpink.svg?style=for-the-badge&logo=SASS&logoColor=white" alt="Sass" />
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
</p>

**Deu Branco** é uma plataforma educacional com estética corporativa voltada para um ambiente de estudo competitivo.  
O objetivo do sistema é permitir que estudantes gerenciem seu desempenho, resolvam questões e participem de competições de conhecimento, equilibrando a alta pressão com um rigor institucional e foco.

---

## Descrição do Produto

A aplicação consiste em um **sistema web (plataforma de estudos)** focado em gamificação estruturada.  
O sistema contará com funcionalidades como:
- Catálogo de questões abrangendo diversas disciplinas e instituições;  
- Sistema de autenticação de jogadores via token JWT;  
- Painel para resolução de questões com opções formatadas (A a E);  
- Gamificação estruturada que trata a competição acadêmica como uma atividade profissional;  
- Painel administrativo para controle e cadastro do banco de questões.

---

## Tecnologias Utilizadas

### Frontend
- **Linguagem:** TypeScript + HTML + Sass
- **Framework/Biblioteca:** [React](https://react.dev/) + Vite  
- **Gerenciador de pacotes:** npm  
- **Estilização:** Sass / Bootstrap customizado

### Backend
- **Linguagem:** Java
- **Framework do servidor:** [Spring Boot](https://spring.io/projects/spring-boot)  
- **Banco de Dados:** [PostgreSQL](https://www.postgresql.org/)  
- **Segurança:** Spring Security (OAuth2 / JWT)

### Ambiente de Desenvolvimento
- **Servidor local (Backend):** Apache Tomcat (embutido no Spring Boot)
- **Servidor local (Frontend):** Vite Development Server
- **Banco de dados local:** PostgreSQL   
- **Controle de versão:** Git + GitHub

---

## Padrões de Design (Frontend)

O frontend foi planejado seguindo o documento de padrões estabelecido para garantir uma estética de **Corporativo Moderno**.
- **Linguagem Visual:** Fundamentada no *Editorial Minimalism*, utilizando a estratégia de "White Canvas" para promover clareza durante as sessões de estudo.
- **Cores:** A paleta é dominada por um alto contraste entre a base branca e a cor principal de ação, o `#0052ff` (Coinbase Blue). A paleta de Quiz conta com cinco tons vibrantes e distintos para as alternativas.
- **Tipografia:** Uso da família **Inter**, garantindo precisão funcional e calma editorial com pesos leves (400) em textos grandes.
- **Layout e Formas:** Abordagem *mobile-first* fluida com componentes de cantos arredondados (ex: *Study Cards* com 24px de raio e *Inputs* com 12px). A profundidade é dada por camadas tonais (*Tonal Layers*), evitando o excesso de sombras.

---

## Regras e Padrões de Uso do Git

### 1. **Regras de Commit:**
   - Todos os commits devem seguir o padrão **Conventional Commits**.
   - Nome do integrante do grupo entre [], por exemplo, `[Giovane] ...`.
   - Use um formato claro e descritivo para a mensagem de commit:
     - `feat:` para novos recursos;
     - `fix:` para correções de bugs;
     - `docs:` para mudanças na documentação;
     - `style:` para ajustes de estilo (não afetam a lógica do código);
     - `refactor:` para mudanças no código que não alteram a funcionalidade;
     - `test:` para inclusão ou modificação de testes;
     - `chore:` para ajustes de infraestrutura ou dependências.
   - Exemplo de mensagem de commit:
     ```bash
     feat: [Giovane] adiciona autenticação de usuário com JWT
     ```

### 2. **Uso de Branches:**
   - Use branches para todas as novas funcionalidades ou correções de bugs.
   - Nomeie suas branches de forma clara e objetiva:
     - `feature/nome-da-feature` para novas funcionalidades;
     - `bugfix/nome-do-bug` para correções de bugs;
     - `hotfix/nome-do-hotfix` para correções urgentes em produção.
   - Exemplo de criação de branch:
     ```bash
     git checkout -b feature/adicionar-paginacao-questoes
     ```

### 3. **Estrutura de Pastas:**
   - Organize o código de forma modular:
     - `frontend/` para arquivos relacionados ao frontend (React, Vite, Sass).
     - `backend/` para arquivos relacionados ao backend (Spring Boot, Controllers, Services).
     - `docs/` para documentação estrutural e de requisitos.
   - Mantenha a documentação atualizada e estruturada dentro da pasta `docs/`.

---

## Projeto: Especificação de Requisitos (ERS) - Deu Branco E-learning

Este repositório funciona como o centralizador de todo o projeto **Deu Branco**, incluindo o documento formal de **Especificação de Requisitos de Software (ERS)** para o sistema educacional e gamificado. O arquivo principal do projeto está incluído aqui, juntamente com outros materiais e recursos relacionados ao desenvolvimento do sistema.

## 1. Objetivo do Documento

O objetivo deste documento é definir e catalogar formalmente todos os requisitos funcionais, não funcionais e regras de negócio necessárias para guiar o desenvolvimento do software **Deu Branco**. Ele serve como a "fonte da verdade" para a equipe de desenvolvimento e stakeholders, garantindo que o produto final atenda a todos os critérios estabelecidos.

## 2. Escopo do Sistema (Deu Branco)

O sistema educacional **Deu Branco** inclui as seguintes funcionalidades principais:

* **Gerenciamento de Contas:**
  Cadastro, login e autenticação JWT para Jogadores e Administradores.
* **Catálogo de Questões:**
  Gerenciamento de banco de perguntas, incluindo enunciados, alternativas de A a E, e controle de disciplinas e instituições.
* **Jornada de Estudo:**
  Acesso rápido e filtros para visualização e resolução de questões, utilizando design interativo para as respostas.
* **Painel Administrativo:**
  Interface dedicada a usuários com *Role* de ADMIN para editar regras de negócio e realizar manutenção da base de dados.

## 3. Estrutura do Documento ERS

O **ERS** foi estruturado para atender às diretrizes e boas práticas de Engenharia de Software, garantindo a clareza e rastreabilidade dos requisitos. A seguir, a organização detalhada do documento:

1. **Requisitos Funcionais (RFs)**
2. **Requisitos Não-Funcionais (RNFs)**
3. **Regras de Negócio (RBRs)**
4. **Padrões de Verificação**
5. **Rastreabilidade**

---

# Localização no Projeto

No seu projeto, a documentação do **ERS** está organizada da seguinte forma:

- **Pasta `docs/`**: Contém os documentos principais do projeto.
  - **Documento `Requisitos.md`**: Onde está a versão completa do **ERS**.
  - **Pasta `Documento de Requisitos/`**: Onde estão os documentos e diagramas finais adotados (como UML e Pacotes).
  - **Pasta `Padrões Adotados/`**: Onde estão os guias de design de interface e regras de codificação e Git do projeto.

---

# Configuração e Execução do Projeto

Siga os passos abaixo para configurar o ambiente de desenvolvimento local.

## 1. Clonar o Repositório

Faça o clone do projeto e entre na pasta raiz:

```bash
git clone [https://github.com/JoaoReiiis/DeuBranco.git](https://github.com/JoaoReiiis/DeuBranco.git)
cd DeuBranco

```

## 2. Configurar o Banco de Dados

1. Certifique-se de ter o **PostgreSQL** instalado e rodando.
2. No diretório `deu-branco-api/src/main/resources`, edite o arquivo `application.properties` (ou configure variáveis de ambiente).
3. Adicione as credenciais de conexão do seu banco local (URL, Username, Password). O Flyway aplicará as *migrations* automaticamente na primeira execução.

## 3. Instalação de Dependências e Execução

Recomenda-se abrir dois terminais separados para rodar os dois ambientes simultaneamente.

### Terminal 1: Backend (Spring Boot)

Navegue até a pasta da API, instale as dependências com o Maven Wrapper e inicie o servidor:

```bash
cd deu-branco-api
./mvnw clean install
./mvnw spring-boot:run

```

### Terminal 2: Frontend (React)

Navegue até a pasta do frontend, instale os pacotes e inicie o servidor de desenvolvimento:

```bash
cd frontend
npm install
npm run dev

```

---

Agora, o sistema estará rodando com o frontend acessível através da porta padrão do Vite (geralmente `http://localhost:5173`) e o backend na porta configurada no Spring Boot (geralmente `http://localhost:8080`). A documentação da API (Swagger) pode ser acessada em `/swagger-ui.html`.

---

## Equipe de Desenvolvimento

Projeto desenvolvido para fins acadêmicos, com foco na aplicação prática de tecnologias web modernas.

**Pelos Alunos:** 

Giovane Felipe Godoi Oliveira

João Vitor Reis Alvarenga

Caio Teixeira Ladeira

---
