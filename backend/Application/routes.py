import datetime

from flask import session

from Application.app import app
from Application.database import database

@app.before_first_request
def session_handler():
    database.create_all()
    session.permanent = True
    app.permanent_session_lifetime = datetime.timedelta(minutes=30)

