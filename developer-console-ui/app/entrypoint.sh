#!/bin/bash -e

set -e
set +x

[[ "${APP_NAME}" ]] && \
[[ "${VAULT_ADDR}" ]] && \
[[ "${VAULT_TOKEN}" ]] && \
[[ "${VAULT_MOUNT}" ]] && {
  VAULT_OAUTH2_ISSUER=$(vault kv get -field app.oauth2.issuer "${VAULT_MOUNT}/${APP_NAME}/oauth2" || echo "")
  [[ "${VAULT_OAUTH2_ISSUER}" ]] && export APP_OAUTH2_ISSUER="${VAULT_OAUTH2_ISSUER}"
  VAULT_OAUTH2_CLIENT_ID=$(vault kv get -field app.oauth2.client.id "${VAULT_MOUNT}/${APP_NAME}/oauth2" || echo "")
  [[ "${VAULT_OAUTH2_CLIENT_ID}" ]] && export APP_OAUTH2_CLIENT_ID="${VAULT_OAUTH2_CLIENT_ID}"
  VAULT_OAUTH2_CLIENT_SECRET=$(vault kv get -field app.oauth2.client.secret "${VAULT_MOUNT}/${APP_NAME}/oauth2" || echo "")
  [[ "${VAULT_OAUTH2_CLIENT_SECRET}" ]] && export APP_OAUTH2_CLIENT_SECRET="${VAULT_OAUTH2_CLIENT_SECRET}"
}

exec "${@}"
