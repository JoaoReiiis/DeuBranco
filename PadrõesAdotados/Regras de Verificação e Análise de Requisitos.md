# Regras de Verificação e Análise de Requisitos

**Projeto:** DeuBranco — plataforma gamificada de preparação para ENEM e vestibulares com mecânicas multiplayer competitivas em tempo real

**Pasta:** Padrões Adotados

**Sprint de origem:** Sprint 0 — Tarefa #5 (Definir critérios para verificação/análise de requisitos)

**Base bibliográfica:** MAGELA, Rogério. *Engenharia de Software Aplicada: Fundamentos.* Rio de Janeiro: Alta Books, 2006. Capítulo 1 — Análise de Requisitos, Seção 7.2 (Especificação de Requisitos) e Seção 7.3 (Análise de Requisitos).

---

## 1. Objetivo

Este documento estabelece os padrões obrigatórios que toda a equipe deve seguir ao escrever, nomear, organizar e revisar os requisitos do projeto DeuBranco. As regras de escrita (Seção 3) e os critérios de verificação (Seção 4) adotam como padrão as diretrizes estabelecidas nas Seções 7.2 e 7.3 do Capítulo 1 de MAGELA (2006). Quando essas diretrizes são apresentadas de forma qualitativa, este documento estabelece critérios quantitativos complementares para garantir aplicação uniforme, objetiva e verificável durante todo o projeto.

---

## 2. Nomenclatura e Identificação dos Requisitos

Como padrão de identificação dos requisitos, serão utilizados os identificadores **RF** (Requisito Funcional) e **RNF** (Requisito Não Funcional), organizados hierarquicamente conforme os princípios de estruturação de requisitos descritos nas Seções 7.2 e 7.3 de MAGELA (2006).

| Tipo de requisito       | Prefixo | Formato do identificador                             | Exemplo                 |
| ----------------------- | ------- | ---------------------------------------------------- | ----------------------- |
| Requisito Funcional     | RF      | RF + 3 dígitos sequenciais; filhos recebem sufixo .N | RF001, RF010.1, RF010.2 |
| Requisito Não Funcional | RNF     | RNF + 3 dígitos sequenciais                          | RNF001, RNF002          |

**Regra de imutabilidade:** um identificador, uma vez atribuído, nunca é reaproveitado, mesmo que o requisito seja removido do escopo. Alteração relevante de conteúdo gera sufixo de versão (ex.: RF001-v2).

**Formato do nome do requisito:** deve começar por um verbo no infinitivo e conter entre 2 e 8 palavras (ex.: "Cadastrar Questão", "Iniciar Partida Multiplayer").

---

## 3. Regras de Especificação de Requisitos (base: Seção 7.2 do Capítulo 1)

