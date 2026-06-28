export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
}

export interface JogadorResponse {
  id: number;
  nome: string;
  email: string;
  role: 'JOGADOR' | 'ADMIN';
}

export interface JogadorCreateRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface JogadorUpdateRequest {
  nome: string;
  email: string;
  senha?: string;
}
