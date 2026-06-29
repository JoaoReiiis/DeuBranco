# Casos de Teste de Validacao - Deu Branco

## Identificacao

| Campo | Valor                                                                |
| --- |----------------------------------------------------------------------|
| Projeto | Deu Branco                                                           |
| Tipo de teste | Teste de validacao / caixa preta / funcional                         |
| Ferramenta usada na automacao | Selenium WebDriver com Java e JUnit                                  |
| Arquivo dos scripts | `teste/src/test/java/deubranco/validacao/ValidacaoSeleniumTest.java` |
| Data | 28/06/2026                                                           |

## Casos de teste

| ID | Nome da funcionalidade | Acoes do usuario para chegar na tela desejada | Dados de entrada | Saida esperada | Print screen da saida ocorrida de fato | Resultado |
| --- | --- | --- | --- | --- | --- | --- |
| CTV-001 | Proteger area autenticada | 1. Abrir a aplicacao. 2. Garantir que nao existe usuario autenticado. 3. Acessar a rota inicial `/`. | Token de autenticacao ausente. | O sistema deve redirecionar o usuario para `/login` e exibir a tela de login. | `teste/evidencias/CTV-001-rota-protegida.png` | Aprovado |
| CTV-002 | Autenticar usuario | 1. Abrir `/login`. 2. Preencher e-mail e senha invalidos. 3. Acionar o botao `Login`. | E-mail: `usuario.invalido@deubranco.com`; Senha: `senhaerrada123`. | O sistema deve permanecer na tela de login e exibir a mensagem `E-mail ou senha invalidos. Verifique suas credenciais.` | `teste/evidencias/CTV-002-login-invalido.png` | Aprovado |
| CTV-003 | Cadastrar jogador | 1. Abrir `/register`. 2. Preencher nome e e-mail validos. 3. Preencher senha com menos caracteres que o minimo. 4. Acionar o botao `Criar Conta`. | Nome: `Aluno Teste`; E-mail: `aluno.teste.validacao@deubranco.com`; Senha: `123`. | O navegador deve bloquear o envio do formulario e manter o campo `senha` em estado invalido. | `teste/evidencias/CTV-003-senha-curta.png` | Aprovado |
| CTV-004 | Cadastrar jogador | 1. Abrir `/register`. 2. Preencher nome, e-mail unico e senha valida. 3. Acionar o botao `Criar Conta`. | Nome: `Aluno Validacao`; E-mail gerado automaticamente; Senha: `SenhaTeste123`. | O sistema deve criar a conta, autenticar o jogador e redirecionar para a tela inicial autenticada. | `teste/evidencias/CTV-004-cadastro-valido.png` | Aprovado |
| CTV-005 | Criar arena | 1. Cadastrar e autenticar um jogador. 2. Abrir `/match/configurar`. 3. Acionar `Iniciar Arena` sem selecionar disciplina. | Numero de questoes padrao: `10`; Tempo padrao: `30s`; Disciplinas: nenhuma. | O sistema deve impedir a criacao da arena e exibir `Selecione pelo menos uma disciplina.` | `teste/evidencias/CTV-005-arena-sem-disciplina.png` | Aprovado |
| CTV-006 | Entrar em sala por PIN | 1. Cadastrar e autenticar um jogador. 2. Abrir a tela inicial. 3. Informar um PIN inexistente. 4. Acionar `Join`. | PIN: `ZZZ999`. | O sistema deve impedir a entrada e exibir mensagem de PIN invalido ou partida nao encontrada. | `teste/evidencias/CTV-006-pin-invalido.png` | Aprovado |

## Observacoes

- Os testes foram definidos a partir do comportamento visivel da interface, sem acesso direto ao banco de dados.
- O teste CTV-002 tambem falha corretamente quando a API retorna erro de credenciais ou quando a autenticacao nao e concluida.
- O teste CTV-003 valida a regra de tamanho minimo da senha diretamente na interface, antes de enviar a requisicao.
- Os testes CTV-004, CTV-005 e CTV-006 validam fluxos autenticados, usando e-mails unicos gerados automaticamente.
- Os testes automatizados tambem salvam novamente os prints na pasta `teste/evidencias` quando executados.