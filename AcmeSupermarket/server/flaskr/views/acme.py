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
import sys

acme = Blueprint('acme', __name__)

SIGNATURE_BASE64_SIZE = 90
UUID_ENCODED_SIZE = 36
INTEGER_SIZE = 4
PRODUCT_SIZE = 4 + 36 + 4 + 4


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
    decoded_content = b64_decode(content)
    signature = b64_decode(request.data[-SIGNATURE_BASE64_SIZE:])

    # Acme tag
    _ = decoded_content[0: INTEGER_SIZE]
    decoded_content = decoded_content[INTEGER_SIZE:]

    # Checking if User exists
    uuid = decode(decoded_content[:UUID_ENCODED_SIZE])
    decoded_content = decoded_content[UUID_ENCODED_SIZE:]
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

    # Extract voucher and discont - TODO check
    discont = int.from_bytes(decoded_content[-1:], sys.byteorder) == 1
    decoded_content = decoded_content[:-1]

    voucher_id = int.from_bytes(decoded_content[-INTEGER_SIZE:], sys.byteorder)
    decoded_content = decoded_content[:-INTEGER_SIZE]

    print(discont)
    print(voucher_id)

    # Extractinf products
    products = {}

    for i in range(0, len(decoded_content) // PRODUCT_SIZE):
        base = i * PRODUCT_SIZE
        prod = decoded_content[base: base + PRODUCT_SIZE]

        # Acme tag
        _ = prod[0: INTEGER_SIZE]
        prod = prod[INTEGER_SIZE:]

        # Product Code
        code = decode(prod[:UUID_ENCODED_SIZE])
        prod = prod[UUID_ENCODED_SIZE:]
        if code in products:
            products[code] += 1
        else:
            products[code] = 1

        # Prices, maybe do validation
        prod = prod[2 * INTEGER_SIZE:]

    print(products)


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
