import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

import ArrowDropUpIcon from '@material-ui/icons/ArrowDropUp';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';

function Finances() {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <FormControl variant="outlined" size="small" className={classes.formControl}>
        <InputLabel id="demo-simple-select-outlined-label">Mês</InputLabel>
        <Select
          labelId="select-outlined-label"
          id="select-outlined"
          label="Mês"
          value={10}
        >
          <MenuItem value={10}>Agosto</MenuItem>
          <MenuItem value={20}>Setembro</MenuItem>
          <MenuItem value={30}>Outubro</MenuItem>
        </Select>
      </FormControl>
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <section>
              <h3><ArrowDropUpIcon />Entrada</h3>
              <h1>R$ 500,00</h1>
            </section>
          </Paper>
        </Grid>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <section>
              <h3><ArrowDropDownIcon />Saída</h3>
              <h1>- R$ 100,00</h1>
            </section>
          </Paper>
        </Grid>
        <Grid item xs={12}>
          <Paper className={classes.paper}>A table</Paper>
        </Grid>
      </Grid>
    </div>
  )
}

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'left',
    color: theme.palette.text.secondary,
  },
  formControl: {
    marginBottom: theme.spacing(2),
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
}));

export default Finances;