import { useEffect, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { useMatch } from '../../../hooks/useMatch';
import { useMatchSocket } from '../../../hooks/useMatchSocket';
import partidaService from '../../../services/partidaService';
import type { PartidaEventoResponse, LobbyPartidaResponse, ParticipacaoResponse } from '../../../types/partida';
import { Card } from '../../../components/ui/Card/Card';
import { Button } from '../../../components/ui/Button/Button';
import { ParticipantList } from '../../../components/domain/ParticipantList/ParticipantList';
import styles from './LobbySala.module.scss';

export function LobbySala() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const { user, token } = useAuth();
  const match = useMatch();

  const matchId = Number(paramId);

  useEffect(() => {
    partidaService.getParticipants(matchId).then((lobby: LobbyPartidaResponse) => {
      match.setMatchId(matchId);
      match.setCodigoPin(lobby.partida.codigoPin);
      match.setTempoDeJogo(lobby.partida.tempoDeJogo);
      match.setStatus(lobby.partida.statusSala);
      match.setHostId(lobby.partida.host.id);
      match.setIsHost(lobby.partida.host.id === user?.id);
      match.setParticipants(lobby.participantes);
    });
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [matchId]);

  const handleLobby = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'JOGADOR_ENTROU' || e.tipo === 'LOBBY_SINCRONIZADO') {
      const dados = e.dados as { participantes?: ParticipacaoResponse[] };
      if (dados.participantes) match.setParticipants(dados.participantes);
    }
  }, [match]);

  const handleStatus = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'PARTIDA_INICIADA') {
      partidaService.getQuestions(matchId).then(match.setQuestions);
      navigate(`/match/${matchId}/jogo`, { replace: true });
    }
    if (e.tipo === 'PARTIDA_CANCELADA') {
      match.reset();
      navigate('/', { replace: true });
    }
  }, [navigate, matchId, match]);

  useMatchSocket({
    matchId,
    token: token ?? '',
    onLobby: handleLobby,
    onStatus: handleStatus,
    onLeaderboard: () => {},
    onPlacar: () => {},
    onResposta: () => {},
  });

  async function handleStart() {
    await partidaService.start(matchId);
  }

  async function handleCancel() {
    await partidaService.cancel(matchId);
    match.reset();
    navigate('/');
  }

  const canStart = match.participants.length >= 2;

  return (
    <div className={styles.page}>
      <Card className={styles.pinCard}>
        <span className={styles.pinLabel}>Código PIN da sala</span>
        <span className={styles.pin}>{match.codigoPin}</span>
        <span className={styles.pinHint}>Compartilhe este código com os jogadores</span>
      </Card>

      <div className={styles.section}>
        <div className={styles.sectionTitle}>
          <span>Participantes</span>
          <span>{match.participants.length} na sala</span>
        </div>
        <ParticipantList participants={match.participants} hostId={match.hostId ?? 0} />
      </div>

      {match.isHost ? (
        <div className={styles.actions}>
          {!canStart && (
            <p className={styles.waiting}>Aguardando pelo menos um outro jogador para iniciar</p>
          )}
          <Button fullWidth disabled={!canStart} onClick={handleStart}>
            Começar partida
          </Button>
          <Button fullWidth variant="danger" onClick={handleCancel}>
            Fechar sala
          </Button>
        </div>
      ) : (
        <p className={styles.waiting}>Aguardando o host iniciar a partida...</p>
      )}
    </div>
  );
}
