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


export default function EmailList({ emails, onRemoveEmail, isView }) {

  return (
    <List dense component="nav"
      style={{ marginTop: 28 }}
      subheader={
        <ListSubheader component="div" style={{ marginBottom: 8 }}>
          <Typography variant="body1">
            {emails.length > 0 ? <b>Emails</b> : <b>Nenhum email adicionado</b>}
          </Typography>
        </ListSubheader>
      }>
      {emails.map((value) => {
        const labelId = `checkbox-list-secondary-label-${value.emailAddress}`;
        return (
          <>
            <ListItem key={value} style={{ paddingTop: 12, paddingBottom: 12 }} button>
              <ListItemText id={labelId}  >
                {value.emailAddress}
              </ListItemText>
              {isView ? null :
                <ListItemSecondaryAction>
                  <IconButton onClick={() => onRemoveEmail(emails.indexOf(value))} aria-label="delete">
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
