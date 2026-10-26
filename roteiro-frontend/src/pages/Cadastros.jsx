import { useState, useEffect } from 'react';
import { api } from '../api.js';
import { useAsync } from '../hooks.js';
import { Alert, Empty, Field, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { num } from '../utils.js';

// Configuração declarativa dos três cadastros simples (RF01, RF02 e clientes).
const CONFIG = {
  motoristas: {
    titulo: 'Motoristas',
    subtitulo: 'Quem roda os roteiros e o rendimento do veículo, usado no custo de combustível.',
    singular: 'motorista',
    campos: [
      { name: 'nome', label: 'Nome' },
      { name: 'telefone', label: 'Telefone' },
      { name: 'documento', label: 'Documento (CNH)' },
      { name: 'veiculo', label: 'Veículo' },
      { name: 'rendimentoKmLitro', label: 'Rendimento (km/l)', type: 'number', step: '0.1', min: '0.1', usaPadrao: true },
    ],
    colunas: [['nome', 'Nome'], ['telefone', 'Telefone'], ['veiculo', 'Veículo'],
      ['rendimentoKmLitro', 'km/l', (v) => num(v)]],
  },
  gerentes: {
    titulo: 'Gerentes',
    subtitulo: 'Coordenadores que acompanham os roteiros.',
    singular: 'gerente',
    campos: [
      { name: 'nome', label: 'Nome' },
      { name: 'telefone', label: 'Telefone' },
      { name: 'email', label: 'E-mail', type: 'email' },
    ],
    colunas: [['nome', 'Nome'], ['telefone', 'Telefone'], ['email', 'E-mail']],
  },
  clientes: {
    titulo: 'Clientes',
    subtitulo: 'Quem faz os pedidos entregues nos roteiros.',
    singular: 'cliente',
    campos: [
      { name: 'nome', label: 'Nome' },
      { name: 'telefone', label: 'Telefone' },
      { name: 'email', label: 'E-mail', type: 'email' },
    ],
    colunas: [['nome', 'Nome'], ['telefone', 'Telefone'], ['email', 'E-mail']],
  },
};

const vazio = (campos) => Object.fromEntries(campos.map((c) => [c.name, '']));

export default function Cadastros({ tipo }) {
  // key força reset do estado interno ao trocar de cadastro
  return <CadastroCrud key={tipo} tipo={tipo} cfg={CONFIG[tipo]} />;
}

function CadastroCrud({ tipo, cfg }) {
  useEffect(() => {
    alert('AVISO: Não colocar dados pessoais verdadeiros!');
  }, []);
  const { data, loading, error, reload } = useAsync(() => api.list(tipo), [tipo]);
  const [form, setForm] = useState(vazio(cfg.campos));
  const [editId, setEditId] = useState(null);
  const [msg, setMsg] = useState({ kind: 'error', text: '' });

  const set = (k, v) => setForm((f) => ({ ...f, [k]: v }));
  const cancelar = () => { setEditId(null); setForm(vazio(cfg.campos)); };

  const salvar = async (e) => {
    e.preventDefault();
    const body = { ...form };
    cfg.campos.forEach((c) => { if (c.type === 'number') body[c.name] = Number(body[c.name]); });
    try {
      if (editId) await api.update(tipo, editId, body);
      else await api.create(tipo, body);
      setMsg({ kind: 'success', text: editId ? 'Alterações salvas.' : `Cadastro de ${cfg.singular} criado.` });
      cancelar();
      reload();
    } catch (err) {
      setMsg({ kind: 'error', text: err.message });
    }
  };

  const editar = (item) => {
    setEditId(item.id);
    setForm(Object.fromEntries(cfg.campos.map((c) => [c.name, item[c.name] ?? ''])));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const excluir = async (item) => {
    if (!window.confirm(`Excluir ${cfg.singular} "${item.nome}"?`)) return;
    try {
      await api.remove(tipo, item.id);
      setMsg({ kind: 'success', text: 'Cadastro excluído.' });
      reload();
    } catch (err) {
      setMsg({ kind: 'error', text: `Não foi possível excluir: ${err.message}` });
    }
  };

  // UC01 A2: sugere o km/l padrão da parametrização global
  const usarPadrao = async (campo) => {
    try {
      const p = await api.parametrizacao();
      set(campo, p.kmPorLitroPadrao);
    } catch (err) {
      setMsg({ kind: 'error', text: err.message });
    }
  };

  return (
    <>
      <PageHeader title={cfg.titulo} subtitle={cfg.subtitulo} />
      <Alert kind={msg.kind}>{msg.text}</Alert>

      <Panel title={editId ? `Editar ${cfg.singular}` : `Novo ${cfg.singular}`}>
        <form className="form-grid" onSubmit={salvar}>
          {cfg.campos.map((c) => (
            <Field key={c.name} label={c.label}>
              <input type={c.type ?? 'text'} step={c.step} min={c.min} required
                value={form[c.name]} onChange={(e) => set(c.name, e.target.value)} />
              {c.usaPadrao && (
                <button type="button" className="link" onClick={() => usarPadrao(c.name)}>
                  Usar km/l padrão da frota
                </button>
              )}
            </Field>
          ))}
          <div className="form-actions">
            <button className="btn primary">{editId ? 'Salvar alterações' : `Cadastrar ${cfg.singular}`}</button>
            {editId && <button type="button" className="btn" onClick={cancelar}>Cancelar</button>}
          </div>
        </form>
      </Panel>

      <Panel>
        {loading && <Loading />}
        <Alert>{error}</Alert>
        {data && data.length === 0 && <Empty>Nenhum {cfg.singular} cadastrado ainda. Use o formulário acima.</Empty>}
        {data && data.length > 0 && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>{cfg.colunas.map(([, l]) => <th key={l}>{l}</th>)}<th aria-label="Ações" /></tr>
              </thead>
              <tbody>
                {data.map((item) => (
                  <tr key={item.id}>
                    {cfg.colunas.map(([k, l, fmt]) => <td key={l}>{fmt ? fmt(item[k]) : item[k]}</td>)}
                    <td className="row-actions">
                      <button className="link" onClick={() => editar(item)}>Editar</button>
                      <button className="link danger" onClick={() => excluir(item)}>Excluir</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Panel>
    </>
  );
}
