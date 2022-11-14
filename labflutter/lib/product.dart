import 'package:uuid/uuid.dart';

class Product {
  final String id = const Uuid().v4();
  String name;
  String type;
  String description;
  String quantity;
  String price;

  Product({
    required this.name,
    required this.type,
    required this.description,
    required this.quantity,
    required this.price
  });
}
