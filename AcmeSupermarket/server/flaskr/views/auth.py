import functools

from array import array
from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from werkzeug.security import check_password_hash, generate_password_hash

from ..utils import generic_error_handler, gen_UUID, bytes_to_string
from flaskr.db.db import get_db
from flaskr.keys.keys import public_key_to_bytes

auth = Blueprint('auth', __name__, url_prefix='/auth')


@auth.route('/register', methods=['POST'])
def register():
    # Extracting from request
    data = json.loads(bytes_to_string(request.data))

    db = get_db()

    if not data['metadata']['name'] or\
        not data['metadata']['username'] or\
        not data['metadata']['password'] or\
        not data['metadata']['publicKey'] or\
        not data['paymentInfo']['CVV'] or\
        not data['paymentInfo']['cardNumber'] or\
        not data['paymentInfo']['cardValidity']['month'] or\
        not data['paymentInfo']['cardValidity']['year']:
        abort(400)

    public_key = b"-----BEGIN PUBLIC KEY-----\n" +\
                 bytes(data['metadata']['publicKey'])[:-1] +\
                 b"\n-----END PUBLIC KEY-----"

    if db.execute(
        'SELECT id FROM user WHERE nickname = ?', (
            data['metadata']['username'],)
    ).fetchone() is not None:
        abort(409)

    # Creating the PaymentCard
    db.execute(
        'INSERT INTO paymentCard (cvv, cardNumber, monthValid,\
                yearValid) VALUES (?, ?, ?, ?)',
        (data['paymentInfo']['CVV'],
         data['paymentInfo']['cardNumber'],
         data['paymentInfo']['cardValidity']['month'],
         data['paymentInfo']['cardValidity']['year'])
    )

    # Registering the new USer
    user_uuid = str(gen_UUID())
    db.execute(
        'INSERT INTO user (id, username, nickname, cardNumber,\
                userPublicKey) VALUES (?, ?, ?, ?, ?)',
        (user_uuid,
         data['metadata']['name'],
         data['metadata']['username'],
         data['paymentInfo']['cardNumber'],
         public_key)
    )

    # Committing changes to db
    db.commit()

    # Returning the User's uuid and the supermarket key
    return current_app.response_class(
        response=json.dumps({
            'uuid': user_uuid,
            'public_key': bytes_to_string(
                public_key_to_bytes(current_app.config["PUBLIC_KEY"])
            )
        }),
        status=201,
        mimetype='application/json'
    )


# Customized Error handlers
@auth.errorhandler(400)
def handle_bad_request(e):
    return generic_error_handler(
        400, "Given information is not valid."
    )


@auth.errorhandler(409)
def handle_wrong_image_type(e):
    return generic_error_handler(
        409, "There already exists an user with the given username."
    )
