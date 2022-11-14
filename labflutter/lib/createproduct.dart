import 'package:flutter/cupertino.dart';
import 'package:labflutter/product.dart';
import 'package:provider/provider.dart';
import 'dart:io';

import 'storage.dart';


class CreateProductView extends StatefulWidget {
  const CreateProductView({super.key, this.product});

  final Product? product;

  @override
  State<StatefulWidget> createState() => CreateProductViewState();
}

class CreateProductViewState extends State<CreateProductView> {
  final TextEditingController nameController = TextEditingController();
  final TextEditingController descriptionController = TextEditingController();
  final TextEditingController typeController = TextEditingController();
  final TextEditingController quantityController = TextEditingController();
  final TextEditingController priceController = TextEditingController();

  @override
  void initState() {
    super.initState();
    if (widget.product != null) {
      nameController.text = widget.product!.name;
      descriptionController.text = widget.product!.description;
      typeController.text = widget.product!.type;
      quantityController.text = widget.product!.quantity.toString();
      priceController.text = widget.product!.price.toString();
    }
  }

  void create(Storage storage) async {
    if (nameController.text == "" ||
        descriptionController.text == "" ||
        typeController.text == "" ||
        quantityController.text == "" ||
        priceController.text == ""
    ) {
      showCupertinoDialog(
        context: context,
        builder: (context) => CupertinoAlertDialog(
          title: const Text("Error"),
          content: const Text("All fields required"),
          actions: [
            CupertinoDialogAction(
              child: const Text("Ok"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        ),
      );
      return;
    }

    if (widget.product == null) {
      storage.products = [
        ...storage.products,
        Product(
          name: nameController.text,
          description: descriptionController.text,
          type: typeController.text,
          quantity: quantityController.text,
          price: priceController.text
        ),
      ];
    } else {
      widget.product!.name = nameController.text;
      widget.product!.description = descriptionController.text;
      widget.product!.type = typeController.text;
      widget.product!.quantity = quantityController.text;
      widget.product!.price = priceController.text;
      Navigator.of(context).pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    final storage = Provider.of<Storage>(context);

    return CupertinoPageScaffold(
      child: NestedScrollView(
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          return <Widget>[
            CupertinoSliverNavigationBar(
              largeTitle: Text(widget.product == null ? 'Create' : "Edit"),
            )
          ];
        },
        body: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                    child: Column(
                      children: [
                        CupertinoTextField(
                          placeholder: "Name",
                          controller: nameController,
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        CupertinoTextField(
                          placeholder: "Description",
                          controller: descriptionController,
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        CupertinoTextField(
                          placeholder: "Type",
                          controller: typeController,
                        ),
                        CupertinoTextField(
                          placeholder: "Quantity",
                          controller: quantityController,
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        CupertinoTextField(
                          placeholder: "Price",
                          controller: priceController,
                        ),
                      ],
                    ),
                  )
                ],
              ),
              CupertinoButton.filled(
                child: Text(
                  widget.product == null ? 'Create' : "Edit",
                  style: const TextStyle(color: CupertinoColors.white),
                ),
                onPressed: () {
                  create(storage);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
