import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { MatchProvider } from './contexts/MatchContext';
import { AuthGuard, AdminGuard, PublicOnlyGuard } from './router/guards';
import { AppShell } from './components/layout/AppShell/AppShell';
import { AuthShell } from './components/layout/AuthShell/AuthShell';
import { Login } from './pages/Login/Login';
import { Register } from './pages/Register/Register';
import { Home } from './pages/Home/Home';
import { GerirPerguntas } from './pages/admin/GerirPerguntas/GerirPerguntas';
import { RegistrarPergunta } from './pages/admin/RegistrarPergunta/RegistrarPergunta';
import { EditarPergunta } from './pages/admin/EditarPergunta/EditarPergunta';
import { VisualizarPergunta } from './pages/admin/VisualizarPergunta/VisualizarPergunta';
import { ConfigurarSala } from './pages/match/ConfigurarSala/ConfigurarSala';
import { LobbySala } from './pages/match/LobbySala/LobbySala';
import { JogoAtivo } from './pages/match/JogoAtivo/JogoAtivo';
import { EspereOutrosJogadores } from './pages/match/EspereOutrosJogadores/EspereOutrosJogadores';
import { PodioFinal } from './pages/match/PodioFinal/PodioFinal';
import { PartidaFinalizada } from './pages/match/PartidaFinalizada/PartidaFinalizada';
import { HistoricoPartidas } from './pages/match/HistoricoPartidas/HistoricoPartidas';

export function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <MatchProvider>
          <Routes>
            {/* Public only */}
            <Route element={<PublicOnlyGuard />}>
              <Route element={<AuthShell />}>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
              </Route>
            </Route>

            {/* Authenticated */}
            <Route element={<AuthGuard />}>
              <Route element={<AppShell />}>
                <Route path="/" element={<Home />} />
                <Route path="/historico" element={<HistoricoPartidas />} />
                <Route path="/match/configurar" element={<ConfigurarSala />} />
                <Route path="/match/:matchId/lobby" element={<LobbySala />} />
                <Route path="/match/:matchId/jogo" element={<JogoAtivo />} />
                <Route path="/match/:matchId/espere" element={<EspereOutrosJogadores />} />
                <Route path="/match/:matchId/podio" element={<PodioFinal />} />
                <Route path="/match/:matchId/finalizada" element={<PartidaFinalizada />} />

                {/* Admin only */}
                <Route element={<AdminGuard />}>
                  <Route path="/admin/perguntas" element={<GerirPerguntas />} />
                  <Route path="/admin/perguntas/nova" element={<RegistrarPergunta />} />
                  <Route path="/admin/perguntas/busca" element={<VisualizarPergunta />} />
                  <Route path="/admin/perguntas/:id/editar" element={<EditarPergunta />} />
                </Route>
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </MatchProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
