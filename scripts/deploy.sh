#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR=$(cd "$(dirname "$0")/.." && pwd)

pushd "$ROOT_DIR" >/dev/null
echo "[ShopSense] Building microservices..."
./gradlew clean bootJar

echo "[ShopSense] Launching Docker Compose stack..."
PROFILE_ARGS=()
if [[ -n "${DOCKER_PROFILES:-}" ]]; then
  IFS=',' read -ra _profiles <<< "${DOCKER_PROFILES}"
  for profile in "${_profiles[@]}"; do
    [[ -n "${profile}" ]] && PROFILE_ARGS+=(--profile "${profile}")
  done
fi
docker compose "${PROFILE_ARGS[@]}" up -d --build
popd >/dev/null

echo "Deployment kicked off. Hit https://localhost/swagger once containers are healthy."
