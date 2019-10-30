import os
from flask import Flask
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization

from .db import db
from .views.auth import auth
from .utils import generic_error_handler
from .keys import keys


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
        # Generate Supermarket keys
        keys.init_keys(app.instance_path)
    except OSError:
        pass

    # Complete with KEYS for storage
    app.config['PRIVATE_KEY'], app.config['PUBLIC_KEY'] =\
        keys.load_keys(app.instance_path)

    # Adding new commands to app cli
    db.init_app(app)
    keys.init_app(app)

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
