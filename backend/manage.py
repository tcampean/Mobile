import datetime


def deploy():
    from Application.app import app
    from Application.database import database
    from Application.Model import Product

    app.app_context().push()
    database.create_all()



deploy()
