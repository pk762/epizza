curl -v -X POST -H "Content-Type: application/json" -d '{
  "comment" : "Some comment",
  "deliveryAddress" : {
    "firstname" : "Mathias",
    "lastname" : "Dpunkt",
    "street" : "Somestreet 1",
    "city" : "Hamburg",
    "telephone" : "+49404321343",
    "postalCode" : "22305"
  },
  "lineItems" : [ {
    "quantity" : 1,
    "pizza" : "http://$DOCKER_IP:8082/pizzas/1"
  },
  {
    "quantity" : 2,
    "pizza" : "http://$DOCKER_IP:8082/pizzas/2"
  }]
}' http://$DOCKER_IP:8082/orders