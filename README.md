# Server_Http


curl -r GET http://localhost:8080
curl -r GET http://localhost:8080/tmp.json
curl --header "Content-Type: application/json" --request POST --data {\"username\":\"xyz\",\"password\":\"xyz\"} http://localhost:8080/data-0.json
curl --header "Content-Type: application/json" --request POST --data {\"username\":\"xyz\",\"password\":\"xyz\"} http://localhost:8080/data-0.json?overwrite=true
curl --header "Content-Type: application/json" --request POST --data {\"username\":\"xyz\",\"password\":\"xyz\"} http://localhost:8080//\..\data-0.json?overwrite=true

-v -p 8080 -d <path to storage>