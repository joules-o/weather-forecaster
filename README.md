# Weather forecast service

## To run
```sbt run```

## Making a Request
Requests should be sent to `http://localhost:8080/weather/`, with latitude and longitude given as query parameters.
Example: `http://localhost:8080/weather/?lat=32&long=96`

## Troubleshooting
Occasionally, NWS seems to refuse requests. Usually, waiting a few seconds before retrying solves this issue. 