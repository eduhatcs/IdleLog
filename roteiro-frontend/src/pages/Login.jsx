import { useState } from 'react';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import { useAsync } from '../hooks.js';
import { Alert, Field } from '../components/ui.jsx';

export default function Login() {
  const { entrar } = useAuth();
  const [perfil, setPerfil] = useState('GERENTE');
  const [pessoaId, setPessoaId] = useState('');
  const recurso = perfil === 'GERENTE' ? 'gerentes' : 'motoristas';
  const { data: pessoas, error, loading } = useAsync(() => api.list(recurso), [recurso]);

  const submit = (e) => {
    e.preventDefault();
    const p = pessoas.find((x) => String(x.id) === pessoaId);
    if (p) entrar({ perfil, id: p.id, nome: p.nome });
  };

  return (
    <div className="login">
      <form className="login-card" onSubmit={submit}>
        <span className="brand-mark big" aria-hidden="true" />
        <h1>Roteiros</h1>
        <p className="muted">Monitoramento de tempo parado e custos por roteiro.</p>

        <div className="seg" role="radiogroup" aria-label="Perfil">
          {[['GERENTE', 'Gerente'], ['MOTORISTA', 'Motorista']].map(([v, l]) => (
            <button type="button" key={v} role="radio" aria-checked={perfil === v}
              className={perfil === v ? 'on' : ''}
              onClick={() => { setPerfil(v); setPessoaId(''); }}>{l}</button>
          ))}
        </div>

        <Field label={perfil === 'GERENTE' ? 'Quem é você?' : 'Seu nome'}>
          <select value={pessoaId} onChange={(e) => setPessoaId(e.target.value)} required disabled={loading}>
            <option value="">Selecione</option>
            {pessoas?.map((p) => <option key={p.id} value={p.id}>{p.nome}</option>)}
          </select>
        </Field>

        <Alert>{error && 'Não foi possível falar com a API. Verifique se o banco e o Spring Boot estão rodando (porta 8080).'}</Alert>
        <button className="btn primary" disabled={!pessoaId}>Entrar</button>
        <small className="muted">Acesso simulado para o MVP: ainda não há senha nem login no backend.</small>
      </form>
    </div>
  );
}
