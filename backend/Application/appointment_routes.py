import datetime
import logging
from flask import request, jsonify, Blueprint, make_response
from flask_cors import cross_origin, CORS

from Application.Model.Product import Product
from Application.database import database

products_api = Blueprint('products_api', __name__)

@cross_origin()
@products_api.route('/products', methods=["GET", "POST"])
def index():
    logging.debug("Entered on /products route with method " + request.method)
    if request.method == "GET":
        return jsonify([product.serialize() for product in Product.query])

    elif request.method == "POST":
        new_product = request.json
        if not Product.query.filter_by(name=new_product["name"]).all() == []:
            logging.debug("Attempted to add product which already exists")
            return make_response("Already existing", 400)
        product = Product(new_product["name"], new_product["type"], new_product["description"], int(new_product["quantity"]), float(new_product["price"]))
        database.session.add(product)
        database.session.commit()
        logging.debug("Added " + product + " to the database")
        return jsonify(Product.query.filter_by(name=product.name).first().serialize())


@cross_origin()
@products_api.route('/productsid', methods=["GET", "POST"])
def index3():
    logging.debug("Entered on /productsid route with method " + request.method)
    if request.method == "POST":
        new_product = request.json
        if not Product.query.filter_by(name=new_product["name"]).all() == []:
            logging.debug("Attempted to add product which already exists")
            return make_response("Already existing", 400)
        product = Product(new_product["name"], new_product["type"], new_product["description"], int(new_product["quantity"]), float(new_product["price"]), int(new_product['id']))
        database.session.add(product)
        database.session.commit()
        logging.debug("Added " + product + " to the database")
        return jsonify(Product.query.filter_by(name=product.name).first().serialize())


@cross_origin()
@products_api.route('/products/<id>', methods=["DELETE", "PUT"])
def index2(id):
    logging.debug("Entered on /products/<id> route with method " + request.method)
    if request.method == "DELETE":
        Product.query.filter_by(id=int(id)).delete()
        logging.debug("Deleted product with id " + id)
        database.session.commit()
        return jsonify(request.json)

    elif request.method == "PUT":
        new_product = request.json
        product = Product.query.filter_by(id=int(id)).first()
        if product.name != new_product["name"] and not Product.query.filter_by(name=new_product["name"]).all() == []:
            logging.debug("Attempted to update product with the name of another product")
            return make_response("Already existing", 400)
        product.name = new_product["name"]
        product.type = new_product["type"]
        product.description = new_product["description"]
        product.quantity = int(new_product["quantity"])
        product.price = float(new_product["price"])
        database.session.commit()
        logging.debug("New product is " + product)
        return jsonify(request.json), 200
