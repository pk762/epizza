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




## Pizzas

```
$ http :8082/pizzas
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Sun, 11 Sep 2016 14:26:17 GMT
Transfer-Encoding: chunked
X-Application-Context: application:8082

{
    "_embedded": {
        "pizzas": [
            {
                "_links": {
                    "pizza": {
                        "href": "http://localhost:8082/pizzas/1"
                    },
                    "self": {
                        "href": "http://localhost:8082/pizzas/1"
                    }
                },
                "description": "The classic - Pizza Salami",
                "imageUrl": "/img/salami.jpg",
                "name": "Pizza Salami",
                "price": {
                    "amount": 8.9,
                    "currency": "EUR"
                },
                "toppings": [
                    "SALAMI",
                    "CHEESE"
                ]
            },
            {
                "_links": {
                    "pizza": {
                        "href": "http://localhost:8082/pizzas/2"
                    },
                    "self": {
                        "href": "http://localhost:8082/pizzas/2"
                    }
                },
                "description": "The exotic among the classics - Pizza Hawaii",
                "imageUrl": "/img/hawaii.jpg",
                "name": "Pizza Hawaii",
                "price": {
                    "amount": 9.9,
                    "currency": "EUR"
                },
                "toppings": [
                    "HAM",
                    "PINEAPPLE",
                    "CHEESE"
                ]
            },
            {
                "_links": {
                    "pizza": {
                        "href": "http://localhost:8082/pizzas/3"
                    },
                    "self": {
                        "href": "http://localhost:8082/pizzas/3"
                    }
                },
                "description": "Take this!",
                "imageUrl": "/img/chicken.jpg",
                "name": "Chicken Pizza with Peanut Butter",
                "price": {
                    "amount": 10.9,
                    "currency": "EUR"
                },
                "toppings": [
                    "CHICKEN",
                    "CHEESE",
                    "PEANUT_BUTTER"
                ]
            },
            {
                "_links": {
                    "pizza": {
                        "href": "http://localhost:8082/pizzas/4"
                    },
                    "self": {
                        "href": "http://localhost:8082/pizzas/4"
                    }
                },
                "description": "Tuna - no dolphins - promise!",
                "imageUrl": "/img/tonno.jpg",
                "name": "Pizza Tonno",
                "price": {
                    "amount": 8.9,
                    "currency": "EUR"
                },
                "toppings": [
                    "CHEESE",
                    "TUNA"
                ]
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://localhost:8082/profile/pizzas"
        },
        "search": {
            "href": "http://localhost:8082/pizzas/search"
        },
        "self": {
            "href": "http://localhost:8082/pizzas"
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 4,
        "totalPages": 1
    }
}
```



```
$ http :8082/pizzas/1
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Sun, 11 Sep 2016 14:28:25 GMT
Transfer-Encoding: chunked
X-Application-Context: application:8082

{
    "_links": {
        "pizza": {
            "href": "http://localhost:8082/pizzas/1"
        },
        "self": {
            "href": "http://localhost:8082/pizzas/1"
        }
    },
    "description": "The classic - Pizza Salami",
    "imageUrl": "/img/salami.jpg",
    "name": "Pizza Salami",
    "price": {
        "amount": 8.9,
        "currency": "EUR"
    },
    "toppings": [
        "SALAMI",
        "CHEESE"
    ]
}
```

## Create an order

```
echo '{
    "lineItems": [
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


## Retrieve all orders

```
$ http :8082/orders
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 09 Sep 2016 13:49:21 GMT
Transfer-Encoding: chunked
X-Application-Context: order:default:8082

{
    "_embedded": {
        "orderResources": [
            {
                "_links": {
                    "self": {
                        "href": "http://localhost:8082/orders/1"
                    }
                },
                "comment": null,
                "created": "2016-09-09T15:48:58.15",
                "deliveryAddress": {
                    "city": "Hamburg",
                    "email": null,
                    "firstname": "Mathias",
                    "lastname": "Dpunkt",
                    "postalCode": "22222",
                    "street": "Some street 99",
                    "telephone": "+4940111111"
                },
                "estimatedTimeOfDelivery": null,
                "orderItems": [
                    {
                        "amount": 2,
                        "pizza": "http://localhost:8082/pizzas/1",
                        "price": {
                            "amount": 17.8,
                            "currency": "EUR"
                        }
                    },
                    {
                        "amount": 1,
                        "pizza": "http://localhost:8082/pizzas/2",
                        "price": {
                            "amount": 9.9,
                            "currency": "EUR"
                        }
                    }
                ],
                "status": "NEW",
                "totalPrice": {
                    "amount": 45.5,
                    "currency": "EUR"
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8082/orders"
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1
    }
}
```


## Retrieve a single order

```
$ http :8082/orders/1
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 09 Sep 2016 13:54:02 GMT
Transfer-Encoding: chunked
X-Application-Context: order:default:8082

{
    "_links": {
        "self": {
            "href": "http://localhost:8082/orders/1"
        }
    },
    "comment": null,
    "created": "2016-09-09T15:51:56.817",
    "deliveryAddress": {
        "city": "Hamburg",
        "email": null,
        "firstname": "Mathias",
        "lastname": "Dpunkt",
        "postalCode": "22222",
        "street": "Some street 99",
        "telephone": "+4940111111"
    },
    "estimatedTimeOfDelivery": null,
    "orderItems": [
        {
            "amount": 2,
            "pizza": "http://localhost:8082/pizzas/1",
            "price": {
                "amount": 17.8,
                "currency": "EUR"
            }
        },
        {
            "amount": 2,
            "pizza": "http://localhost:8082/pizzas/1",
            "price": {
                "amount": 17.8,
                "currency": "EUR"
            }
        },
        {
            "amount": 1,
            "pizza": "http://localhost:8082/pizzas/2",
            "price": {
                "amount": 9.9,
                "currency": "EUR"
            }
        },
        {
            "amount": 1,
            "pizza": "http://localhost:8082/pizzas/2",
            "price": {
                "amount": 9.9,
                "currency": "EUR"
            }
        },
        {
            "amount": 1,
            "pizza": "http://localhost:8082/pizzas/2",
            "price": {
                "amount": 9.9,
                "currency": "EUR"
            }
        }
    ],
    "status": "NEW",
    "totalPrice": {
        "amount": 65.3,
        "currency": "EUR"
    }
}
```
