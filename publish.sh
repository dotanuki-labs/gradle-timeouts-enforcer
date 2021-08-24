#!/usr/bin/env bash

set -e

readonly local_release="local"
readonly portal_release="portal"

readonly release_mode="$1"

validate_release() {
    echo
    echo "🔥 Validating release ..."
    echo

    local portal_ready="yes"

    if [[ -z "$TAG" ]]; then
        echo " ‣ Could not detect 'TAG' environment variable"
        echo " ‣ Release version will default to 'SNAPSHOT' according build conventions"
        portal_ready="no"
        echo
    fi

    if [[ -z "$API_KEY" ]]; then
        echo " ‣ Could not detect 'API_KEY' environment variable"
        portal_ready="no"
    fi

    if [[ -z "$API_SECRET" ]]; then
        echo " ‣ Could not detect 'API_SECRET' environment variable"
        portal_ready="no"
    fi

    if [[ "$portal_ready" == "no" && "$1" == "portal" ]]; then
        echo
        echo " ‣ Missing parameters required to publish on Gradle Plugins Portal"
        echo " 𝙭 Aborting"
        echo
        exit 1
    fi

    echo
    echo "🔥 Running publishing to : $release_mode"
    echo
}

publish_to_maven_local() {
    validate_release "$local_release"
    ./gradlew clean publishToMavenLocal -Ptag=$TAG
}

publish_to_gradle_portal() {
    validate_release "$portal_release"
    ./gradlew clean publishPlugins \
        -Pgradle.publish.key=$API_KEY \
        -Pgradle.publish.secret=$API_SECRET \
        -Ptag=$TAG \
        --stacktrace
}

case "$release_mode" in
"$local_release")
    publish_to_maven_local
    ;;
"$portal_release")
    publish_to_gradle_portal
    ;;
*)
    echo
    echo "Error: unsupported release mode → $release_mode"
    echo "Should pick one from : 'local' or 'portal'"
    echo
    exit 1
    ;;
esac
