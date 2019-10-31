from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from flaskr.keys.keys import sign, verify

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

@acme.route('/checkout', methods=['POST'])
def checkout():
    db = get_db()

    content = list(request.form.keys())[0]
    # signature = content[-64:]
    # message = bytes_to_string(content[: -64])
    uuid = str(content[:4])

    user = db.execute(
        'SELECT userPublicKey FROM user WHERE id = ?',
        (uuid, )
    ).fetchone()
    user['userPublicKey']

    # if verify()

    return current_app.response_class(
        status=200,
        mimetype='application/json'
    )