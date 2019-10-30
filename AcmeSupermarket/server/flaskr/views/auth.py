import functools

from flask import (
    Blueprint, flash, g, request, session, current_app, request, abort, json
)
from werkzeug.security import check_password_hash, generate_password_hash

from ..utils import generic_error_handler, gen_UUID
from flaskr.db.db import get_db

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
    nickname = request.form['nickname']
    payment_card = request.form['paymentCard']
    user_public_key = request.form['key']

    db = get_db()

    if not nickname or\
            not payment_card or\
            not user_public_key:
        abort(400)

    elif db.execute(
        'SELECT id FROM user WHERE nickname = ?', (nickname,)
    ).fetchone() is not None:
        abort(409)

    # Registering the new USer
    user_uuid = str(gen_UUID())
    db.execute(
        'INSERT INTO user (id, nickname, paymentCard,\
                userPublicKey) VALUES (?, ?, ?, ?)',
        (user_uuid, nickname, payment_card, user_public_key)
    )
    db.commit()

    # Returning the User's uuid and the supermarket key
    return current_app.response_class(
        response=json.dumps({
            'uuid': user_uuid,
            'supermarketKey': current_app.config["SECRET_KEY"]
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
        'SELECT * FROM user WHERE username = ?', (username,)
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
        409, "There already exists an user with the given nickname."
    )
