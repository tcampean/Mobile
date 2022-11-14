import 'package:flutter/cupertino.dart';

import 'product.dart';


class Storage extends ChangeNotifier {
  List<Product> _products = [];

  List<Product> get products => _products;

  set products(List<Product> products) {
    _products = products;
    notifyListeners();
  }

  List<Product> init() {
    if(_products.isEmpty) {
      _products.add(Product(name: "Chocolate Cupcake",
          type: "Cupcake",
          description: "Delicious cupcakes with chocolate chips and chocolate cream filling",
          quantity: "10",
          price: "1.99"));
      _products.add(Product(name: "Garlic Bread",
          type: "Bakery",
          description: "Tasteful bread with scrumptious garlic bits",
          quantity: "20",
          price: "5.33"));
      _products.add(Product(name: "Strawberry Cake",
          type: "Cake",
          description: "Sweet cake with strawberries and vanilla filling",
          quantity: "20",
          price: "23.33"));
      _products.add(Product(name: "Vanilla Cookie",
          type: "Cookie",
          description: "Amazing cookie with vanilla bits and crusty exterior",
          quantity: "20",
          price: "20.33"));
      notifyListeners();
    }
    return _products;
  }
}