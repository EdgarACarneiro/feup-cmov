module.exports = (sequelize, DataTypes) => {
    const Transaction = sequelize.define('Transaction', {
        uuid: {
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        },
        total: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
        discounted: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
    }, {});

    Transaction.associate = (models) => {
        Transaction.hasMany(models.Product, {
            foreignKey: {
                name: 'code',
                allowNull: false
            }
        })
        Transaction.hasOne(models.Voucher, {
            foreignKey: {
                name: 'TODO',
                allowNull: true
            }
        })
    };

    return Transaction;
};
