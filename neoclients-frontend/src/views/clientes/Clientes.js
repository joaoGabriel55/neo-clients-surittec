import React from 'react';
import { snackbarService } from "uno-material-ui";
import DataTable from '../../components/dataTable/DataTable'
import ClienteDialog from './ClienteDialog'
import ClienteViewerDialog from './ClienteViewerDialog'
import { AuthContext } from '../../auth/context/AuthContextProvider';

import { getClientes, deleteCliente } from '../../services/ClienteService'

const headCells = [
  { id: 'nome', numeric: false, disablePadding: true, label: 'Nome' },
  { id: 'cpf', numeric: true, disablePadding: false, label: 'CPF' }
];

function Clientes() {
  const { userLogged } = React.useContext(AuthContext);
  const [clientes, setClientes] = React.useState([])
  const [editCliente, setEditClient] = React.useState(null)
  const [viewCliente, setViewClient] = React.useState(null)

  async function getClientesAPI() {
    try {
      const response = await getClientes()
      setClientes(response.data.data)
    } catch (error) {
      snackbarService.showSnackbar('Problema ao carregar clientes', 'error')
    }
  }

  async function onChangeData() {
    await getClientesAPI()
  }

  function onEditData(data) {
    setEditClient(data)
  }

  function onViewData(data) {
    setViewClient(data)
  }

  async function onDeleteData(data) {
    if (data) {
      try {
        for (const cliente of data) {
          await deleteCliente(cliente.id)
        }
        snackbarService.showSnackbar('Cliente(s) removido(s) com sucesso!', 'success')
        await onChangeData()
      } catch (error) {
        snackbarService.showSnackbar('Problema ao remover cliente(s)', 'error')
      }
    }
  }

  function onClearEditData() {
    setEditClient(null)
  }

  React.useEffect(() => {
    async function loadCustomers() {
      await getClientesAPI()
    }
    loadCustomers()
  }, [])

  return (
    <>
      {clientes.length > 0 ?
        <DataTable
          title="Clientes"
          userLogged={userLogged}
          header={headCells}
          rows={clientes}
          onDeleteData={onDeleteData}
          onViewData={onViewData}
          onEditData={onEditData}
        /> : <h2>Não há clientes cadastrados</h2>}
      {viewCliente ?
        <ClienteViewerDialog viewData={viewCliente} setViewClient={setViewClient} />
        : null}
      {userLogged.profile === 'ROLE_ADMIN' ?
        <ClienteDialog
          onChange={onChangeData}
          editData={editCliente}
          onClearEditData={onClearEditData}
        />
        : null
      }
    </>
  )
}

export default Clientes;