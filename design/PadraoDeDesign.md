---
name: DeuBranco Institucional
colors:
  surface: '#fbf9fb'
  surface-dim: '#dbd9dc'
  surface-bright: '#fbf9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f3f6'
  surface-container: '#efedf0'
  surface-container-high: '#e9e7ea'
  surface-container-highest: '#e3e2e5'
  on-surface: '#1b1c1e'
  on-surface-variant: '#434656'
  inverse-surface: '#303033'
  inverse-on-surface: '#f2f0f3'
  outline: '#737688'
  outline-variant: '#c3c5d9'
  surface-tint: '#004ced'
  primary: '#003ec7'
  on-primary: '#ffffff'
  primary-container: '#0052ff'
  on-primary-container: '#dfe3ff'
  inverse-primary: '#b7c4ff'
  secondary: '#585e6b'
  on-secondary: '#ffffff'
  secondary-container: '#dde2f2'
  on-secondary-container: '#5e6472'
  tertiary: '#4c4e4f'
  on-tertiary: '#ffffff'
  tertiary-container: '#656666'
  on-tertiary-container: '#e4e4e4'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dde1ff'
  primary-fixed-dim: '#b7c4ff'
  on-primary-fixed: '#001452'
  on-primary-fixed-variant: '#0038b6'
  secondary-fixed: '#dde2f2'
  secondary-fixed-dim: '#c1c6d5'
  on-secondary-fixed: '#161c27'
  on-secondary-fixed-variant: '#414753'
  tertiary-fixed: '#e2e2e2'
  tertiary-fixed-dim: '#c6c6c7'
  on-tertiary-fixed: '#1a1c1c'
  on-tertiary-fixed-variant: '#454747'
  background: '#fbf9fb'
  on-background: '#1b1c1e'
  surface-variant: '#e3e2e5'
  primary-active: '#003ecc'
  primary-disabled: '#a8b8cc'
  hairline: '#dee1e6'
  surface-soft: '#f7f7f7'
  quiz-a: '#4F46E5'
  quiz-b: '#7C3AED'
  quiz-c: '#DB2777'
  quiz-d: '#EA580C'
  quiz-e: '#059669'
  semantic-up: '#05b169'
  semantic-down: '#cf202f'
typography:
  display-mega:
    fontFamily: Inter
    fontSize: 80px
    fontWeight: '400'
    lineHeight: 80px
    letterSpacing: -2px
  display-lg:
    fontFamily: Inter
    fontSize: 52px
    fontWeight: '400'
    lineHeight: 52px
    letterSpacing: -1.3px
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '400'
    lineHeight: 40px
    letterSpacing: -0.5px
  headline-md:
    fontFamily: Inter
    fontSize: 44px
    fontWeight: '400'
    lineHeight: 48px
    letterSpacing: -1px
  title-md:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-strong:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '700'
    lineHeight: 24px
  label-caps:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  button:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 18px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  xxs: 4px
  xs: 8px
  sm: 12px
  base: 16px
  md: 20px
  lg: 24px
  xl: 32px
  xxl: 48px
  section: 96px
---

## Boas Vindas

  O documento abaixo descreve, detalhadamente, todos os padrões do design elaborado no figma para facilitar o futuro desenvolvimento do frontend da solução.

  Abaixo, descrevi todos os porquês da escolha de cada detalhe do design da plataforma.

  Além disso, deixo disposto os padrões para configuração dos componentes.


## Brand & Style

Este design system estabelece uma estética **Corporativo Moderno** voltada para um ambiente de estudo competitivo. Ele equilibra a natureza de alta pressão da “competição multiplayer” com o rigor institucional, silenciosamente confiante, de uma plataforma financeira de alto nível. O objetivo é fazer com que o desempenho acadêmico pareça tão prestigioso e preciso quanto a gestão de ativos.

A linguagem visual é fundamentada em **Editorial Minimalism**:
- **White Canvas Strategy:** use fundos brancos puros como estado padrão para promover clareza e foco durante sessões intensas de estudo.
- **Quiet Confidence:** os títulos usam pesos leves (400), mesmo em escalas grandes, para sinalizar autoridade sem excesso de ênfase.
- **Structured Gamification:** diferente de apps educacionais tradicionalmente “barulhentos”, este sistema trata a competição como uma atividade profissional. Cores vibrantes ficam restritas a papéis funcionais secundários (como opções de quiz), enquanto a marca permanece ancorada em uma paleta disciplinada de azul e cinza.
- **Layered Precision:** a profundidade é construída por blocos tonais e faixas sutis de elevação, em vez de sombras agressivas, preservando uma hierarquia plana, porém sofisticada.

## Colors

A paleta é dominada por uma relação de alto contraste entre uma base branca pura e o “Coinbase Blue”.

