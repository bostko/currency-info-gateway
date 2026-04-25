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
<command id="f7c9a53c-2432-4002-bbd8-523c9fb45a84" consumer="13617162">
   <rate>
      <currency>USD</currency>
      <date>2026-04-25</date>
      <timestamp>1777127673546</timestamp>
      <conversions>
         <conversion currency="FJD" rate="2.329382784103633"/>
         <conversion currency="MXN" rate="18.42529391125226"/>
         <conversion currency="STD" rate="22043.807725330447"/>
         <conversion currency="LVL" rate="0.5716189666255352"/>
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
<command id="f7c9a53c-2432-4002-bbd8-523c9fb45a84" consumer="13617162">
   <rate>
      <currency>USD</currency>
      <date>2026-04-25</date>
      <timestamp>1777127673546</timestamp>
      <conversions>
         <conversion currency="FJD" rate="2.329382784103633"/>
         <conversion currency="MXN" rate="18.42529391125226"/>
         <conversion currency="STD" rate="22043.807725330447"/>
         <conversion currency="LVL" rate="0.5716189666255352"/>
         <conversion currency="SCR" rate="14.922438436619705"/>
      </conversions>
   </rate>
   <rate>
      <currency>USD</currency>
      <date>2026-04-25</date>
      <timestamp>1777127678473</timestamp>
      <conversions>
         <conversion currency="FJD" rate="2.139873099662708"/>
         <conversion currency="MXN" rate="17.527956949392436"/>
         <conversion currency="STD" rate="20957.768534026134"/>
         <conversion currency="LVL" rate="0.5846388701887645"/>
      </conversions>
   </rate>
</command>

```