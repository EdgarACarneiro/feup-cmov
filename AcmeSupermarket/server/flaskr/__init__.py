import os
from flask import Flask
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization

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

    if test_config is None:
        # load the instance config, if it exists, when not testing
        app.config.from_pyfile('config.py', silent=True)
    else:
        # load the test config if passed in
        app.config.from_mapping(test_config)

    # ensure the instance folder exists
    try:
        os.makedirs(app.instance_path)
        # TODO - Run the script here
        # TODO - Create keys generation as init_app thingy
    except OSError:
        pass

    # Complete with KEYS & Parsing keys for storage
    app.config.from_pyfile('config.cfg', silent=True)
    app.config['PUBLIC_KEY'] = serialization.load_pem_public_key(
        app.config['PUBLIC_KEY'],
        backend=default_backend()
    )
    app.config['PRIVATE_KEY'] = serialization.load_pem_private_key(
        app.config['PRIVATE_KEY'],
        password=None,
        backend=default_backend()
    )

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
