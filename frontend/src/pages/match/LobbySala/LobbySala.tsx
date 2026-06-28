import { useEffect, useCallback } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { useMatch } from '../../../hooks/useMatch';
import { useMatchSocket } from '../../../hooks/useMatchSocket';
import partidaService from '../../../services/partidaService';
import type { PartidaEventoResponse, LobbyPartidaResponse, ParticipacaoResponse } from '../../../types/partida';
import { Logo } from '../../../components/ui/Logo/Logo';
import { Button } from '../../../components/ui/Button/Button';
import styles from './LobbySala.module.scss';

const MAX_SLOTS = 12;

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
  const pinDigits = (match.codigoPin ?? '------').split('');

  // Build slot array (filled participants + empty open slots)
  const slots = Array.from({ length: MAX_SLOTS }, (_, i) => match.participants[i] ?? null);

  return (
    <div className={styles.page}>
      {/* Dedicated lobby header */}
      <header className={styles.header}>
        <div className={styles.headerInner}>
          <Link to="/" className={styles.logoBox}>
            <Logo variant="horizontal" height={32} />
          </Link>

          {match.isHost && (
            <span className={styles.hostBadge}>HOST ACTIVE</span>
          )}

          <div className={styles.headerRight}>
            {match.isHost ? (
              <button className={styles.cancelBtn} onClick={handleCancel}>
                CANCELAR SESSÃO
              </button>
            ) : (
              <span className={styles.waitingLabel}>AGUARDANDO AÇÃO DO ANFITRIÃO...</span>
            )}
          </div>
        </div>
      </header>

      <div className={styles.body}>
        {/* PIN */}
        <div className={styles.pinCard}>
          <p className={styles.pinLabel}>Código PIN da sala</p>
          <div className={styles.pinDigits}>
            {pinDigits.map((ch, i) => (
              <span key={i} className={styles.pinDigit}>{ch}</span>
            ))}
          </div>
          <p className={styles.pinHint}>Compartilhe este código com os jogadores</p>
        </div>

        {/* Avatar grid */}
        <div className={styles.grid}>
          {slots.map((p, i) => (
            <div key={i} className={`${styles.slot} ${p ? styles['slot--filled'] : ''}`}>
              {p ? (
                <>
                  <div className={styles.avatar}>
                    {p.jogador.nome.charAt(0).toUpperCase()}
                  </div>
                  <span className={styles.slotName}>{p.jogador.nome.split(' ')[0]}</span>
                  {p.jogador.id === match.hostId && (
                    <span className={styles.hostTag}>host</span>
                  )}
                </>
              ) : (
                <span className={styles.openLabel}>Open</span>
              )}
            </div>
          ))}
        </div>

        {/* Actions */}
        {match.isHost && (
          <div className={styles.startArea}>
            {!canStart && (
              <p className={styles.waitHint}>Aguardando pelo menos um outro jogador</p>
            )}
            <Button fullWidth disabled={!canStart} onClick={handleStart}>
              Start Game Now
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}
