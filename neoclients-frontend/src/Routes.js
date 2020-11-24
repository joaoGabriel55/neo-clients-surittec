import React from 'react';

import { tokenDecoder } from './utils/TokenDecoder'

import { Switch, Redirect, useHistory } from 'react-router-dom';

import { AuthContext } from './auth/context/AuthContextProvider';

import RouteWithLayout from './components/RouterWithLayout';

import Main from './layouts/template/Main';

import SimpleLayout from './layouts/template/SimpleLayout'
import SignIn from './auth/views/SignIn'

import { Clientes } from './views'

const SignInRouter = () => {
  return (
    <>
      <Redirect
        exact
        from="/"
        to="/sign-in"
      />
      <RouteWithLayout
        component={SignIn}
        exact
        layout={SimpleLayout}
        path="/sign-in"
      />
    </>
  )
}

const Routes = () => {
  const { loginContext } = React.useContext(AuthContext);
  const history = useHistory()

  async function userTokenValidation() {

    const token = localStorage.getItem('accessToken')
    if (token) {
      try {
        const userToken = tokenDecoder(token)
        console.log(userToken)
        const { username, authorities } = userToken.sub
        const userData = {
          username,
          profile: authorities[0].authority
        }
        loginContext(userData)

        return true
      } catch (error) {
        return false
      }
    } else if (!localStorage.getItem('accessToken')) {
      history.replace('/sign-in')
    }
  }

  if (!userTokenValidation())
    return (
      <Switch>
        {SignInRouter()}
      </Switch>
    )

  return (
    <Switch>
      <Redirect
        exact
        from="/"
        to="/sign-in"
      />
      <RouteWithLayout
        component={SignIn}
        exact
        layout={SimpleLayout}
        path="/sign-in"
      />
      <RouteWithLayout
        component={Clientes}
        exact
        layout={Main}
        path="/clientes"
      />
    </Switch>
  )
}

export default Routes;