| #      | Referência bibliográfica                                                             | Padrão adotado                                                                                                                        | Critério objetivo de verificação                                                                                                                                                           |
| ------ | ------------------------------------------------------------------------------------ |---------------------------------------------------------------------------------------------------------------------------------------| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **E1** | 7.2.1 — Defina somente um requisito por vez                                          | Cada requisito deve representar uma única regra de negócio. Exigências distintas devem ser especificadas em requisitos independentes. | A frase do requisito contém no máximo 1 verbo modal "DEVE" e nenhuma conjunção aditiva ("e"/"ou") unindo duas regras de negócio distintas. Caso contrário, reprovado e deve ser desmembrado. |
| **E2** | 7.2.2 — Evite certas palavras                                                        | Não serão utilizados termos que introduzam ambiguidade lógica ou imprecisão de interpretação, conforme definido no Anexo I.           | Zero ocorrências dos termos do Anexo I no texto do requisito, exceto quando seguidos de qualificação numérica explícita.                                                                   |
| **E3** | 7.2.3 — Evite frases grandes                                                         | Os requisitos deverão ser escritos de forma concisa, favorecendo clareza e facilidade de verificação.                                 | A frase do campo "Nome do Requisito" tem no máximo 8 palavras (Seção 2) e a frase de cada passo do fluxo principal/alternativo tem no máximo **20 palavras**.                              |
| **E4** | 7.2.4 — Utilize um vocabulário fechado                                               | Todo termo de negócio utilizado nos requisitos deverá possuir definição única e oficial registrada no glossário do projeto.           | Todo termo de negócio citado em um requisito (ex.: "Partida", "Sala", "Ranking") consta no arquivo glossario.md do repositório. Termo ausente do glossário = reprovado.                    |
| **E5** | 7.2.5 — Mantenha uma estrutura hierárquica dos requisitos                            | Os requisitos deverão ser organizados hierarquicamente caso um requisito esteja encapsulado a outro.                                  | Todo requisito-filho referencia explicitamente o ID do seu requisito-pai no nome (ex.: RF010.1). Requisito sem pai é, por definição, um requisito macro e deve corresponder a 1 caso de uso. |
| **E6** | 7.2.6 — Para cada novo requisito, atualize seus atributos                            | Todo requisito deverá possuir seus atributos devidamente preenchidos e atualizados ao longo do ciclo de vida.                         | Os 7 atributos citados estão preenchidos na tabela de cada requisito no documento de requisitos. Qualquer campo vazio = reprovado.                                                         |
| **E7** | 7.2.7 — Agrupe corretamente os requisitos                                            | Os requisitos deverão ser organizados conforme sua entidade ou processo de negócio correspondente.                                    | Cada requisito está posicionado na seção (tarja cinza) correspondente à sua entidade de negócio no documento (ex.: seção "Questões", seção "Partida", seção "Ranking").                    |
| **E8** | 7.2.8 — Transforme os requisitos do usuário em requisitos do software                | Os requisitos deverão manter vínculo explícito com os artefatos de análise e projeto derivados.                                       | Todo requisito macro corresponde a exatamente 1 caso de uso no diagrama UML. Todo requisito filho corresponde a passos identificáveis no diagrama de sequência daquele caso de uso.        |

---

## 4. Critérios de Análise/Verificação de Requisitos (base: Seção 7.3 do Capítulo 1)

| #      | Referência bibliográfica          | Padrão adotado                                                                                                                                                         | Critério objetivo de verificação                                                                                                                                                                             |
|--------| --------------------------------- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **A1** | 7.3.2 — Completa                  | Todas as necessidades identificadas deverão estar representadas na especificação de requisitos.                                                                        | Toda necessidade listada nas notas de elicitação da equipe corresponde a, no mínimo, 1 requisito no documento. Necessidade sem requisito correspondente = reprovado.                                         |
| **A2** | 7.3.3 — Sem ambiguidade           | Cada requisito deverá admitir apenas uma interpretação possível. Quando necessário, condições múltiplas deverão ser explicitamente delimitadas.                        | A frase do requisito admite apenas 1 leitura gramatical. Quando houver mais de uma condição na mesma frase, cada condição deve ser isolada entre colchetes [ ], conforme técnica descrita por MAGELA (2006). |
| **A3** | 7.3.4 — Verificável               | Todo requisito deverá ser passível de validação por meio de procedimento objetivo e reproduzível.                                                                      | Existe um caso de teste objetivo associável ao requisito (entrada/saída definidas — Regra do documento de requisitos). Toda RNF possui métrica numérica, unidade e limite.                                   |
| **A4** | 7.3.5 — Consistente               | Não deverão existir requisitos contraditórios ou definições conflitantes para um mesmo conceito de negócio.                                                            | Nenhum par de requisitos produz resultado contraditório para a mesma entrada. Cada termo de negócio possui exatamente 1 definição no glossário (E4).                                                         |
| **A5** | 7.3.6 — Compreensível             | A especificação deverá manter equilíbrio entre formalismo técnico e compreensão pelos envolvidos no projeto.                                                           | Um integrante da equipe que não escreveu o requisito consegue explicá-lo corretamente, com suas próprias palavras, em até 2 minutos, sem consultar o autor original.                                         |
| **A6** | 7.3.7 — Modificável               | Os requisitos deverão ser escritos de forma concisa e estruturada para facilitar manutenção e evolução.                                                                | A frase do requisito respeita o limite de 20 palavras por passo de fluxo (E3). Requisito que exceder esse limite deve ser revisado antes da entrega.                                                         |
| **A7** | 7.3.10 — Independência de projeto | Requisitos funcionais não devem depender de tecnologias específicas, salvo quando a tecnologia constituir requisito arquitetural explicitamente aprovado pela equipe.  | A frase do requisito não contém nome de tecnologia, framework, linguagem de programação, nome de tabela do banco de dados ou nome de classe do código que não foi previamente acordado pela equipe.          |
| **A8** | 7.3.11 — Organizada               | A especificação deverá permanecer organizada simultaneamente por domínio de negócio e por hierarquia de requisitos.                                                    | O requisito está corretamente posicionado na seção de sua entidade de negócio (E7) e no nível hierárquico correto da árvore de requisitos (E5).                                                              |

