module.exports = (sequelize, DataTypes) => {
    const Supermarket = sequelize.define('Supermarket', {
        key: {
            // TODO: Update datatype
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        }
    }, {});

    Supermarket.associate = (models) => {
        Supermarket.hasMany(models.User, {
            foreignKey: {
                name: 'uuid',
                allowNull: false
            }
        })
    };

    return Supermarket;
};
