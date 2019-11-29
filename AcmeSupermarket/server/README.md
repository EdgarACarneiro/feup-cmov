# AcmeSupermarket Server

## Setup _&_ Launch


1. Position yourself inside the /server folder.
2. Activate a virtual environment. To do so, run the following commands:
> In Mac/ Linux:
```shell
python3 -m venv venv
. venv/bin/activate
pip3 install -e .
```
> In Windows:
```shell
py -3 -m venv venv
venv\Scripts\activate
pip3 install -e .
```
3. Then, run the following commands to launch the server:
> In Mac/ Linux:
```shell
export FLASK_APP=flaskr
flask init-db            # For initializing the database 
flask seed-db            # For seeding the database
flask run                # For running the application
```
> In Windows (using Powershell running as administrator):
```
$env:FLASK_APP=”flaskr”
flask init-db            # For initializing the database 
flask seed-db            # For seeding the database
flask run                # For running the application
```

4. The server shall now be running in port 5000.
5. Seeing that the server will be accessed remotely using the internet, it must be visible to do so. Hence, with ngrok one can expose a local server as a public URL. To do so, download ngrok and then position yourself in the folder containing it and run:
```shell
./ngrok http 5000
```
6. The http _URL_ being forwarded is what is of interest and shall be copied. In this copy, the user should copy `xxxxxxxx.ngrok.io`. Notice that the http is not copied since the mobile applications inject the http header by themselves.

## Route examples

* ### `get-products`
```shell
curl http://localhost:5000/get-products
```
> Response will be signed by ACME's private key

Expected _json_ response, after verifying and converting text to _json_:
```json
[
    {
        "code": "prod1",
        "price": 99,
        "prodName": "Trident Fruit"
    },
    {
        "code": "prod2",
        "price": 199,
        "prodName": "Ruffles"
    },
    {
        "code": "prod3",
        "price": 199,
        "prodName": "Ketchup"
    },
    {
        "code": "prod4",
        "price": 16,
        "prodName": "Water Luso 0.5L"
    },
    {
        "code": "prod5",
        "price": 1420,
        "prodName": "Wine Tapada Das Lebres 1L"
    }
]
```

* ### `/auth/register`
For sending the data to the server and making it easier to interpret on server side, the register route was developed using json for both the request and the response.

__Request example:__
```json
{"metadata":{"name":"Pedro","password":"edgar","publicKey":"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANZNb/F40h6Jp6sJ/WB92/oNN981d6oN9idQX1YB1hhf\nMV4z0GlJ9zCWSVEZ99S9mMmerxmWur7BI7G6r3cpYu0CAwEAAQ\u003d\u003d\n","username":"Pedro"},"paymentInfo":{"CVV":123,"cardNumber":"9238948293489293","cardValidity":{"month":1,"year":23}}}
```
__Response example:__
```json
{"public_key":"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANj1kmumzLb3q5VzHVpfi2/d7MvZGVThpAult04LidIjLKQo/PQX3SiT7QXwH3fAKEqinOJdxSS0ZATe/FXygMsCAwEAAQ==", "uuid":"aa64bd8c-4aee-4a89-a990-cd42a8da057b"}
```
> Notice that the public key is in Base64 and encoded in format `ISO-8859-1`.

* ### `/get-transactions`
For the application request, byte handling and direct byte writing to a buffer is used. For the server response, a mixture between byte writing and json loading is used. 

__Request example:__
```python
b'Acme\xaad\xbd\x8cJ\xeeJ\x89\xa9\x90\xcdB\xa8\xda\x05{\xa0\x9b?\x1a\xdd\xa0\xb0\xc59\xff[\xdeF\x05\x946\xa2\x19S\xcb\x93\x9c}\xc5Lk\xa6\xac\xe8gC:\x06n7\xba\x1ej\x05\xd3\xb4W\\\xf8\xfb\xcdz\xdf\xcd\xff\xdb#M\x07P\x8d\x97\x91;\xb9\x9cqAa'
```
__Response format:__ An array of past transactions, where each transaction is encrypted with the customer public key and the totality of the message is signed with the server’s private key.

