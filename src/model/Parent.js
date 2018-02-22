const mongoose = require('mongoose');

const ParentSchema = mongoose.Schema(
    {
        name: {
            type: String,
            required: true,
        },
        surname: {
            type: String,
            required: true,
        },
        address: {
            type: String,
            required: true,
        },
    },
    {
        timestamps: true,
    });

module.exports = mongoose.model('Parent', ParentSchema);