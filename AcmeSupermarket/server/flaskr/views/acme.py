from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from flaskr.keys.keys import sign

acme = Blueprint('acme', __name__)

from ..utils import generic_error_handler, gen_UUID, bytes_to_string
from flaskr.db.db import get_db

@acme.route('/get-products', methods=['GET'])
def get_products():
    db = get_db()

    products = db.execute(
        'SELECT * FROM product'
    ).fetchall()
    # Converting rows to dictionaries
    products = [
        dict(prod)
        for prod in products
    ]

    return current_app.response_class(
        response=sign(
            current_app.config['PRIVATE_KEY'],
            str(products)
        ),
        status=200,
        mimetype='application/json'
    )