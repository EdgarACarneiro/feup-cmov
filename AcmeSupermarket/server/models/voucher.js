module.exports = (sequelize, DataTypes) => {
    const Voucher = sequelize.define('Voucher', {
        uuid: {
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        },
        discount: {
            type: DataTypes.INTEGER,
        }
        // When voucher is used associate it to a transaction and remove from User's list
    }, {});

    return Voucher;
};
