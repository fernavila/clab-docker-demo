# clab-docker-demo
This repo is just for testing docker

curl -v localhost:8080/api/v1/transits

curl -v localhost:8080/api/v1/transits/2

curl -X POST localhost:8080/api/v1/transits -H 'Content-type:application/json' -d '{"transitNumber": "6666", "transitType": "999"}'

curl -X PUT localhost:8080/api/v1/transits/3 -H 'Content-type:application/json' -d '{"transitNumber": "6336", "transitType": "919"}'

curl -X DELETE localhost:8080/api/v1/transits/2
