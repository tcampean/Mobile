import 'package:flutter/material.dart';
import 'package:labflutter/listcomponent.dart';
import 'package:labflutter/storage.dart';
import 'package:provider/provider.dart';


class MainPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    final products = Provider.of<Storage>(context);
    products.init();
    return ProductList(products.products);
  }
}