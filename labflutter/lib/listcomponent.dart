import 'dart:io';
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'product.dart';
import 'storage.dart';

class ProductList extends StatelessWidget {
  final List<Product> products;

  const ProductList(this.products, {super.key});

  void delete(BuildContext context, Storage storage, Product event) {
    showCupertinoDialog(
      context: context,
      builder: (context) => CupertinoAlertDialog(
        title: const Text("Confirm"),
        content: const Text("Are you sure you want to delete?"),
        actions: [
          CupertinoDialogAction(
            child: const Text("No"),
            onPressed: () {
              Navigator.of(context).pop();
            },
          ),
          CupertinoDialogAction(
            child: const Text("Yes"),
            onPressed: () {
              storage.products = storage.products
                  .where((element) => element.id != event.id)
                  .toList();
              Navigator.of(context).pop();
            },
          ),
        ],
      ),
    );
  }

  void update(Storage storage, Product product) {
    storage.products = [...storage.products];
  }

  @override
  Widget build(BuildContext context) {
    final storage = Provider.of<Storage>(context);

    return SizedBox(
      height: 400,
      child: ListView.separated(
        itemCount: products.length,
        itemBuilder: (context, index) {
          final product = products[index];
          return Container(
            width: 150,
            height: 200,
            color: Colors.grey,
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        children: [
                          const Spacer(),
                          GestureDetector(
                            child: const Icon(
                              CupertinoIcons.pencil,
                              size: 40,
                            ),
                            onTap: () => Navigator.of(context).push(
                              CupertinoPageRoute(
                                builder: (context) => ChangeNotifierProvider.value(
                                  value: storage,
                                  child: null,
                                ),
                              ),
                            ),
                          ),
                          GestureDetector(
                            child: const Icon(
                              CupertinoIcons.trash,
                              size: 40,
                            ),
                            onTap: () => delete(context, storage, product),
                          ),
                        ],
                      ),
                      const Spacer(),
                      ClipRect(
                        child: BackdropFilter(
                          filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
                          child: Padding(
                            padding: const EdgeInsets.all(5),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.stretch,
                              children: [
                                Text(
                                  product.name,
                                  style: const TextStyle(
                                    fontSize: 25,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                Text(
                                  product.description
                                ),
                                Text("Type: " + product.type),
                                Text("Quantity: " + product.quantity.toString()),
                                Text("Price: " + product.price.toString())
                              ],
                            ),
                          ),
                        ),
                      )
                    ],
                  ),
                ),
              ],
            ),
          );
        },
        scrollDirection: Axis.vertical,
        separatorBuilder: (context, index) => const SizedBox(
          width: 15,
        ),
      ),
    );
  }
}
