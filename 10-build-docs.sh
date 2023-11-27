#!/bin/bash -e
APP_VERSION="1.0.0"

# This script contains necessary command instructions to generate documentation files.

set -eo pipefail

sleep 1

[[ "${APP_VERSION}" ]] || (echo "ERROR: APP_VERSION not set." && exit 1)

ASCIIDOC_INPUT=${ASCIIDOC_INPUT:-"./docs"}

# [[ "${CONFLUENCE_URL}" ]] && \
# [[ "${CONFLUENCE_SPACE}" ]] && \
# [[ "${CONFLUENCE_ANCESTOR}" ]] && \
# [[ "${CONFLUENCE_USERNAME}" ]] && \
# [[ "${CONFLUENCE_PASSWORD}" ]] && \
# [[ "${CONFLUENCE_BRANCH}" == "${APP_BRANCH}" || "${APP_TAG}" ]] && {
#   docker run --rm \
#   --volume "${PWD}:${PWD}" \
#   --workdir "${PWD}" \
#   --entrypoint java \
#   confluencepublisher/confluence-publisher:0.16.0 -jar /opt/asciidoc-confluence-publisher-docker.jar \
#   asciidocRootFolder="${ASCIIDOC_INPUT}" \
#   rootConfluenceUrl="${CONFLUENCE_URL}" \
#   spaceKey="${CONFLUENCE_SPACE}" \
#   ancestorId="${CONFLUENCE_ANCESTOR}" \
#   username="${CONFLUENCE_USERNAME}" \
#   password="${CONFLUENCE_PASSWORD}" \
#   versionMessage="${APP_VERSION}" \
#   publishingStrategy=REPLACE_ANCESTOR \
#   orphanRemovalStrategy=REMOVE_ORPHANS \
#   attributes='{"automation": "true", "source": "'"${APP_SOURCE}"'"}'
# }

ASCIIDOC_OUTPUT=${ASCIIDOC_OUTPUT:-"./artifacts/asciidoc"}
mkdir -p "${ASCIIDOC_OUTPUT}"

asciidoctor \
--backend html5 \
--require asciidoctor-diagram \
--out-file "${ASCIIDOC_OUTPUT}/asciidoc.html" \
--attribute "revnumber=${APP_VERSION}" \
./docs/index.adoc
asciidoctor \
--backend pdf \
--require asciidoctor-pdf \
--require asciidoctor-diagram \
--out-file "${ASCIIDOC_OUTPUT}/asciidoc.pdf" \
--attribute "revnumber=${APP_VERSION}" \
./docs/index.adoc
