import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import InputMask from "react-input-mask";
import { Divider } from '@material-ui/core';

import theme from '../../theme'

import { ClienteModel } from '../../models/ClienteModel'

import EmailList from './EmailList';
import TelefoneList from './TelefoneList';

export default function ClienteViewerDialog(props) {
  const { viewData, setViewClient } = props

  const [open, setOpen] = React.useState(false);
  const [cliente, setCliente] = React.useState(new ClienteModel())
  const [telefones, setTelefones] = React.useState([])
  const [emails, setEmails] = React.useState([])
  const [endereco, setEndereco] = React.useState({})

  React.useEffect(() => {
    if (viewData) {
      handleClickOpen(viewData)
    }
  })

  const handleClickOpen = (viewData) => {
    if (viewData) {
      const { emails, endereco, telefones } = viewData
      setCliente(viewData)
      setEmails(emails)
      setTelefones(telefones)
      setEndereco(endereco)
      setOpen(true)
    }
  };

  const handleClose = () => {
    setViewClient(null)
    setOpen(false);
  };

  return (
    <div>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
        maxWidth="sm"
        fullWidth={true}>
        <DialogTitle id="form-dialog-title">
          <div style={{ color: theme.palette.secondary.main }}>
            {cliente.nome}
          </div>
        </DialogTitle>
        <Divider />
        <DialogContent>
          <p><strong>CPF: </strong>{cliente.cpf}</p>
          <Divider />
          <h3>Endereço</h3>
          <p><strong>CEP: </strong>{endereco.cep}</p>
          <p><strong>Rua: </strong>{endereco.logradouro}</p>
          <p><strong>Bairro: </strong>{endereco.bairro}</p>
          <p><strong>Cidade/UF: </strong>{endereco.cidade}/{endereco.uf}</p>
          {endereco.complemento ?
            <p><strong>Complemento: </strong>{endereco.complemento}</p>
            : null}
          <Divider />
          <EmailList emails={emails} isView={true} />
          <TelefoneList telefones={telefones} isView={true} />
        </DialogContent>
        <Divider />
        <DialogActions>
          <Button onClick={handleClose} color="secondary">
            Fechar
          </Button>
        </DialogActions>
      </Dialog>
    </div >
  );
}
