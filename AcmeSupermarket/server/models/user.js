const bcrypt = require('bcrypt');

const SALT_WORK_FACTOR = 10;

module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define('User', {
        uuid: {
            type: DataTypes.UUIDV4,
            unique: true,
            allowNull: false,
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        nickname: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        password: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        paymentCard: {
            // Update this later
            type: DataTypes.INTEGER,
            allowNull: false,
        }
    }, {});

    // eslint-disable-next-line
    User.prototype.isValidPassword = async function (password) {
        const compare = await bcrypt.compare(password, this.password);
        return compare;
    };

    User.associate = (models) => {
        User.hasMany(models.Transaction, {
            foreignKey: {
                name: 'uuid',
                allowNull: true
            }
        })
        User.hasMany(models.Voucher, {
            foreignKey: {
                name: 'uuid',
                allowNull: true
            }
        })
    };

    User.beforeCreate(async (user) => {
        /* eslint-disable */
        user.password = await bcrypt.hash(user.password, SALT_WORK_FACTOR);
    });

    return User;
};
