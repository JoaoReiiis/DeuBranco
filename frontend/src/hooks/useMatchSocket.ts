import { useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import type { PartidaEventoResponse } from '../types/partida';

interface UseMatchSocketOptions {
  matchId: number;
  token: string;
  onLobby: (e: PartidaEventoResponse) => void;
  onStatus: (e: PartidaEventoResponse) => void;
  onLeaderboard: (e: PartidaEventoResponse) => void;
  onPlacar: (e: PartidaEventoResponse) => void;
  onResposta: (e: PartidaEventoResponse) => void;
  onUserQueue?: (e: PartidaEventoResponse) => void;
}

export function useMatchSocket({
  matchId, token, onLobby, onStatus, onLeaderboard, onPlacar, onResposta, onUserQueue,
}: UseMatchSocketOptions) {
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    const proto = window.location.protocol === 'https:' ? 'wss' : 'ws';
    const client = new Client({
      brokerURL: `${proto}://${window.location.host}/ws`,
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 3000,
      onConnect: () => {
        client.subscribe(`/topic/partidas/${matchId}/lobby`, (msg) =>
          onLobby(JSON.parse(msg.body) as PartidaEventoResponse));
        client.subscribe(`/topic/partidas/${matchId}/status`, (msg) =>
          onStatus(JSON.parse(msg.body) as PartidaEventoResponse));
        client.subscribe(`/topic/partidas/${matchId}/leaderboard`, (msg) =>
          onLeaderboard(JSON.parse(msg.body) as PartidaEventoResponse));
        client.subscribe(`/topic/partidas/${matchId}/placar`, (msg) =>
          onPlacar(JSON.parse(msg.body) as PartidaEventoResponse));
        client.subscribe(`/topic/partidas/${matchId}/respostas`, (msg) =>
          onResposta(JSON.parse(msg.body) as PartidaEventoResponse));
        if (onUserQueue) {
          client.subscribe(`/user/queue/partidas`, (msg) =>
            onUserQueue(JSON.parse(msg.body) as PartidaEventoResponse));
        }
        client.publish({ destination: `/app/partidas/${matchId}/sincronizar` });
      },
      onStompError: (frame) => {
        console.error('STOMP error', frame);
      },
    });
    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
      clientRef.current = null;
    };
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [matchId, token]);

  const publish = useCallback((destination: string, body?: object) => {
    clientRef.current?.publish({
      destination,
      body: body ? JSON.stringify(body) : undefined,
    });
  }, []);

  return { publish };
}
