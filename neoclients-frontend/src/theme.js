import { createMuiTheme } from '@material-ui/core/styles'
const theme = createMuiTheme({
    typography: {
        fontSize: 12
    },
    palette: {
        primary: { main: '#289c98', dark: '#1c6361' },
        secondary: { main: '#282C9A', light: '#C9CAF1' }
    },
})
export default theme