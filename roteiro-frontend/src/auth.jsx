import { createContext, useContext, useState } from 'react';

// RNF04 — controle de acesso por perfil (camada de interface).
// ATENÇÃO: o backend ainda não tem autenticação. Isto só controla o que aparece
// na tela; a segurança real precisa de Spring Security no backend.
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(() => {
    try { return JSON.parse(sessionStorage.getItem('sessao')); } catch { return null; }
  });

  const entrar = (s) => { sessionStorage.setItem('sessao', JSON.stringify(s)); setSessao(s); };
  const sair = () => { sessionStorage.removeItem('sessao'); setSessao(null); };

  return (
    <AuthContext.Provider value={{ sessao, entrar, sair, ehGerente: sessao?.perfil === 'GERENTE' }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
