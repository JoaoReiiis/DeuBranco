## Boas-vindas

O documento complementar abaixo descreve, em detalhes, todos os padrões do design elaborado no Figma para facilitar o futuro desenvolvimento do frontend da solução.

A seguir, estão documentados os motivos por trás das escolhas de design da plataforma.

Além disso, também estão definidos os padrões de configuração dos componentes.

<img width="1431" height="793" alt="Preview do design" src="https://github.com/user-attachments/assets/cd4a70eb-33dd-47d5-828e-d0448bc8f586" />

## Configuração base

| Propriedade | Valor |
| --- | --- |
| Name | DeuBranco Institucional |

## Colors

| Token | Valor |
| --- | --- |
| surface | `#fbf9fb` |
| surface-dim | `#dbd9dc` |
| surface-bright | `#fbf9fb` |
| surface-container-lowest | `#ffffff` |
| surface-container-low | `#f5f3f6` |
| surface-container | `#efedf0` |
| surface-container-high | `#e9e7ea` |
| surface-container-highest | `#e3e2e5` |
| on-surface | `#1b1c1e` |
| on-surface-variant | `#434656` |
| inverse-surface | `#303033` |
| inverse-on-surface | `#f2f0f3` |
| outline | `#737688` |
| outline-variant | `#c3c5d9` |
| surface-tint | `#004ced` |
| primary | `#003ec7` |
| on-primary | `#ffffff` |
| primary-container | `#0052ff` |
| on-primary-container | `#dfe3ff` |
| inverse-primary | `#b7c4ff` |
| secondary | `#585e6b` |
| on-secondary | `#ffffff` |
| secondary-container | `#dde2f2` |
| on-secondary-container | `#5e6472` |
| tertiary | `#4c4e4f` |
| on-tertiary | `#ffffff` |
| tertiary-container | `#656666` |
| on-tertiary-container | `#e4e4e4` |
| error | `#ba1a1a` |
| on-error | `#ffffff` |
| error-container | `#ffdad6` |
| on-error-container | `#93000a` |
| primary-fixed | `#dde1ff` |
| primary-fixed-dim | `#b7c4ff` |
| on-primary-fixed | `#001452` |
| on-primary-fixed-variant | `#0038b6` |
| secondary-fixed | `#dde2f2` |
| secondary-fixed-dim | `#c1c6d5` |
| on-secondary-fixed | `#161c27` |
| on-secondary-fixed-variant | `#414753` |
| tertiary-fixed | `#e2e2e2` |
| tertiary-fixed-dim | `#c6c6c7` |
| on-tertiary-fixed | `#1a1c1c` |
| on-tertiary-fixed-variant | `#454747` |
| background | `#fbf9fb` |
| on-background | `#1b1c1e` |
| surface-variant | `#e3e2e5` |
| primary-active | `#003ecc` |
| primary-disabled | `#a8b8cc` |
| hairline | `#dee1e6` |
| surface-soft | `#f7f7f7` |
| quiz-a | `#4F46E5` |
| quiz-b | `#7C3AED` |
| quiz-c | `#DB2777` |
| quiz-d | `#EA580C` |
| quiz-e | `#059669` |
| semantic-up | `#05b169` |
| semantic-down | `#cf202f` |

## Typography

| Estilo | fontFamily | fontSize | fontWeight | lineHeight | letterSpacing |
| --- | --- | --- | --- | --- | --- |
| display-mega | Inter | 80px | `400` | 80px | -2px |
| display-lg | Inter | 52px | `400` | 52px | -1.3px |
| display-lg-mobile | Inter | 36px | `400` | 40px | -0.5px |
| headline-md | Inter | 44px | `400` | 48px | -1px |
| title-md | Inter | 18px | `600` | 24px | — |
| body-md | Inter | 16px | `400` | 24px | — |
| body-strong | Inter | 16px | `700` | 24px | — |
| label-caps | Inter | 14px | `600` | 20px | — |
| button | Inter | 16px | `600` | 18px | — |

## Rounded

| Token | Valor |
| --- | --- |
| sm | `0.25rem` |
| DEFAULT | `0.5rem` |
| md | `0.75rem` |
| lg | `1rem` |
| xl | `1.5rem` |
| full | `9999px` |

## Spacing

| Token | Valor |
| --- | --- |
| xxs | `4px` |
| xs | `8px` |
| sm | `12px` |
| base | `16px` |
| md | `20px` |
| lg | `24px` |
| xl | `32px` |
| xxl | `48px` |
| section | `96px` |

