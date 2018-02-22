if (!process.env.NODE_ENV || process.env.NODE_ENV === 'development') {
    require('dotenv').load();
}
require('./src/server/Server')();