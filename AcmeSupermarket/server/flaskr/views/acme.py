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

    content = request.data
    print(content)

    content = request.data[: -64]
    uuid = bytes_to_string(content[36:])
    signature = content[-64:]
    user = db.execute(
        'SELECT userPublicKey FROM user WHERE id = ?',
        (uuid, )
    ).fetchone()
    print(user['userPublicKey'])

    # if verify()

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