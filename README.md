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

    {"currency":"EUR","date":"2026-04-25","timestamp":1777121777084000,"rates":{"FJD":2.500586,"MXN":20.275453,"STD":24313.629281}}

#### Historical rates

```bash
curl -s -H 'Content-Type: application/json' --data '{"requestId": "'$(uuidgen)'", "timestamp": 1586335186721,"client":"1234","currency":"EUR", "period":2}' http://localhost:8080/json_api/history
```
Must return something like this:
```json
[
  {
    "currency": "EUR",
    "date": "2026-04-25",
    "timestamp": 1777113037372000,
    "rates": {
      "FJD": 2.549125,
      "MXN": 20.308071,
      "STD": 24313.62364
    }
  }
]
```

### XML API

#### Current rate

```bash
curl -s -H 'Content-Type: application/xml' --data '<command id="'$(uuidgen)'"><get consumer="13617162"><currency>GBP</currency></get></command>'  http://localhost:8081/xml_api/command | xmllint --format -
```
Must return something like this:

```xml
<?xml version="1.0"?>
<command id="b52f7419-20b1-4d4b-846b-08895e82ac3f" consumer="13617162">
  <rate>
    <currency>GBP</currency>
    <date>2026-04-25</date>
    <timestamp>1777122595319000</timestamp>
    <conversions>
      <conversion currency="FJD" rate="2.943692183163737"/>
      <conversion currency="MXN" rate="23.508527983348753"/>
      <conversion currency="STD" rate="28114.704305041625"/>
    </conversions>
  </rate>
</command>
```

#### Historical rates
```bash
curl -s -H 'Content-Type: application/xml' --data '<command id="'$(uuidgen)'"><history consumer="13617162" currency="USD" period="24"/></command>'  http://localhost:8081/xml_api/command | xmllint --format -
```
Must return something like this:

```xml
<?xml version="1.0"?>
<command id="bfa4b526-7093-4cb6-8cbd-db68a8990b87" consumer="13617162">
   <rate>
      <currency>USD</currency>
      <date>2026-04-25</date>
      <timestamp>1777113037372000</timestamp>
      <conversions>
         <conversion currency="FJD" rate="2.1941999869163578"/>
         <conversion currency="MXN" rate="17.48049590447564"/>
         <conversion currency="STD" rate="20928.339203757074"/>
      </conversions>
   </rate>
   <rate>
      <currency>USD</currency>
      <date>2026-04-25</date>
      <timestamp>1777113037372000</timestamp>
      <conversions>
         <conversion currency="FJD" rate="2.1941999869163578"/>
         <conversion currency="MXN" rate="17.48049590447564"/>
         <conversion currency="STD" rate="20928.339203757074"/>
      </conversions>
   </rate>
</command>

```