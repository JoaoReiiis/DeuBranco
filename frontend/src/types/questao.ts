export type Alternativa = 'A' | 'B' | 'C' | 'D' | 'E';

export type Disciplina =
  | 'PORTUGUES' | 'INGLES' | 'ESPANHOL'
  | 'MATEMATICA' | 'FISICA' | 'QUIMICA' | 'BIOLOGIA'
  | 'HISTORIA' | 'GEOGRAFIA' | 'FILOSOFIA' | 'SOCIOLOGIA'
  | 'ARTES' | 'EDUCACAO_FISICA' | 'REDACAO';

export type Instituicao =
  | 'ENEM' | 'FUVEST' | 'UNICAMP' | 'UNESP' | 'UERJ'
  | 'UFPR' | 'UFMG' | 'ITA' | 'IME' | 'OUTRA';

export interface QuestaoResponse {
  id: number;
  enunciado: string;
  opcaoA: string;
  opcaoB: string;
  opcaoC: string;
  opcaoD: string;
  opcaoE: string;
  alternativaCorreta: Alternativa;
  disciplina: Disciplina;
  instituicao: Instituicao;
  imagens: string[] | null;
  ativo: boolean;
}

export interface QuestaoRequest {
  enunciado: string;
  opcaoA: string;
  opcaoB: string;
  opcaoC: string;
  opcaoD: string;
  opcaoE: string;
  alternativaCorreta: Alternativa;
  disciplina: Disciplina;
  instituicao: Instituicao;
  imagens?: string[];
  ativo?: boolean;
}

export interface QuestaoFiltros {
  disciplina?: Disciplina;
  instituicao?: Instituicao;
  ativo?: boolean;
  page?: number;
  size?: number;
}

export const DISCIPLINAS: { value: Disciplina; label: string }[] = [
  { value: 'PORTUGUES', label: 'Português' },
  { value: 'INGLES', label: 'Inglês' },
  { value: 'ESPANHOL', label: 'Espanhol' },
  { value: 'MATEMATICA', label: 'Matemática' },
  { value: 'FISICA', label: 'Física' },
  { value: 'QUIMICA', label: 'Química' },
  { value: 'BIOLOGIA', label: 'Biologia' },
  { value: 'HISTORIA', label: 'História' },
  { value: 'GEOGRAFIA', label: 'Geografia' },
  { value: 'FILOSOFIA', label: 'Filosofia' },
  { value: 'SOCIOLOGIA', label: 'Sociologia' },
  { value: 'ARTES', label: 'Artes' },
  { value: 'EDUCACAO_FISICA', label: 'Educação Física' },
  { value: 'REDACAO', label: 'Redação' },
];

export const INSTITUICOES: { value: Instituicao; label: string }[] = [
  { value: 'ENEM', label: 'ENEM' },
  { value: 'FUVEST', label: 'FUVEST' },
  { value: 'UNICAMP', label: 'UNICAMP' },
  { value: 'UNESP', label: 'UNESP' },
  { value: 'UERJ', label: 'UERJ' },
  { value: 'UFPR', label: 'UFPR' },
  { value: 'UFMG', label: 'UFMG' },
  { value: 'ITA', label: 'ITA' },
  { value: 'IME', label: 'IME' },
  { value: 'OUTRA', label: 'Outra' },
];
