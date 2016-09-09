[ ![Build Status](https://travis-ci.org/ePages-de/rnd-microservices-handson.svg)](https://travis-ci.org/ePages-de/rnd-microservices-handson)

# rnd-microservices-handson

RnD Day: Microservices Hands-On

# Goals

- experience core principles of Microservices architecture
  - isolated runtimes
  - Java & Spring Boot technology stack
  - persistence with JPA
  - exposes synchronous REST/JSON endpoints
  - consumes external synchronous REST/JSON endpoints
  - sends asynchronous events over message bus
  - receives asynchronous events over message bus

# Use Case

This project contains a collection of four microservices
based on spring-boot, communicating via REST and AMQP.
The "start-group-delivery" and "start-group-bakery"
branches contain the starting points for implementing
two services. The "ref" branch contains the reference
implementations. Avoid peeking here without trying yourself
first. :-)

The folder pizza-service-intro/index.html contains a presentation that gives an overview of the application.

The exercise branches "start-group-delivery" and "start-group-bakery" contain a presentation explaining the tasks (delivery/exercise-instructions/index.html or bakery/exercise-instructions/).

# Run the solution

We dockerized our microservices and you can use docker-compose to run the solution.

You need to install [docker](http://docs.docker.com/mac/started/) and [docker-compose](https://docs.docker.com/compose/install/). If you are not on linux you also need [docker-machine](https://docs.docker.com/machine/install-machine/).


Start the event bus:
```
infrastructure/start.sh
```

Start the microservices:
```
./service.sh
```

In order to point the ui to the correct backend you might need to change the following lines to point to your docker host ip in the docker-compose.yml
```
- CATALOG_API_BACKEND_HOST=http://192.168.99.100:8081
- ORDER_API_BACKEND_HOST=http://192.168.99.100:8082
```

On linux your docker host ip should be localhost. if you run docker machine you can find the ip by running:
```
docker-machine env <name-of-docker-vm>
```
## The UI

You should be able to reach the UI under
```
http://<docker-ip>:8085
```

## Interaction with the system via REST

All the service have an index endpoint that tells you which resources they expose

e.g. the order service:
```
curl -X GET 'http://192.168.99.100:8082'
```

The result is this - it just manages an orders resource:
```json
{
  "_links": {
    "self": {
      "href": "http://192.168.99.100:8082/",
      "templated": false
    },
    "orders": {
      "href": "http://192.168.99.100:8082/orders",
      "templated": false
    }
  }
}
```

Catalog service:
```
curl -X GET 'http://192.168.99.100:8081'
```

Bakery service:
```
curl -X GET 'http://192.168.99.100:8083'
```

Delivery service:
```
curl -X GET 'http://192.168.99.100:8081'
```

### Post an order
```
curl -X POST -H "Content-Type: application/json" -d '{
    "orderItems": [{
            "pizza": "http://192.168.99.100:8081/pizzas/1",
            "amount": 2
        },
        {
            "pizza": "http://192.168.99.100:8081/pizzas/2",
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
}' 'http://192.168.99.100:8082/orders'
```

The location header of the response contains the URI of the order.

### Get an order
```
curl -X GET 'http://192.168.99.100:8082/orders/1'
```

This will give you something like this - you should see the status change until it reaches DELIVERED.

```json
{
  "status": "DELIVERED",
  "created": "2015-09-25T08:09:53.409",
  "estimatedTimeOfDelivery": "2015-09-25T08:10:28.933",
  "totalPrice": "EUR 27.70",
  "orderItems": [
    {
      "pizza": "http://192.168.99.100:8081/pizzas/1",
      "amount": 2,
      "price": "EUR 8.90"
    },
    {
      "pizza": "http://192.168.99.100:8081/pizzas/2",
      "amount": 1,
      "price": "EUR 9.90"
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
  "_links": {
    "self": {
      "href": "http://192.168.99.100:8082/orders/1",
      "templated": false
    }
  }
}
```

### Get the bakery and delivery status
You can also query the bakery and deliver service for their status of the order.

```
curl -X GET 'http://192.168.99.100:8083/bakery-orders'
```

```
curl -X GET 'http://192.168.99.100:8084/delivery-orders'
```

```json
{
  "_links": {
    "self": {
      "href": "http://192.168.99.100:8084/delivery-orders",
      "templated": false
    }
  },
  "_embedded": {
    "deliveryOrders": [
      {
        "orderLink": "http://192.168.99.100:8082/orders/1",
        "deliveryOrderState": "DONE"
      }
    ]
  },
  "page": {
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

# Where to go next

If you are interested in the topic and want to go further you can have a look at the following good reads:

## Concepts

- [Sam Newman's "Building Microservices"](http://samnewman.io/books/building_microservices/) is an excellent read on microservices and the challenges and benefits associated with it
- [Martin Fowler's "Microservices Resource Guide"](http://martinfowler.com/microservices/)
- ["Richardson Maturity Model - steps toward the glory of REST" by Martin Fowler](http://martinfowler.com/articles/richardsonMaturityModel.html)
- [Patterns of Enterprise Application Architecture](http://martinfowler.com/books/eaa.html)

## Technology

- [Building Microservices with Spring Boot](http://www.infoq.com/articles/boot-microservices)
- [https://spring.io/guides/tutorials/bookmarks/](https://spring.io/guides/tutorials/bookmarks/)
- [RabbitMQ tutorials which nicely illustrates the concepts and features of RabbitMQ](https://www.rabbitmq.com/getstarted.html)
