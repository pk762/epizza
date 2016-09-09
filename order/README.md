# epizza API


## Index

```
$ http :8082/
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 09 Sep 2016 12:23:03 GMT
Transfer-Encoding: chunked
X-Application-Context: order:default:8082

{
    "_links": {
        "pizzas": {
            "href": "http://localhost:8082/pizzas{?page,size,sort}",
            "templated": true
        },
        "profile": {
            "href": "http://localhost:8082/profile"
        }
    }
}
```


## Create an order

```
echo '{
    "orderItems": [
        {
            "pizza": "http://localhost:8082/pizzas/1",
            "amount": 2
        },
        {
            "pizza": "http://localhost:8082/pizzas/2",
            "amount": 1
        }
    ],
    "deliveryAddress": {
        "firstname": "Mathias",
        "lastname": "Dpunkt",
        "street": "Some street 99",
        "city": "Hamburg",
        "postalCode": "22222",
        "telephone": "+4940111111"
    },
    "comment": "Slice it!"
}' | http POST http://localhost:8082/orders
```

