import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListSubheader from '@material-ui/core/ListSubheader';
import ListItemText from '@material-ui/core/ListItemText';

import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Clear';
import Typography from '@material-ui/core/Typography';
import { Divider } from '@material-ui/core';

export default function TelefoneList({ telefones, onRemoveTelefone, isView }) {
  return (
    <List dense component="nav"
      style={{ marginTop: 28 }}
      subheader={
        <ListSubheader component="div" style={{ marginBottom: 8 }}>
          <Typography variant="body1">
            {telefones.length > 0 ? <b>Telefones</b> : <b>Nenhum telefone adicionado</b>}
          </Typography>
        </ListSubheader>
      }>
      {telefones.map((value) => {
        const labelId = `checkbox-list-secondary-label-${value.emailAddress}`;
        return (
          <>
            <ListItem key={value} style={{ paddingTop: 12, paddingBottom: 12 }} button>
              <ListItemText id={labelId}  >
                {value.numero} - {value.tipoTelefone}
              </ListItemText>

              {isView ? null :
                <ListItemSecondaryAction>
                  <IconButton onClick={() => onRemoveTelefone(telefones.indexOf(value))} aria-label="delete">
                    <DeleteIcon />
                  </IconButton>
                </ListItemSecondaryAction>}
            </ListItem>
            <Divider />
          </>
        );
      })}
    </List>
  )
}