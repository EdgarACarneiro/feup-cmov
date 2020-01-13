# feup-cmov
Project developed for 'Mobile Computation', a fifth year subject @FEUP. Made in collaboration with [@chaotixkilla](https://github.com/chaotixkilla).

## Practical Assignment \#1

### Theme

A supermarket – call it the Acme Electronic Supermarket – intends to implement a more efficient shopping and payment system supplying an Android app to its customers.

The idea asks for the customers to register the products in the app, when they put them in the shop basket. When they are ready to leave, they check out the products in the basket passing or showing the phone in a terminal. The payment is made (using the associated credit or debit card) and the gate doors are opened.

Using the app, the customers should first make a registration (only once when they use the app for the first time) in the supermarket remote service, supplying personal data and payment card data for payments. Also, an asymmetric cryptographic key pair is generated, being the private key securely stored in the device.

At any time, the app should be capable of consulting past transactions’ information, and retrieve available emitted loyalty vouchers, that can concede a discount in some future buy. A loyalty voucher is offered whenever the customer accumulated payments surpassed a multiple of some value.

### Result

* __Grade and Documentation__

| Report | Server Setup | Report Grade (20%)| Project Grade (80%)|
|:- |:- |:-:|:-:|
| Report is available [here](https://github.com/EdgarACarneiro/feup-cmov/blob/master/AcmeSupermarket/docs/report.pdf) | Server Setup is available [here](https://github.com/EdgarACarneiro/feup-cmov/tree/master/AcmeSupermarket/server) | 19.5 | 19 |

* __Customer App UI__

| | | |
|:-:|:-:|:-:|
| Register | Login | Main Menu |
| ![Screenshot_20191117_190216_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869952-39188200-12a6-11ea-9133-f2597c448f09.jpg) | ![Screenshot_20191117_185602_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869951-39188200-12a6-11ea-9ea7-ea0d34653f76.jpg) | ![Screenshot_20191117_190939_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869953-39188200-12a6-11ea-9965-dfc85df80a61.jpg) |
| Confirm Purchase | Previous Transactions | Checkout |
| ![Screenshot_20191118_154810_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869955-39b11880-12a6-11ea-8ba3-b8f7b831cbc8.jpg) | ![Screenshot_20191118_154946_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869956-39b11880-12a6-11ea-86d8-e1fffce87a8b.jpg) | ![Screenshot_20191118_154708_org feup cmov acmecustomer](https://user-images.githubusercontent.com/22712373/69869954-39188200-12a6-11ea-8e6c-eae9cd8a7086.jpg) |

* __Terminal App UI__

| | | |
|:-:|:-:|:-:|
| Landing Screen | Successful Transaction | Error on Transaction | 
| ![received_2298018783654117](https://user-images.githubusercontent.com/22712373/69869950-387feb80-12a6-11ea-832c-b632882d11de.png) | ![received_445207113079941](https://user-images.githubusercontent.com/22712373/69869947-37e75500-12a6-11ea-9c3b-85cb00b57f4e.png) | ![received_458530121440994](https://user-images.githubusercontent.com/22712373/69869948-387feb80-12a6-11ea-9c82-87c68d9cca33.png) |

---

## Practical Assignment \#2

### Theme

A team of mobile apps’ developers had the idea to provide an application for weather data consultation.
In this app the user maintains a record of the cities he is interested in, among the district capitals of Portugal. In any time, he can add or remove cities from the list.

Also, from the list of those particularly interesting cities the user can ask for the current weather conditions of a city, obtaining information at least about temperature, pressure, precipitation, wind and humidity (can also have an image characterizing those conditions – an icon).

Another feature is that the user can ask for a characterization of a forecast, in the next day and in 3h intervals, of a city (between the ones in the preferred list), and should obtain in a separate page at least min and max temperatures, wind, precipitation, and humidity. In a section of that page a line graph should be drawn linking the temperatures at 3h intervals along the next 8 forecasts (for instance at 0h00, 3h00, 6h00, 9h00, 12h00, 15h00, 18h00, 21h00). Also, a small image, characterizing the conditions for each of those hours, can optionally be shown near each of the 8 mains points of the graph.

### Result

* __Grade and Documentation__

| Report | Report Grade (20%)| Project Grade (80%)|
|:- |:-:|:-:|
| Report is available [here](https://github.com/EdgarACarneiro/feup-cmov/blob/master/WeatherApp/docs/report.pdf) | 19 | 19 |

* __Weather App UI__

| Application Workflow and Screens | 
|:-:|
| ![Imgur](https://i.imgur.com/yJfA38K.png)|

## Authors
* [chaotixkilla](https://github.com/chaotixkilla)
* [EdgarACarneiro](https://github.com/EdgarACarneiro)
