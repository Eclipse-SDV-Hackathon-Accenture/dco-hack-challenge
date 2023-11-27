/** @type {import('next').NextConfig} */
module.exports = {
  reactStrictMode: true,
  publicRuntimeConfig: {
    url: process.env.APP_DCO_GATEWAY_SERVICE_URL,
  },
}
