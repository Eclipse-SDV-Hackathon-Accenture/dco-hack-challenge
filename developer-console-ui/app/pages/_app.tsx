import '../styles/globals.css'
import type { AppProps } from 'next/app'
import ForceSession from '../components/auth/ForceSession'
import { ApolloProvider } from '@apollo/client'
import { StoreProvider } from 'easy-peasy'
import { store } from '../services/store.service'
import client from '../libs/apollo'
const App = ({ Component, pageProps: { session, ...pageProps } }: AppProps) => {
  return (
    <>
      {/* @ts-ignore */}
      <StoreProvider store={store}>
        <ForceSession>
          <ApolloProvider client={client as any}>
            {/* @ts-ignore */}
            <Component {...pageProps} />
          </ApolloProvider>
        </ForceSession>
      </StoreProvider>
    </>
  )
}
export default App
