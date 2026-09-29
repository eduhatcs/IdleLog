import { useState } from 'react';
import { api } from '../api.js';
import { useAsync } from '../hooks.js';
import { Alert, Empty, Field, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { fmtData } from '../utils.js';

const STATUS = ['PENDENTE', 'EM_ROTA', 'ENTREGUE', 'CANCELADO'];
const STATUS_LABEL = { PENDENTE: 'Pendente', EM_ROTA: 'Em rota', ENTREGUE: 'Entregue', CANCELADO: 'Cancelado' };
const hoje = () => new Date().toISOString().slice(0, 10);

export default function Pedidos() {
  const pedidos = useAsync(() => api.list('pedidos'));
  const clientes = useAsync(() => api.list('clientes'));
  const [form, setForm] = useState({ clienteId: '', data: hoje(), enderecoEntrega: '' });
  const [msg, setMsg] = useState({ kind: 'error', text: '' });
  const set = (k, v) => setForm((f) => ({ ...f, [k]: v }));

  const criar = async (e) => {
    e.preventDefault();
    try {
      await api.create('pedidos', { ...form, clienteId: Number(form.clienteId) });
      setMsg({ kind: 'success', text: 'Pedido criado com status Pendente.' });
      setForm({ clienteId: '', data: hoje(), enderecoEntrega: '' });
      pedidos.reload();
    } catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  const mudarStatus = async (p, status) => {
    try { await api.atualizarStatusPedido(p.id, status); pedidos.reload(); }
    catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  const excluir = async (p) => {
    if (!window.confirm(`Excluir o pedido #${p.id}?`)) return;
    try { await api.remove('pedidos', p.id); pedidos.reload(); }
    catch (err) { setMsg({ kind: 'error', text: `Não foi possível excluir: ${err.message}` }); }
  };

  return (
    <>
      <PageHeader title="Pedidos" subtitle="Entregas dos clientes que entram nos roteiros." />
      <Alert kind={msg.kind}>{msg.text}</Alert>

      <Panel title="Novo pedido">
        <form className="form-grid" onSubmit={criar}>
          <Field label="Cliente">
            <select required value={form.clienteId} onChange={(e) => set('clienteId', e.target.value)}>
              <option value="">Selecione</option>
              {clientes.data?.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
            </select>
          </Field>
          <Field label="Data">
            <input type="date" required value={form.data} onChange={(e) => set('data', e.target.value)} />
          </Field>
          <Field label="Endereço de entrega">
            <input required value={form.enderecoEntrega} onChange={(e) => set('enderecoEntrega', e.target.value)} />
          </Field>
          <div className="form-actions"><button className="btn primary">Criar pedido</button></div>
        </form>
      </Panel>

      <Panel>
        {pedidos.loading && <Loading />}
        <Alert>{pedidos.error}</Alert>
        {pedidos.data?.length === 0 && <Empty>Nenhum pedido ainda. Cadastre um cliente e crie o primeiro pedido.</Empty>}
        {pedidos.data?.length > 0 && (
          <div className="table-wrap">
            <table>
              <thead><tr><th>#</th><th>Cliente</th><th>Data</th><th>Endereço</th><th>Status</th><th aria-label="Ações" /></tr></thead>
              <tbody>
                {pedidos.data.map((p) => (
                  <tr key={p.id}>
                    <td>{p.id}</td>
                    <td>{p.clienteNome}</td>
                    <td>{fmtData(p.data)}</td>
                    <td>{p.enderecoEntrega}</td>
                    <td>
                      <select className={`status ${p.status}`} value={p.status} aria-label={`Status do pedido ${p.id}`}
                        onChange={(e) => mudarStatus(p, e.target.value)}>
                        {STATUS.map((s) => <option key={s} value={s}>{STATUS_LABEL[s]}</option>)}
                      </select>
                    </td>
                    <td className="row-actions"><button className="link danger" onClick={() => excluir(p)}>Excluir</button></td>
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
