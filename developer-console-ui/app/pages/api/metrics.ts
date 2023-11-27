import { NextApiRequest, NextApiResponse } from 'next'
import { register, collectDefaultMetrics } from 'prom-client'

collectDefaultMetrics()

const metrics = async (req: NextApiRequest, res: NextApiResponse) => {
  res.setHeader('Content-type', register.contentType)
  res.send(await register.metrics())
}

export default metrics
