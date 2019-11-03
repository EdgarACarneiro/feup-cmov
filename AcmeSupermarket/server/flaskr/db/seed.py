from .db import get_db


def seed_db():
    """Seed the database"""
    db = get_db()

    # Create Payment Cards
    pcs = [
        (1, 123, '123409930913', 987654321, 12, 23)
    ]
    for pc in pcs:
        db.execute(
            'INSERT INTO paymentCard (id, cvv, cardNumber,\
                    monthValid, yearValid) VALUES (?, ?, ?, ?, ?)',
            pc
        )

    # Create Users
    users = [
        ('0001', 'Bot', 'Bot1', 1, 'testKey'),
        ('0002', 'Bot', 'Bot2', 1, 'anotherKey'),
        ('0003', 'Bot', 'Bot3', 1, 'key')
    ]
    for user in users:
        db.execute(
            'INSERT INTO user (id, nickname, paymentCard,\
                    userPublicKey) VALUES (?, ?, ?, ?)',
            user
        )

    # Create Products
    prods = [
        ('prod1', '099', 'Trident Fruit'),
        ('prod2', '199', 'Ruffles'),
        ('prod3', '199', 'Ketchup'),
        ('prod4', '016', 'Water Luso 0.5L'),
        ('prod5', '1420', 'Wine Tapada Das Lebres 1L')
    ]
    for prod in prods:
        db.execute(
            'INSERT INTO product (code, price, prodName) VALUES (?, ?, ?)',
            prod
        )

    # Create Vouchers
    vouchers = [
        (1, '0001', int(False)),
        (2, '0001', int(True)),
        (3, '0001', int(False)),
        (4, '0003', int(True))
    ]
    for voucher in vouchers:
        db.execute(
            'INSERT INTO voucher (id, ownerID, used) VALUES (?, ?, ?)',
            voucher
        )

    # Create Transactions
    transactions = [
        (1, '0001', 397, 200, 2, [
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
        (4, '0003', 397, 0, 4, [
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
