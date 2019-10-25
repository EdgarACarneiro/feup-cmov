module.exports = (sequelize, DataTypes) => {
    const Voucher = sequelize.define('Voucher', {
        uuid: {
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        }
        // When voucher is used associate ti transaction and remove from User's list
    }, {});

    return Voucher;
};
