import React, { useState, createContext } from "react";


const userContext = { username: null, profile: null }
// Create Context Object
export const AuthContext = createContext(userContext)

// Create a provider for components to consume and subscribe to changes
export const AuthContextProvider = ({ children }) => {
    const [userLogged, setUserLogged] = useState(userContext)

    // Login updates the user data with a name parameter
    const loginContext = (user) => {
        userContext.username = user.username
        userContext.profile = user.profile
        setUserLogged(() => (userContext));
    };

    // Logout updates the user data to default
    const logoutContext = () => {
        userContext.username = null
        userContext.profile = null
        setUserLogged(() => (userContext));
    };

    return (
        <AuthContext.Provider value={{ userLogged, loginContext, logoutContext }}>
            {children}
        </AuthContext.Provider>
    );
};