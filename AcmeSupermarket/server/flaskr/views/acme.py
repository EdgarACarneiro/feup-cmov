from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from flaskr.keys.keys import (
    sign, verify, user_key_from_bytes, load_keys
)
from ..utils import (
    generic_error_handler, gen_UUID, decode, encode, b64_decode, b64_encode
)
from flaskr.db.db import get_db
from struct import pack
import uuid

acme = Blueprint('acme', __name__)

SIGNATURE_BASE64_SIZE = 90
UUID_BASE64_SIZE = 48


@acme.route('/get-products', methods=['GET'])
def get_products():
    db = get_db()

    products = db.execute(
        'SELECT * FROM product'
    ).fetchall()

    # Converting to tag format
    encoded_prods = []
    for prod in products:
        code = uuid.UUID('{' + prod['code'] + '}')
        euros = int(prod['price'] // 100)
        cents = int(prod['price'] % 100)

        encoded_prods.append(
            b64_encode(
                pack('4s', encode('Acme')) +
                pack('16s', code.bytes) +
                pack('>i', euros) +
                pack('>i', cents) +
                pack('b', len(prod['prodName'])) +
                pack('35s', encode(prod['prodName']))
            )
        )

    # Appending signature
    encoded_prods = list(map(
        lambda prod:
            decode(
                prod + b64_encode(sign(
                    current_app.config['PRIVATE_KEY'],
                    prod
                )),
            ),
        encoded_prods
    ))

    return current_app.response_class(
        response=json.dumps(encoded_prods),
        status=200,
        mimetype='application/json'
    )


@acme.route('/get-transactions', methods=['GET'])
def get_transactions():
    return current_app.response_class(
        status=200,
        mimetype='application/json'
    )


@acme.route('/get-vouchers', methods=['POST'])
def get_vouchers():
    db = get_db()

    content = request.data[: -SIGNATURE_BASE64_SIZE]
    signature = b64_decode(request.data[-SIGNATURE_BASE64_SIZE:])

    # Checking if User exists
    uuid = decode(b64_decode(content))
    user = db.execute(
        'SELECT userPublicKey FROM user WHERE id = ?',
        (uuid, )
    ).fetchone()
    if user is None:
        abort(401)

    # Verifying User through signature
    if not verify(user_key_from_bytes(user['userPublicKey']),
                  signature,
                  content):
        abort(401)

    # Getting vouchers
    vouchers = db.execute(
        'SELECT id FROM voucher WHERE id = ? AND used = 0',
        (uuid, )
    ).fetchall()

    return current_app.response_class(
        response=json.dumps({
            'vouchers': [row['id'] for row in vouchers]
        }),
        status=200,
        mimetype='application/json'
    )


@acme.route('/checkout', methods=['POST'])
def checkout():
    db = get_db()

    content = request.data[: -SIGNATURE_BASE64_SIZE]
    signature = b64_decode(request.data[-SIGNATURE_BASE64_SIZE:])

    # Checking if User exists
    uuid = decode(b64_decode(content[0:UUID_BASE64_SIZE]))
    user = db.execute(
        'SELECT userPublicKey FROM user WHERE id = ?',
        (uuid, )
    ).fetchone()
    if user is None:
        abort(401)

    # Verifying User through signature
    if not verify(user_key_from_bytes(user['userPublicKey']),
                  signature,
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
