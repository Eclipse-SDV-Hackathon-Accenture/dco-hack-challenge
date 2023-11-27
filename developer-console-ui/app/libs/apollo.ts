import { ApolloClient, HttpLink, InMemoryCache } from '@apollo/client'
import { setContext } from '@apollo/client/link/context';
import getConfig from 'next/config';


const httpLink = new HttpLink({ uri: getConfig().publicRuntimeConfig.url + '/graphql', fetch: fetch });

export const Link = getConfig().publicRuntimeConfig.url + '/graphql'

const authLink = setContext((_, { headers }) => {
  // get the authentication token from local storage if it exists
  const token = localStorage.getItem('token');
  // return the headers to the context so httpLink can read them
  return {
    headers: {
      ...headers,
      'Authorization': token ? `Basic ${token}` : "",
    }
  }
});

const client = new ApolloClient({
  link: authLink.concat(httpLink),
  cache: new InMemoryCache()
});

export default client
