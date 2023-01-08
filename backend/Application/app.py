from flask import Flask
from flask_bcrypt import Bcrypt
from flask_migrate import Migrate

migrate = Migrate()
bcrypt = Bcrypt()

app = Flask(__name__)
app.secret_key = 'thisisasecretkey'

migrate.init_app(app)
bcrypt.init_app(app)

