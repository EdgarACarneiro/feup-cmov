from flask import (
    current_app, json
)
from uuid import uuid4

def generic_error_handler(error: int, msg: str):
    '''Creates a generic error handler for the given error
    and returns the given message as the content'''
    return current_app.response_class(
        response=json.dumps(msg),
        status=error,
        mimetype='application/json'
    )

def gen_UUID():
    '''Generates a UUID using random values'''
    return uuid4()
