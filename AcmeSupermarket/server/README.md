# AcmeSupermarket Server

## Setup _&_ Launch

To fire up the server one must fist activate a virtual environment:

* In Mac \ Linux:
```shell
python3 -m venv venv
. venv/bin/activate
pip3 install -e .
```

* In Windows:
```shell
py -3 -m venv venv
venv\Scripts\activate
pip3 install -e .
```

And then, to actually launch the server:
```shell
export FLASK_APP=flaskr
export FLASK_DEBUG=true
flask init-db
flask run                   # Launch app
```

Notice that if you wish to, there are to special commands to interact with the flask app, namely:
```shell
flask init-db               # For initializing the db
flask gen-keys              # For generating new server public and private keys
```

## Route examples

Here are some useful queries to test the available routes using [curl](https://curl.haxx.se).

### `auth/register`
```shell
curl -d "nickname=yoloDude&paymentCard=928465823&key=supadupakey" -X POST http://localhost:5000/auth/register
```
Expected _json_ response:
```json
{
    "public_key": "b'-----BEGIN PUBLIC KEY-----\\nMFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANj1kmumzLb3q5VzHVpfi2/d7MvZGVTh\\npAult04LidIjLKQo/PQX3SiT7QXwH3fAKEqinOJdxSS0ZATe/FXygMsCAwEAAQ==\\n-----END PUBLIC KEY-----\\n'",
    "uuid": "1c7372bf-03b7-47cd-9cfc-eb68a6b1e406"
}
```