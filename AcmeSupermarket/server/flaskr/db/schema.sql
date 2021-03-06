DROP TABLE IF EXISTS paymentCard;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS acmeTransaction;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS transactionProdcuts;

CREATE TABLE paymentCard (
  cvv INTEGER NOT NULL,
  cardNumber TEXT PRIMARY KEY,
  monthValid INTEGER NOT NULL,
  yearValid INTEGER NOT NULL
);

CREATE TABLE user (
  id VARCHAR(16) PRIMARY KEY,
  username TEXT NOT NULL,
  nickname TEXT UNIQUE NOT NULL,
  cardNumber TEXT NOT NULL,
  userPublicKey BLOB NOT NULL,
  accumulatedDiscount INTEGER DEFAULT 0,
  FOREIGN KEY (cardNumber) REFERENCES paymentCard (cardNumber)
);

CREATE TABLE voucher (
  id VARCHAR(16) PRIMARY KEY,
  ownerID VARCHAR(16) NOT NULL,
  used BOOLEAN DEFAULT 0,
  FOREIGN KEY (ownerID) REFERENCES user (id)
);

CREATE TABLE acmeTransaction (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  ownerID VARCHAR(16) NOT NULL,
  total INTEGER NOT NULL,
  discounted INTEGER DEFAULT 0,
  voucherID VARCHAR(16) DEFAULT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (ownerID) REFERENCES user (id),
  FOREIGN KEY (voucherID) REFERENCES voucher (id)
);

CREATE TABLE product (
  code VARCHAR(16) PRIMARY KEY,
  price INTEGER NOT NULL,
  prodName TEXT NOT NULL
);

CREATE TABLE transactionProdcuts (
  transactionID INTEGER NOT NULL,
  productID VARCHAR(16) NOT NULL,
  quantity INTEGER DEFAULT 1,
  FOREIGN KEY (transactionID) REFERENCES acmeTransaction (id),
  FOREIGN KEY (productID) REFERENCES product (code),
  PRIMARY KEY (transactionID, productID)
);
