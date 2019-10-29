# AcmeSupermarket Server

## Setup _&_ Launch

To fire up the server one must fist activate a virtual environment:

* In Mac \ Linux:
```shell
python3 -m venv venv
. venv/bin/activate
pip3 install -U -r requirements.txt
```

* In Windows:
```shell
py -3 -m venv venv
venv\Scripts\activate
pip3 install -U -r requirements.txt
```

And then, to actually launch the server:
```shell
export FLASK_APP=flaskr
export FLASK_DEBUG=true
flask init-db               # For initializing the db
flask run                   # Launch app
```

## Route examples

Here are some useful queries to test the available routes using [curl](https://curl.haxx.se).

* `auth/register`
```shell
curl -d "username=admin&password=default" -X POST http://localhost:5000/auth/register
```

* `auth/login`
```shell
curl -d "username=admin&password=default" -X POST http://localhost:5000/auth/login
```
