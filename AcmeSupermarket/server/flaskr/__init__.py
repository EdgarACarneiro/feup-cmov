import os
from flask import Flask
from .db import db
from .views.auth import auth
from .utils import generic_error_handler


def create_app(test_config=None):
    # create and configure the app
    app = Flask(__name__, instance_relative_config=True)

    # Initial Mapping
    app.config.from_mapping(
        DATABASE=os.path.join(app.instance_path, 'flaskr.sqlite'),
    )
    # Complete with KEYS
    app.config.from_pyfile('config.cfg', silent=True)


    if test_config is None:
        # load the instance config, if it exists, when not testing
        app.config.from_pyfile('config.py', silent=True)
    else:
        # load the test config if passed in
        app.config.from_mapping(test_config)

    # ensure the instance folder exists
    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass

    db.init_app(app)

    # Bluprints
    app.register_blueprint(auth)

    # a simple page that says hello
    @app.route('/hello')
    def hello():
        return 'Hello, World!'

    app.register_error_handler(
        500, lambda _: generic_error_handler(
            500, 'Something went wrong. Please try again later.'
        )
    )

    return app
