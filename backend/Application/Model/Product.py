from Application.database import database


class Product(database.Model):
    __tablename__ = "Product"
    __table_args__ = {'extend_existing': True}

    id = database.Column(database.Integer, primary_key=True)
    name = database.Column(database.String, nullable=False)
    type = database.Column(database.String, nullable=False)
    description = database.Column(database.String, nullable=False)
    quantity = database.Column(database.Integer, nullable=False)
    price = database.Column(database.Float, nullable=False)

    def __init__(self, name, type, description, quantity, price, id=None):
        self.name = name
        self.type = type
        self.description = description
        self.quantity = quantity
        self.price = price
        if id:
            self.id = id

    def __str__(self):
        return str(self.id) + "," + str(self.name) + "," + self.type + "," + self.description + "," + str(self.quantity) + "," + str(self.price)

    def __repr__(self):
        return str(self.id) + "," + str(self.name) + "," + self.type + "," + self.description + "," + str(self.quantity) + "," + str(self.price)

    def get_id(self):
        return self.id_appointment

    def serialize(self):
        return {
            "id": str(self.id),
            "name": self.name,
            "type": self.type,
            "description": self.description,
            "quantity": str(self.quantity),
            "price": str(self.price),
        }