__Response example:__
```py
b'{"transactions": ["Hpv2m+gpUu9B7rfOMVEX0YocpwnXJRpAZY6BV4W+pHPTJ07BpaISnIacffRi1vjmxMlvyKeL3J0U2I6NtsCPGg==", "B0Xj/M8nk9cgVd6XNC3ysmyiI6BQA70WFktU1o+4MPiQDkeNhVS1G3pd3zpX1Len7KvTZ29NqgOOqpmjBUSbFA==", "cg+Cc/YDLT/Q7/EI48fuMB3rOliyTczBFp/irjXHmhNemqphnyfCZ/h+RDiOz8gt3RR/rKKxEj0wBPKwVA3WxA=="]}R\x86~\x13\x83yD\xde\xc9\xb3&,\xc51\xb5x\x8e\xdf\xae\'\xc5;B&1:\xa7A-`\xde\x19\xa23\xee\x87\xb7\x05\\\xed\x8c\x17 @\xa4op\x17a\x9d\xd1\xa9Z{)k\xfe3\x84ZN\x9aA@'
eyJ0cmFuc2FjdGlvbnMiOiBbIkhwdjJtK2dwVXU5QjdyZk9NVkVYMFlvY3B3blhKUnBBWlk2QlY0VytwSFBUSjA3QnBhSVNuSWFjZmZSaTF2am14TWx2eUtlTDNKMFUySTZOdHNDUEdnPT0iLCAiQjBYai9NOG5rOWNnVmQ2WE5DM3lzbXlpSTZCUUE3MFdGa3RVMW8rNE1QaVFEa2VOaFZTMUczcGQzenBYMUxlbjdLdlRaMjlOcWdPT3FwbWpCVVNiRkE9PSIsICJjZytDYy9ZRExUL1E3L0VJNDhmdU1CM3JPbGl5VGN6QkZwL2lyalhIbWhOZW1xcGhueWZDWi9oK1JEaU96OGd0M1JSL3JLS3hFajB3QlBLd1ZBM1d4QT09Il19UoZ+E4N5RN7JsyYsxTG1eI7frifFO0ImMTqnQS1g3hmiM+6HtwVc7YwXIECkb3AXYZ3RqVp7KWv+M4RaTppBQA==
```
> Notice that in the given example the discount and everys single voucher is encoded and then passed to `Base64`. It is also possible to discern the json structure.


* ### `/get-vouchers`
For the application request, byte handling and direct byte writing to a buffer is used. For the server response, a mixture between byte writing and json loading is used. 

__Request example:__
```py
b'Acme\xaad\xbd\x8cJ\xeeJ\x89\xa9\x90\xcdB\xa8\xda\x05{\xa0\x9b?\x1a\xdd\xa0\xb0\xc59\xff[\xdeF\x05\x946\xa2\x19S\xcb\x93\x9c}\xc5Lk\xa6\xac\xe8gC:\x06n7\xba\x1ej\x05\xd3\xb4W\\\xf8\xfb\xcdz\xdf\xcd\xff\xdb#M\x07P\x8d\x97\x91;\xb9\x9cqAa'
```

__Response format:__  An array of available vouchers as well as available discount, where besides the available discount, each voucher is encrypted with the customer public key and the totality of the message is signed with the server’s private key.

__Response example:__
```py
b'{"discount": "lgsTCONFGPG1PHIc4Mo8wYkI+WXSE8bdUrvqxnZR5nILR4wcwA4wxZmo/WW7SF6yOUr1Wnw/H3Bcv1dGYYUvHg==", "vouchers": ["KUetiXUI7u0sPDbCQx8D48iy0euVPiuiv5A5ywSCGrE8jDYn84+x612OLIX7x/uSe7Q4zdXNvYCJq7IF1Lrz0g==", "HB0zWO2fdC0hUnBbkEnpklob/2HkJyTK2GRPQnTtQWPEGQoSqXDego4IJqsg5yclzLc1ytmPu34YP2e9W1BkAw==", "qunTwvwDluanATLG68TZ/cgun9RKHisWSjnWmnlCzlb43+yRQFSrTwWzcovUaq3Ro0X3db6ItaWvNEsgVYbPdw==", "kVYp0OrCWMAuIthLJxKJ+T0CrHRd/Cw5P+fhV3N1bUJAdOkQrJa14CzaDohDbJwYI5tOcTCZaWZ2marU35w0rQ==", "fTzWWxAqrdydolo+hpY/LTiivYvv5YTpm0s7LHmO1elNThUr7EMSSZFhZnatU4snjos00jxfFuVy9FFa1BABqg=="]}\xa4cAoN?\x07Q\xa8-\xbe\xc5\xe7p\xd1`\xecw*\x06\xfd\x19\x06w\xa8]\xf4t\x1a\x06\\\xe2,\xd3\xba&\xd7O~\x1d\x8bz\x8e\xcaA\xf5\x14:\xa9\xe7\xd2\xef]\r\xd2QDE\xfe\xb7h\x0f\x05w'
```
> Notice that in the given example the discount and everys single voucher is encoded and then passed to `Base64`. It is also possible to discern the json structure.

* ### `/checkout`
For the application request, byte handling and direct byte writing to a buffer is used. For the server response, a mixture between byte writing and json loading is used.

__Request example:__
```py
b'Acme\xaad\xbd\x8cJ\xeeJ\x89\xa9\x90\xcdB\xa8\xda\x05{\xa0\x9b?\x1a\xdd\xa0\xb0\xc59\xff[\xdeF\x05\x946\xa2\x19S\xcb\x93\x9c}\xc5Lk\xa6\xac\xe8gC:\x06n7\xba\x1ej\x05\xd3\xb4W\\\xf8\xfb\xcdz\xdf\xcd\xff\xdb#M\x07P\x8d\x97\x91;\xb9\x9cqAa'
```
__Response format:__ A string containing the operation total price, in cents.

__Response example:__
```json
50820
```

For more details, please check the [official report](https://github.com/EdgarACarneiro/feup-cmov/blob/master/AcmeSupermarket/docs/report.pdf).