from flask import (
    current_app, json
)
from uuid import uuid4
import base64

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


def decode(byte_string):
    '''Convert bytes to a string'''
    return byte_string.decode('iso8859-1')


def encode(string):
    '''Convert a string to bytes'''
    return string.encode('iso8859-1')


def b64_decode(byte_array):
    '''Convert bytes BASE64 format'''
    return base64.b64decode(byte_array)


def b64_encode(byte_array):
    '''Convert bytes BASE64 format'''
    return base64.b64encode(byte_array)
