import base64

from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from flaskr.keys.keys import sign, verify, user_key_from_bytes, load_keys

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

    content = request.data[: -90]
    signature = base64.b64decode(request.data[-90:])

    # Checking if User exists
    uuid = bytes_to_string(base64.b64decode(content[0:48]))
    print(uuid)
    user = db.execute(
        'SELECT userPublicKey FROM user WHERE id = ?',
        (uuid, )
    ).fetchone()
    if user is None:
        abort(401)

    print(request.data)
    print("---")
    print(user['userPublicKey'])
    print("---")
    print(signature)
    print(len(signature))
    print("---")
    print(request.data[-90:])
    print("---")
    print(content)
    # Verifying User through signature
    if not verify(user_key_from_bytes(user['userPublicKey']),\
                  signature,\
                  content):
        abort(401)

    # Analyzing content

    # checkout_info = bytes_to_string(content).split(';')
    # if len(checkout_info) != 4:
    #     abort(400)

    # uuid, shop_list, voucher, discount = checkout_info

    # # Processing shop_list
    # products = {}
    # for prod in shop_list.split(','): 
    #     prod, price = prod.split('-')
    #     if prod not in products:
    #         products[prod] = {
    #             'price': price,
    #             'quantity': 1
    #         }
    #     else:
    #         products[prod]['quantity'] += 1

    # print(products)

    return current_app.response_class(
        status=200,
        mimetype='application/json'
    )

# Customized Error handlers
@acme.errorhandler(401)
def handle_unauthorized_request(e):
    return generic_error_handler(
        401, "Attempt of unauthorized access to information."
    )

@acme.errorhandler(400)
def handle_bad_request(e):
    return generic_error_handler(
        400, "Invalid data passed in request"
    )