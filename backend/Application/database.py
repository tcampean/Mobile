from flask_sqlalchemy import SQLAlchemy

from Application.app import app

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///HEALTH.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

database = SQLAlchemy(app)