---

## 5. Anexo I — Lista de Termos a Evitar (base: Seção 7.2.2 do Capítulo 1)

Os termos a seguir não deverão ser utilizados na especificação de requisitos, por introduzirem ambiguidade lógica ou imprecisão de interpretação, conforme a Seção 7.2.2 de MAGELA (2006):

`E`, `OU`, `SOMENTE SE`, `EXCETO`, `SE NECESSÁRIO`, `MAS`, `CONTUDO`, `ENTRETANTO`, `USUALMENTE`, `GERALMENTE`, `FREQUENTEMENTE`, `TIPICAMENTE`, `AMIGÁVEL`, `VERSÁTIL`, `FLEXÍVEL`, `APROXIMADAMENTE`, `TÃO LOGO QUANTO POSSÍVEL`, `TALVEZ`, `PROVAVELMENTE`.

**Tratamento obrigatório quando esses termos aparecerem:**

| Categoria do termo                                                                                                                                          | Ação obrigatória                                                                                                                            |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| Conectores lógicos (E, OU, SOMENTE SE, EXCETO, SE NECESSÁRIO, MAS, CONTUDO, ENTRETANTO)                                                                     | Separar a frase em dois requisitos distintos, ou isolar cada condição entre colchetes `[ ]`, conforme a técnica descrita por MAGELA (2006). |
| Advérbios de frequência/aproximação (USUALMENTE, GERALMENTE, FREQUENTEMENTE, TIPICAMENTE, APROXIMADAMENTE, TÃO LOGO QUANTO POSSÍVEL, TALVEZ, PROVAVELMENTE) | Substituir por valor numérico exato e unidade de medida.                                                                                    |
| Adjetivos vagos (AMIGÁVEL, VERSÁTIL, FLEXÍVEL)                                                                                                              | Substituir por uma referência objetiva e verificável, como padrões de interface, lista de plataformas suportadas ou métricas mensuráveis.   |

---

## 6. Checklist Consolidado de Verificação

Para cada requisito do documento, deverão ser aplicadas as 20 verificações abaixo. Qualquer resposta "não" implica retrabalho obrigatório antes da entrega. Este checklist será reaplicado durante as atividades de revisão e validação do projeto.

| Bloco                | Itens a verificar                                                                                                                                                                                                                                                                                                                         |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Escrita do requisito | E1 (um requisito por vez) · E2 (sem termos do Anexo I) · E3 (≤ 20 palavras por passo) · E4 (vocabulário no glossário) · E5 (hierarquia correta) · E6 (7 atributos preenchidos) · E7 (agrupado por entidade) · E8 (macro→caso de uso / filho→sequência) · E9 (RNF de Corretude e Confiabilidade definidos)                                 |
| Análise/verificação  | A1 (fonte real) · A2 (necessidade não esquecida) · A3 (uma única interpretação) · A4 (testável/mensurável) · A5 (sem contradição) · A6 (explicável em 2 min) · A7 (conciso) · A8 (rastreável para trás) · A9 (rastreável para frente: ator + interface + Kanban) · A10 (sem solução técnica embutida) · A11 (organização nas 2 dimensões) |

---

## 7. Referência Bibliográfica

MAGELA, Rogério. **Engenharia de Software Aplicada: Fundamentos.** Rio de Janeiro: Alta Books, 2006. Capítulo 1 — Análise de Requisitos: Seção 7.2 "Especificação de Requisitos" (p. 18–19) e Seção 7.3 "Análise de Requisitos" (p. 20–24).
