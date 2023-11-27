#!/bin/bash -e

# This script proxies all needed tools for CICD through trusted docker containers.

# shellcheck disable=SC2046
# shellcheck disable=SC2086

set -eo pipefail

function docker_run() {
  local DOCKER_WORK=${DOCKER_WORK:-"$(pwd)"}
  local DOCKER_HOME=${DOCKER_HOME:-"$(pwd)"}
  local DOCKER_USER=${DOCKER_USER:-"$(id -u):$(id -g)"}
  local DOCKER_NETWORK=${DOCKER_NETWORK:-"services"}
  local OPTS
  OPTS="${OPTS} --rm"
  OPTS="${OPTS} --interactive"
  OPTS="${OPTS} --env DOCKER_USER=${DOCKER_USER}"
  OPTS="${OPTS} --user ${DOCKER_USER}"
  OPTS="${OPTS} --env DOCKER_HOME=${DOCKER_HOME}"
  OPTS="${OPTS} --volume ${DOCKER_HOME}:${DOCKER_HOME}" && \
  OPTS="${OPTS} --env HOME=${DOCKER_HOME}"
  OPTS="${OPTS} --env DOCKER_WORK=${DOCKER_WORK}"
  OPTS="${OPTS} --volume ${DOCKER_WORK}:${DOCKER_WORK}" && \
  OPTS="${OPTS} --workdir ${DOCKER_WORK}"
  OPTS="${OPTS} --env DOCKER_NETWORK=${DOCKER_NETWORK}"
  docker network inspect "${DOCKER_NETWORK}" &> /dev/null && \
  OPTS="${OPTS} --network ${DOCKER_NETWORK}"
  docker run ${OPTS} "${@}"
}

function mvn() {
  local DOCKER_IMAGE="maven:3.8.5-amazoncorretto-17"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --env MAVEN_OPTS \
  --env APP_OAUTH2_ISSUER \
  --env APP_DEVICE_REST_URL \
  --env APP_VEHICLE_REST_URL \
  --entrypoint mvn \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function npm() {
  local DOCKER_IMAGE="node:16"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --env GITLAB_TOKEN \
  --entrypoint npm \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function asciidoctor() {
  local DOCKER_IMAGE="asciidoctor/docker-asciidoctor:1.3.0"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --entrypoint asciidoctor \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function asciidoctor2confluence() {
  local DOCKER_IMAGE="confluencepublisher/confluence-publisher:0.16.0"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --entrypoint java \
  "${DOCKER_IMAGE}" \
  -jar /opt/asciidoc-confluence-publisher-docker.jar \
  "${@}"
}

function base64() {
  local DOCKER_IMAGE="busybox:1.34.1"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --entrypoint base64 \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function jq() {
  local DOCKER_IMAGE="imega/jq:1.6"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --entrypoint jq \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function yq() {
  local DOCKER_IMAGE="mikefarah/yq:4"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --entrypoint yq \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function aws() {
  local DOCKER_IMAGE="conmob/aws:2.2.42-kubectl-1.22.3-helm-3.7.2-terraform-1.1.2"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --env AWS_DEFAULT_REGION \
  --env AWS_DEFAULT_OUTPUT \
  --env AWS_ACCESS_KEY_ID \
  --env AWS_SECRET_ACCESS_KEY \
  --env AWS_SESSION_TOKEN \
  --entrypoint aws \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function kubectl() {
  local DOCKER_IMAGE="conmob/aws:2.2.42-kubectl-1.22.3-helm-3.7.2-terraform-1.1.2"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --env AWS_DEFAULT_REGION \
  --env AWS_DEFAULT_OUTPUT \
  --env AWS_ACCESS_KEY_ID \
  --env AWS_SECRET_ACCESS_KEY \
  --env AWS_SESSION_TOKEN \
  --entrypoint kubectl \
  "${DOCKER_IMAGE}" \
  "${@}"
}

function helm() {
  local DOCKER_IMAGE="conmob/aws:2.2.42-kubectl-1.22.3-helm-3.7.2-terraform-1.1.2"
  docker image inspect "${DOCKER_IMAGE}" &> /dev/null ||
  docker pull "${DOCKER_IMAGE}" > /dev/null
  docker_run \
  --env HELM_EXPERIMENTAL_OCI \
  --env AWS_DEFAULT_REGION \
  --env AWS_DEFAULT_OUTPUT \
  --env AWS_ACCESS_KEY_ID \
  --env AWS_SECRET_ACCESS_KEY \
  --env AWS_SESSION_TOKEN \
  --entrypoint helm \
  "${DOCKER_IMAGE}" \
  "${@}"
}
