import { Link } from "../libs/apollo"
import { GET_SCENARIO } from "./queries"

export const onSubmit = async (userName: any, pass: any, setUser: Function, router: any) => {
    {
       
        let username = userName.current
        let password = pass.current
        var decodedStringBtoA = `${username}:${password}`
        var encodedStringBtoA = Buffer.from(decodedStringBtoA).toString('base64') 
        localStorage.setItem('token', encodedStringBtoA);
        localStorage.setItem('user', userName.current);
        const token = localStorage.getItem('token');
        return fetch(Link, {
            method: 'POST',
            headers: {
                'content-type': 'application/json',
                'Authorization': token ? `Basic ${token}` : "",
            },
            body: JSON.stringify({
                query: GET_SCENARIO,
                variables: {
                    scenarioPattern: '',
                    page: 0,
                    size: 10,
                },
            }),
        })
            .then((res) => res.json())
            .then((result) =>result)
            .catch((error) => {
                router.replace('/error')
                localStorage.removeItem('token')
                setUser(false)
            })
    }
}