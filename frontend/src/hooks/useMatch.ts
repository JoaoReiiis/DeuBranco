import { useContext } from 'react';
import { MatchContext } from '../contexts/MatchContext';

export function useMatch() {
  const ctx = useContext(MatchContext);
  if (!ctx) throw new Error('useMatch must be used inside MatchProvider');
  return ctx;
}
