# Country Tour API

## Usage
### Example
Request a tour with starting country: Bulgaria (BG) • Budget per country: 100 • Total budget: 1200 • Currency: EUR
```shell
curl -s "http://localhost:8082/tour?country=BG&currency=EUR&budgetPerCountry=100&totalBudget=1200" | jq .
```
Response should look like:
```json
{
  "startingCountry": "BG",
  "currency": "EUR",
  "totalBudget": 1200.0,
  "budgetPerCountry": 100.0,
  "neighborCount": 5,
  "completeTours": 2,
  "leftover": 200.0,
  "countryBudgets": [
    {
      "country": "TR",
      "currency": "TRY",
      "amount": 5269.1828000000005
    },
    {
      "country": "GR",
      "currency": "GBP",
      "amount": 77.1479
    },
    {
      "country": "MK",
      "currency": "MKD",
      "amount": 6155.4353
    },
    {
      "country": "SR",
      "currency": "RSD",
      "amount": 11730.366
    },
    {
      "country": "RO",
      "currency": "RON",
      "amount": 509.1349
    }
  ]
}
```