import { Navigate, NavLink, Route, Routes } from 'react-router-dom';
import { useAuth } from './auth.jsx';
import Login from './pages/Login.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Cadastros from './pages/Cadastros.jsx';
import Pedidos from './pages/Pedidos.jsx';
import Roteiros from './pages/Roteiros.jsx';
import RoteiroDetalhe from './pages/RoteiroDetalhe.jsx';
import Parametrizacao from './pages/Parametrizacao.jsx';
import Relatorios from './pages/Relatorios.jsx';

const MENU_GERENTE = [
  ['/', 'Dashboard'],
  ['/roteiros', 'Roteiros'],
  ['/pedidos', 'Pedidos'],
  ['/motoristas', 'Motoristas'],
  ['/clientes', 'Clientes'],
  ['/gerentes', 'Gerentes'],
  ['/parametrizacao', 'Parâmetros'],
  ['/relatorios', 'Relatórios'],
];
const MENU_MOTORISTA = [['/roteiros', 'Meus roteiros']];

export default function App() {
  const { sessao, sair, ehGerente } = useAuth();
  if (!sessao) return <Login />;

  const menu = ehGerente ? MENU_GERENTE : MENU_MOTORISTA;

  return (
    <div className="shell">
      <aside className="side">
        <div className="brand">
          <span className="brand-mark" aria-hidden="true" />
          <div>
            <strong>Roteiros</strong>
            <small>Tempo parado e custos</small>
          </div>
        </div>
        <nav aria-label="Principal">
          {menu.map(([to, label]) => (
            <NavLink key={to} to={to} end={to === '/'}>{label}</NavLink>
          ))}
        </nav>
        <div className="who">
          <span>{sessao.nome}</span>
          <small>{ehGerente ? 'Gerente' : 'Motorista'}</small>
          <button className="link" onClick={sair}>Sair</button>
        </div>
      </aside>

      <main className="main">
        <Routes>
          {ehGerente ? (
            <>
              <Route path="/" element={<Dashboard />} />
              <Route path="/motoristas" element={<Cadastros tipo="motoristas" />} />
              <Route path="/gerentes" element={<Cadastros tipo="gerentes" />} />
              <Route path="/clientes" element={<Cadastros tipo="clientes" />} />
              <Route path="/pedidos" element={<Pedidos />} />
              <Route path="/parametrizacao" element={<Parametrizacao />} />
              <Route path="/relatorios" element={<Relatorios />} />
            </>
          ) : (
            <Route path="/" element={<Navigate to="/roteiros" replace />} />
          )}
          <Route path="/roteiros" element={<Roteiros />} />
          <Route path="/roteiros/:id" element={<RoteiroDetalhe />} />
          <Route path="*" element={<Navigate to={ehGerente ? '/' : '/roteiros'} replace />} />
        </Routes>
      </main>
    </div>
  );
}
