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

Before starting the server, __the server's private and public keys must be generated__, if they do not already exist. To do so, run the `gen_py` script, without any arguments. After doing so, we can expect to have a file `config.cfg` inside the `server/instance` folder, containing the public and private keys. An example of a generated config is presented below:
```python
PRIVATE_KEY = b'-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC3 (...) +1N3qe8Fqfsg3dN\nWgK3Al1dnYR6B7K8sxTpk6vZGYZAvmRv7FdSgQECgYA0+nc1JFrDJVJJUUNiDvRF\nJR15M/rnaHlZgVSGT/4zEU4WOrbf9Q9Y8EXQ6E+IaoFVnkVeUM2zeUqDt74Z/y7k\nqpyc/UqwY+qj/pBB0mHbgd8K//Abl09fOfcRn0DdxH3NIv2YgoArU9N1N+9ZHtnm\nMUxTmH7NuybUjjCORXiqoQ==\n-----END PRIVATE KEY-----\n'
PUBLIC_KEY = b'-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtwx/nyw1V+NcxCEoLKvM\nDm7CUFDq/bar7Jh0OkgF8NkH5YRauSONT8D8l2EMD7tV3A154CMoJHY0i8466q3T\n45RhO7JHS+Kncl2Ggsa/R/0lKDz1h5SuglRp4McDHtHVCpkulh+ad6NhLKtRz2xO\nAYahAEuAdx3O5GM153VM55190BxlffuXkC9wbyGvi9za75O1J/Y4eBMbAYrEQvlG\nXyMgBZHPgK0uOoACm3m7FbqjmtfZGdByb5sBx/iyRmtknFwKElSuqQHH5g+KMHVt\n2eprMxN4YeoSyqltkHhp3T/97xQFimFIh/gUqmiczfsolG3uFIkkGqcOgbEVda1V\nGQIDAQAB\n-----END PUBLIC KEY-----\n'
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

### `auth/register`
```shell
curl -d "nickname=yoloDude&paymentCard=928465823&key=supadupakey" -X POST http://localhost:5000/auth/register
```
Expected _json_ response:
```json
{
    "supermarketKey": "dev",
    "uuid": "40bd5ff6-eb90-4a79-9b9b-80ee79e5cb02"
}
```