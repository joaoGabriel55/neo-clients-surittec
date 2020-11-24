import React from 'react';
import axios from 'axios'


import { makeStyles } from '@material-ui/core/styles';
import { snackbarService } from "uno-material-ui";
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';

import { Add } from '@material-ui/icons';
import Fab from '@material-ui/core/Fab';
import InputMask from "react-input-mask";
import { Divider } from '@material-ui/core';

import theme from '../../theme'

import { ClienteModel } from '../../models/ClienteModel'

import { createCliente, updateCliente } from '../../services/ClienteService'
import EmailList from './EmailList';

import * as EmailValidator from 'email-validator';
import TelefoneList from './TelefoneList';

export default function ClienteDialog(props) {
  const { onChange, editData, onClearEditData } = props

  const classes = useStyles();
  const [open, setOpen] = React.useState(false);

  const [cliente, setCliente] = React.useState(new ClienteModel())

  const [email, setEmail] = React.useState({ emailAddress: '' })
  const [emails, setEmails] = React.useState([])

  const [telefone, setTelefone] = React.useState({ numero: '', tipoTelefone: '' })
  const [telefones, setTelefones] = React.useState([])

  const [cep, setCep] = React.useState('')
  const [endereco, setEndereco] = React.useState(null)

  React.useEffect(() => {
    if (editData) {
      handleClickOpen(editData)
    }
  })

  const updateField = e => {
    setCliente({
      ...cliente,
      [e.target.name]: e.target.value
    })
  }

  const updateAddressField = e => {
    setEndereco({
      ...endereco,
      [e.target.name]: e.target.value
    })
  }

  function addEmail() {
    if (email && email.emailAddress.length > 0 && EmailValidator.validate(email.emailAddress)) {
      setEmails([...emails, email])
      setCliente({
        ...cliente,
        emails: [...emails, email]
      })
      setEmail({ emailAddress: '' })
    } else {
      snackbarService.showSnackbar('Email inválido', 'error')
    }
  }

  function addTelefone() {
    if (telefone && telefone.numero && telefone.tipoTelefone) {
      const onlyNumbers = telefone.numero.replace(/\D/g, "");
      if (telefone.tipoTelefone === 'CELULAR' && onlyNumbers.length !== 11) {
        snackbarService.showSnackbar('Verifique o quantidade de dígitos', 'error')
        return
      } else if (telefone.tipoTelefone !== 'CELULAR' && onlyNumbers.length !== 10) {
        snackbarService.showSnackbar('Verifique o quantidade de dígitos', 'error')
        return
      }

      setTelefones([...telefones, telefone])
      setCliente({
        ...cliente,
        telefones: [...telefones, telefone]
      })
      setTelefone({ numero: '', tipoTelefone: '' })
    } else {
      snackbarService.showSnackbar('Telefone inválido', 'error')
      return
    }
  }

  function onRemoveEmail(indexEmail) {
    const newEmails = emails.filter((_, index) => index !== indexEmail)
    setEmails(newEmails)
    setCliente({
      ...cliente,
      emails: newEmails
    })
  }

  function onRemoveTelefone(indexTelefone) {
    const newTelefones = telefones.filter((_, index) => index !== indexTelefone)
    setTelefones(newTelefones)
    setCliente({
      ...cliente,
      telefones: newTelefones
    })
  }

  function loadAddress() {
    const cepPure = cep.replace(/\D/g, "");
    if (cepPure.length !== 8) {
      snackbarService.showSnackbar('CEP inválido', 'error')
      return
    } else {
      axios.get(`https://viacep.com.br/ws/${cep}/json`)
        .then(res => {
          const data = {
            cep: res.data['cep'],
            logradouro: res.data['logradouro'],
            bairro: res.data['bairro'],
            cidade: res.data['localidade'],
            uf: res.data['uf']
          }
          setEndereco({ ...data })
        })
        .catch(() => snackbarService.showSnackbar('CEP inválido', 'error'))
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    const clienteId = cliente.id;
    try {
      setCliente({
        ...cliente,
        endereco: endereco
      })

      const payload = {
        nome: cliente.nome,
        cpf: cliente.cpf,
        endereco: endereco,
        emails: cliente.emails,
        telefones: cliente.telefones
      }

      if (!clienteId)
        await createCliente(payload)
      else {
        await updateCliente(clienteId, payload)
      }

      const message = `Cliente ${cliente.nome} ${clienteId ? 'atualizado' : 'cadastrado'} com sucesso!`

      setCliente(new ClienteModel())
      setOpen(false)
      snackbarService.showSnackbar(message, 'success')
      await onChange()
    }
    catch (err) {
      console.log(err)
      snackbarService.showSnackbar(`Erro ao ${clienteId ? 'atualizar' : 'cadastrar'} o cliente`, 'error')
    }
  }

  const handleClickOpen = (editData) => {
    if (editData && editData.id) {
      setCliente(editData)
      const { emails, endereco, telefones } = editData
      setEmails(emails)
      setTelefones(telefones)
      setCep(endereco.cep)
      setEndereco(endereco)
      onClearEditData()
    } else {
      setCliente(new ClienteModel())
      setCep('')
      setEndereco(null)
      setEmails([])
      setTelefones([])
    }
    setOpen(true);
  };

  const handleClose = () => {
    setCliente(new ClienteModel())
    setOpen(false);
  };

  return (
    <div>
      <Fab variant="extended" color="secondary" className={classes.fab} onClick={handleClickOpen}>
        <Add className={classes.extendedIcon} />
        Cadastrar cliente
      </Fab>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
        maxWidth="sm"
        fullWidth={true}>
        <DialogTitle id="form-dialog-title">
          <div style={{ color: theme.palette.secondary.main }}>
            {`${cliente.id ? 'Atualizar' : 'Cadastrar novo'} cliente`}</div>
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <TextField
              id="nome"
              required
              variant="outlined"
              autoFocus
              margin="normal"
              label="Nome"
              color="secondary"
              name="nome"
              value={cliente.nome}
              onChange={updateField}
              fullWidth
            />
            <div className={classes.inlineFlexRow}>
              <TextField
                id="emails"
                required={emails.length == 0}
                variant="outlined"
                autoFocus
                margin="normal"
                label="Email"
                color="secondary"
                name="emails"
                value={email.emailAddress}
                onChange={(e) => setEmail({ emailAddress: e.target.value })}
                fullWidth
              />
              <div style={{ width: 8, height: 8 }}></div>
              <Tooltip title="Adicionar Email" aria-label="add">
                <IconButton aria-label="add" color='secondary' onClick={addEmail}>
                  <Add />
                </IconButton>
              </Tooltip>
            </div>
            <EmailList emails={emails} onRemoveEmail={onRemoveEmail} />
            <InputMask
              mask="999.999.999-99"
              id="cpf"
              maskChar=" "
              name="cpf"
              value={cliente.cpf}
              onChange={updateField}
            >
              {() => <TextField
                required
                name="cpf"
                variant="outlined"
                margin="normal"
                label="CPF"
                color="secondary"
                fullWidth
              />}
            </InputMask>
            <div className={classes.inlineFlexRow}>
              <FormControl
                variant="outlined" className={classes.formControl} color="secondary" required
              >
                <InputLabel id="demo-simple-select-outlined-label">Tipo Telefone</InputLabel>
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  value={telefone.tipoTelefone}
                  onChange={(e) => setTelefone({ numero: telefone.numero, tipoTelefone: e.target.value })}

                >
                  <MenuItem value={'CELULAR'}>Celular</MenuItem>
                  <MenuItem value={'COMERCIAL'}>Comercial</MenuItem>
                  <MenuItem value={'RESIDENCIAL'}>Residencial</MenuItem>
                </Select>
              </FormControl>
              <InputMask
                mask={telefone.tipoTelefone === 'CELULAR' ? '(99) 99999-9999' : '(99) 9999-9999'}
                id="telefone"
                maskChar=" "
                name="telefone"
                value={telefone.numero}
                onChange={(e) => setTelefone({ numero: e.target.value, tipoTelefone: telefone.tipoTelefone })}
              >
                {() => <TextField
                  required={telefones.length == 0}
                  name="telefone"
                  variant="outlined"
                  margin="normal"
                  label="Telefone"
                  color="secondary"
                />}
              </InputMask>
              <div style={{ width: 8, height: 8 }}></div>
              <Tooltip title="Adicionar Email" aria-label="add">
                <IconButton aria-label="add" color='secondary' onClick={addTelefone}>
                  <Add />
                </IconButton>
              </Tooltip>
            </div>
            <TelefoneList telefones={telefones} onRemoveTelefone={onRemoveTelefone} />
            <div className={classes.inlineFlexRow}>
              <InputMask
                mask="99999-999"
                id="cep"
                maskChar=" "
                name="cep"
                value={cep}
                onChange={(e) => setCep(e.target.value)}
              >
                {() => <TextField
                  required
                  variant="outlined"
                  margin="normal"
                  color="secondary"
                  name="cep"
                  label="CEP"
                  fullWidth
                />}
              </InputMask>
              <div style={{ width: 8, height: 8 }}></div>
              <Tooltip title="Carregar informações" aria-label="add">
                <IconButton aria-label="add" color='secondary' onClick={loadAddress}>
                  <CloudDownloadIcon />
                </IconButton>
              </Tooltip>
            </div>
            {endereco !== null ? (
              <>
                <TextField
                  id="logradouro"
                  required
                  variant="outlined"
                  autoFocus
                  margin="normal"
                  label="Logradouro"
                  color="secondary"
                  name="logradouro"
                  value={endereco.logradouro}
                  onChange={updateAddressField}
                  fullWidth
                />
                <TextField
                  id="bairro"
                  required
                  variant="outlined"
                  autoFocus
                  margin="normal"
                  label="Bairro"
                  color="secondary"
                  name="bairro"
                  value={endereco.bairro}
                  onChange={updateAddressField}
                  fullWidth
                />
                <TextField
                  id="cidade"
                  required
                  variant="outlined"
                  autoFocus
                  margin="normal"
                  label="Cidade"
                  color="secondary"
                  name="cidade"
                  value={endereco.cidade}
                  onChange={updateAddressField}
                  fullWidth
                />
                <TextField
                  id="uf"
                  required
                  variant="outlined"
                  autoFocus
                  margin="normal"
                  label="Logradouro"
                  color="secondary"
                  name="uf"
                  value={endereco.uf}
                  onChange={updateAddressField}
                  fullWidth
                />
                <TextField
                  id="complemento"
                  variant="outlined"
                  autoFocus
                  margin="normal"
                  label="Complemento"
                  color="secondary"
                  name="complemento"
                  value={endereco.complemento}
                  onChange={updateAddressField}
                  fullWidth
                />
              </>
            ) : null}

          </DialogContent>
          <br></br>
          <Divider />
          <DialogActions>
            <Button onClick={handleClose} color="secondary">
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              style={{ color: 'white' }}
              disableElevation>
              {`${cliente.id ? 'Atualizar' : 'Cadastrar'}`}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </div>
  );
}

const useStyles = makeStyles((theme) => ({
  fab: {
    position: 'absolute',
    bottom: theme.spacing(4),
    right: theme.spacing(2),
  },
  extendedIcon: {
    marginRight: theme.spacing(1),
  },
  inlineFlexRow: {
    display: 'flex', flexDirection: 'row',
    justifyContent: 'space-between', alignItems: 'center',
    marginTop: 8,
    ['@media (max-width:780px)']: {
      display: 'flex', flexDirection: 'column'
    }
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
}));
