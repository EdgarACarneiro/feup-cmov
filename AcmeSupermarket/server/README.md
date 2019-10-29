# AcmeSupermarket Server

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
flask run
```
