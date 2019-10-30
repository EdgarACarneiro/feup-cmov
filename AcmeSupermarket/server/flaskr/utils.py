from flask import (
    current_app, json
)

def generic_error_handler(error: int, msg: str):
    return current_app.response_class(
        response=json.dumps(msg),
        status=error,
        mimetype='application/json'
    )
