const api = require('../api/Routes');
const port = require('./Config').PORT;

const server = () => {
    api.listen(port, () => {
        console.log(`Listening on port ${port}`);
    });
};
module.exports = server;