## Brand & Style

Este design system estabelece uma estética **Corporativo Moderno** voltada para um ambiente de estudo competitivo. Ele equilibra a natureza de alta pressão da competição multiplayer com o rigor institucional, silenciosamente confiante, de uma plataforma financeira de alto nível. O objetivo é fazer com que o desempenho acadêmico pareça tão prestigioso e preciso quanto a gestão de ativos.

A linguagem visual é fundamentada em **Editorial Minimalism**:
- **White Canvas Strategy:** use fundos brancos puros como estado padrão para promover clareza e foco durante sessões intensas de estudo.
- **Quiet Confidence:** os títulos usam pesos leves (400), mesmo em escalas grandes, para sinalizar autoridade sem excesso de ênfase.
- **Structured Gamification:** diferente de apps educacionais tradicionalmente barulhentos, este sistema trata a competição como uma atividade profissional.
- **Layered Precision:** a profundidade é construída por blocos tonais e faixas sutis de elevação, em vez de sombras agressivas.

## Colors

A paleta é dominada por uma relação de alto contraste entre uma base branca pura e o Coinbase Blue.

- **Primary Action:** `#0052ff` deve ser usado exclusivamente para CTAs primários, indicadores de progresso e estados ativos.
- **Institutional Neutrals:** `Ink` (`#0a0b0d`) entrega alto contraste para títulos, enquanto `Body` (`#5b616e`) garante conforto de leitura em materiais extensos.
- **Quiz Palette:** cinco tons distintos, acessíveis e vibrantes são usados como acentos secundários.
- **Elevation Bands:** use `surface-soft` (`#f7f7f7`) para separar visualmente seções longas.

## Typography

O sistema utiliza a família **Inter** para reproduzir uma estética limpa e funcional.

- **Editorial Calm:** use `fontWeight: 400` em estilos grandes de display.
- **Mobile Adaptation:** `Display LG` (52px) deve refluír para 36px no mobile.
- **Functional Precise:** use semibold (600) em títulos de componentes e labels de botões.

## Layout & Spacing

Este design system emprega um grid **fluid-to-fixed** com foco em mobile-first.

- **Grid System:** 12 colunas no desktop e 4 colunas no mobile.
- **Margins & Gutters:** use `base` (16px) como padrão.
- **Vertical Pacing:** use `section` (96px) no desktop e `xxl` (48px) no mobile.
- **Internal Spacing:** cards e containers usam `xl` (32px).

## Elevation & Depth

A hierarquia é estabelecida por **Tonal Layers** em vez de elevação tradicional.

- **The Layering Order:** Base Canvas < Section Bands < Cards.
- **Shadows:** use apenas a sombra “Soft Float” em estados específicos de interação.
- **Borders:** use bordas de 1px em `hairline`.
- **Dark Mode Context:** em seções Hero escuras, use cards sobrepostos em superfícies mais claras.

## Shapes

A linguagem de formas utiliza arredondamento acentuado para suavizar a estética institucional.

- **Buttons & Search:** use forma `pill`.
- **Cards:** use `rounded-xl` (24px).
- **Inputs:** use `rounded-md` (12px).
- **Avatars:** use `rounded-full`.

## Components

### Buttons

| Tipo | Especificação |
| --- | --- |
| Primary | Formato pill, preenchimento `#0052ff`, texto branco, label em negrito e centralizada |
| Secondary | Formato pill, preenchimento `#eef0f3`, texto `#0a0b0d`, sem borda |
| Quiz Options | Card com 24px de raio, borda hairline de 1px; ao selecionar, a borda passa para 2px e adota a cor da Quiz Palette ou Primary Blue |

### Cards

| Tipo | Especificação |
| --- | --- |
| Study Card | Fundo branco, raio de 24px, borda hairline de 1px, padding interno de 32px |
| Leaderboard Row | Fundo `#f7f7f7`, raio de 8px, números monoespaçados para ranking |

### Input Fields

| Tipo | Especificação |
| --- | --- |
| Text Inputs | Raio de 12px, borda hairline; em foco, borda `2px #0052ff` |

### Chips & Badges

| Tipo | Especificação |
| --- | --- |
| Status Tags | Formato pill, texto de 12px, peso bold, fundo com tingimento sutil da cor semântica |

### Progress Bars

| Tipo | Especificação |
| --- | --- |
| Arena Progress | Altura de 8px, trilha pill, preenchimento primary blue, trilha em `#eef0f3` |
