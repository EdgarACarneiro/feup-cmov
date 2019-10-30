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


def bytes_to_string(byte_string):
    '''Convert bytes to a string'''
    return byte_string.decode('utf-8')


def string_to_bytes(string):
    '''Convert a string to bytes'''
    return string.encode('utf-8')
