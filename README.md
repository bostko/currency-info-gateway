# Getting started

## Running

1. Start rates-collector service

       ./gradlew :rates-collector:bootRun

2. Start json-api service
     
       ./gradlew :rates-api:bootRun

3. Start xml-api service

       ./gradlew :xml-api:bootRun

## Testing

### JSON API

#### Current rate

```bash
curl -s -H 'Content-Type: application/json' --data '{"requestId": "'$(uuidgen)'", "timestamp": 1586335186721,"client":"1234","currency":"EUR"}' http://localhost:8080/json_api/current
```
Must return something like this:

    {"currency":"EUR","rate":0.933179,"base":"EUR","date":"2026-04-24","timestamp":1777022095090000}

#### Historical rates

```bash
curl -s -H 'Content-Type: application/json' --data '{"requestId": "'$(uuidgen)'", "timestamp": 1586335186721,"client":"1234","currency":"EUR", "period":2}' http://localhost:8080/json_api/history
```
Must return something like this:
```json
[{"currency":"EUR","rate":0.963789,"base":"EUR","date":"2026-04-24","timestamp":1777019933020000},
  {"currency":"EUR","rate":0.979665,"base":"EUR","date":"2026-04-24","timestamp":1777019937988000},
  {"currency":"EUR","rate":0.907751,"base":"EUR","date":"2026-04-24","timestamp":1777019942988000},
  {"currency":"EUR","rate":0.974143,"base":"EUR","date":"2026-04-24","timestamp":1777019947988000}]
```

### XML API

#### Current rate

```bash
curl -s -H 'Content-Type: application/xml' --data '<command id="'$(uuidgen)'"><get consumer="13617162"><currency>GBP</currency></get></command>'  http://localhost:8081/xml_api/command | xmllint --format -
```
Must return something like this:

    <?xml version="1.0"?>
    <command id="b77e6301-2b0c-44b4-a43e-fe8465a8b84d" consumer="13617162">
      <rate>
        <currency>GBP</currency>
        <rate>0.800892</rate>
        <base>EUR</base>
        <date>2026-04-24</date>
        <timestamp>1777021687987000</timestamp>
      </rate>
    </command>

#### Historical rates
```bash
curl -s -H 'Content-Type: application/xml' --data '<command id="'$(uuidgen)'"><history consumer="13617162" currency="USD" period="24"/></command>'  http://localhost:8081/xml_api/command | xmllint --format -
```
Must return something like this:

    <?xml version="1.0"?>
    <command id="d78da96a-dea0-4b6e-8dd2-beb625809e1f" consumer="13617162">
      <rate>
        <currency>USD</currency>
        <rate>1.171487</rate>
        <base>EUR</base>
        <date>2026-04-24</date>
        <timestamp>1777019933020000</timestamp>
      </rate>
      <rate>
        <currency>USD</currency>
        <rate>1.120797</rate>
        <base>EUR</base>
        <date>2026-04-24</date>
        <timestamp>1777019937988000</timestamp>
      </rate>
      </command>