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


@auth.before_app_request
def load_logged_in_user():
    user_id = session.get('user_id')

    if user_id is None:
        g.user = None
    else:
        g.user = get_db().execute(
            'SELECT * FROM user WHERE id = ?', (user_id,)
        ).fetchone()


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

    public_key = array('b', data['metadata']['publicKey'])

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
    user_uuid = gen_UUID()
    db.execute(
        'INSERT INTO user (id, username, nickname, cardNumber,\
                userPublicKey) VALUES (?, ?, ?, ?, ?)',
        (user_uuid.bytes,
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


def login_required(view):
    @functools.wraps(view)
    def wrapped_view(**kwargs):
        if g.user is None:
            return abort(415)

        return view(**kwargs)

    return wrapped_view


@auth.route('/login', methods=['POST'])
def login():
    username = request.form['nick']
    password = request.form['password']
    db = get_db()
    error = None
    user = db.execute(
        'SELECT * FROM user WHERE username = ?', (username)
    ).fetchone()

    if user is None:
        error = 'Incorrect username.'
    elif not check_password_hash(user['password'], password):
        error = 'Incorrect password.'

    if error is None:
        session.clear()
        session['user_id'] = user['id']
        return current_app.response_class(
            response={},
            status=200,
            mimetype='application/json'
        )

    flash(error)

    return abort(500)

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
