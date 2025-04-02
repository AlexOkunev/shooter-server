curl -X POST --data-binary "@./connectors/source/keycloak_players_source.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq

curl -X POST --data-binary "@./connectors/sink/keycloak_players_sink.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq