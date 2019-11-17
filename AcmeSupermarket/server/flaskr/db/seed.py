from .db import get_db


def seed_db():
    """Seed the database"""
    db = get_db()

    # Create Payment Cards
    pcs = [
        (123, '123409930913', 12, 23)
    ]
    for pc in pcs:
        db.execute(
            'INSERT INTO paymentCard (cvv, cardNumber, monthValid,\
                    yearValid) VALUES (?, ?, ?, ?)',
            pc
        )

    # Create Users
    users = [
        ('0001', 'Bot', 'Bot1', '123409930913', 'testKey'),
        ('0002', 'Bot', 'Bot2', '123409930913', 'anotherKey'),
        ('0003', 'Bot', 'Bot3', '123409930913', 'key')
    ]
    for user in users:
        db.execute(
            'INSERT INTO user (id, username, nickname, cardNumber,\
                    userPublicKey) VALUES (?, ?, ?, ?, ?)',
            user
        )

    # Create Products
    prods = [
        ('4dadae03-06c6-4a18-9eed-18c8a34db686', 99, 'Trident Fruit'),
        ('4dadae03-06c6-4a18-9eed-48c8a34db686', 199, 'Ruffles'),
        ('4dadae03-06c6-4a18-9eed-38c8a34db686', 199, 'Ketchup'),
        ('4dadae03-06c6-4a18-9eed-68c8a34db686', 16, 'Water Luso 0.5L'),
        ('4dadae03-06c6-4a18-9eed-78c8a34db686', 1420, 'Wine Tapada Das Lebres 1L')
    ]
    for prod in prods:
        db.execute(
            'INSERT INTO product (code, price, prodName) VALUES (?, ?, ?)',
            prod
        )

    # Create Vouchers
    vouchers = [
        ('4dadae03-06c6-4a18-9eed-18c8a34db686', '0001', int(False)),
        ('5dadae03-06c6-4a18-9eed-18c8a34db686', '0001', int(True)),
        ('7dadae03-06c6-4a18-9eed-18c8a34db686', '0001', int(False)),
        ('9dadae03-06c6-4a18-9eed-18c8a34db686', '0003', int(True))
    ]
    for voucher in vouchers:
        db.execute(
            'INSERT INTO voucher (id, ownerID, used) VALUES (?, ?, ?)',
            voucher
        )

    # Create Transactions
    transactions = [
        (1, '0001', 397, 200, '5dadae03-06c6-4a18-9eed-18c8a34db686', [
            ('prod1', 2),
            ('prod2', 1)
        ]),
        (2, '0001', 513, 0, None, [
            ('prod1', 1),
            ('prod2', 1),
            ('prod3', 1),
            ('prod4', 1)
        ]),
        (3, '0002', 7100, 0, None, [
            ('prod5', 5)
        ]),
        (4, '0003', 397, 0, '9dadae03-06c6-4a18-9eed-18c8a34db686', [
            ('prod4', 10),
            ('prod2', 10)
        ]),
    ]
    for *t, tps in transactions:
        db.execute(
            'INSERT INTO acmeTransaction (id, ownerID, total,\
                discounted, voucherID) VALUES (?, ?, ?, ?, ?)',
            t
        )
        for tp in tps:
            db.execute(
                'INSERT INTO transactionProdcuts (transactionID,\
                    productID, quantity) VALUES (?, ?, ?)',
                (t[0], *tp)
            )

    # Commiting everything to the database
    db.commit()