- **Primary Action:** `#0052ff` deve ser usado exclusivamente para CTAs primários, indicadores de progresso e estados ativos. Representa a identidade da “Arena”.
- **Institutional Neutrals:** `Ink` (`#0a0b0d`) entrega alto contraste para títulos, enquanto `Body` (`#5b616e`) garante conforto de leitura em materiais extensos.
- **Quiz Palette:** para interações de múltipla escolha, cinco tons distintos, acessíveis e vibrantes são introduzidos. Eles devem ser usados como acentos secundários (por exemplo, bordas das opções ou pequenos indicadores de letra), para que a interface não perca sua base institucional.
- **Elevation Bands:** use `surface-soft` (`#f7f7f7`) em seções horizontais de largura total para quebrar páginas longas e agrupar conteúdos relacionados.

## Typography

O sistema utiliza a famosa e conhecida família **Inter** para reproduzir a estética limpa e funcional do estilo de referência.

- **Editorial Calm:** a regra tipográfica mais importante é o uso de `fontWeight: 400` em todos os estilos grandes de display (de Mega até SM). Isso cria uma sensação sofisticada e editorial que diferencia o app de jogos educacionais mais “inflados”.
- **Mobile Adaptation:** os estilos de display reduzem de forma agressiva no mobile. `Display LG` (52px) deve refluír para 36px em dispositivos móveis, garantindo legibilidade sem quebras excessivas de palavra.
- **Functional Precise:** use semibold (600) em títulos de componentes e labels de botões, assegurando affordance clara em contraste com títulos de peso leve.

## Layout & Spacing

Este design system emprega um modelo de grid **fluid-to-fixed** otimizado para consumo mobile-first.

- **Grid System:** um grid de 12 colunas no desktop (max-width 1280px) e um grid de 4 colunas no mobile.
- **Margins & Gutters:** as margens no mobile são fixadas em `base` (16px). Os gutters permanecem consistentes em `base` (16px) para manter uma sensação compacta e técnica.
- **Vertical Pacing:** use a unidade `section` (96px) como padding vertical entre grandes blocos de conteúdo no desktop, reduzindo para `xxl` (48px) no mobile. Esse “espaço em branco generoso” é essencial para a personalidade silenciosamente confiante da marca.
- **Internal Spacing:** cards e containers usam padding interno `xl` (32px) para garantir que o conteúdo pareça premium e sem aperto.

## Elevation & Depth

A hierarquia é estabelecida por **Tonal Layers** em vez de elevação tradicional.

- **The Layering Order:** Base Canvas (`#FFFFFF`) < Section Bands (`#F7F7F7`) < Cards (`#FFFFFF`).
- **Shadows:** use sombras com parcimônia. Uma única sombra “Soft Float” `0 4px 12px rgba(0, 0, 0, 0.04)` é permitida apenas em estados hover de cards interativos ou dropdowns ativos.
- **Borders:** use bordas de 1px em `hairline` (`#dee1e6`) para definir cards sobre fundos brancos. Isso fornece uma aparência precisa e “engenheirada”.
- **Dark Mode Context:** em seções editoriais de “Hero” (usando `#0a0b0d`), a profundidade é criada posicionando cards com superfície `#16181c` sobre o fundo mais escuro.

## Shapes

A linguagem de formas é uma característica central deste design system, usando arredondamento extremo para suavizar a estética institucional.

- **Buttons & Search:** todos os botões, campos de busca e badges devem usar a forma `pill` (100px). Isso torna os elementos interativos imediatamente reconhecíveis.
- **Cards:** containers principais de conteúdo e cards de quiz usam `rounded-xl` (24px).
- **Inputs:** campos de formulário padrão usam `rounded-md` (12px) para oferecer uma aparência mais estruturada do que a dos botões, ajudando o usuário a distinguir entre “inserir” e “enviar”.
- **Avatars:** perfis de usuário e ícones de ativos são sempre `rounded-full` (circulares).

## Components

### Buttons
- **Primary:** formato pill, preenchimento `#0052ff`, texto branco. Labels em negrito e centralizadas.
- **Secondary:** formato pill, preenchimento `#eef0f3`, texto `#0a0b0d`. Sem borda.
- **Quiz Options:** cards com 24px de arredondamento, borda hairline de 1px. Ao selecionar, a borda aumenta para 2px e muda para a cor específica da Quiz Palette ou para o Primary Blue.

### Cards
- **Study Card:** fundo branco, raio de 24px, borda hairline de 1px. Padding interno de 32px.
- **Leaderboard Row:** fundo cinza suave (`#f7f7f7`), raio de 8px, com números monoespaçados para ranking, reforçando precisão técnica.

### Input Fields
- **Text Inputs:** raio de 12px, borda hairline. Em foco, a borda passa a ser `2px #0052ff`.

### Chips & Badges
- **Status Tags:** formato pill, texto pequeno (12px), peso bold. Use tingimentos sutis de fundo com as cores semânticas correspondentes (por exemplo, verde bem claro para “Completed”).

### Progress Bars
- **Arena Progress:** altura de 8px, trilha em formato pill. O preenchimento usa primary blue, enquanto a trilha usa o cinza soft-hairline (`#eef0f3`).
