module.exports = (sequelize, DataTypes) => {
    const Product = sequelize.define('Product', {
        code: {
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        },
        price: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        },
    }, {});

    return Product;
};
