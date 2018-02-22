const express = require('express');
const HttpStatus = require('http-status-codes');
const Parent = require('../model/Parent');
const errorUtils = require('../util/errorUtils');

const parentRouter = express.Router();

parentRouter.get('/', (req, res) => {
    Parent.find()
        .then(parents => res.status(HttpStatus.OK).send(parents))
        .catch(() => res.status(HttpStatus.INTERNAL_SERVER_ERROR).send(errorUtils.errorMessage(`Parents could not be retrieved.`)));
});

parentRouter.get('/:id', (req, res, next) => {
    const id = req.params.id;
    Parent.findById(req.params.id)
        .then(parent => res.send(parent))
        .catch(() => res.status(HttpStatus.NOT_FOUND).send(errorUtils.errorMessage(`Parent with id ${id} could not be retrieved.`)))
        .then(next());
});

parentRouter.post('/', (req, res, next) => {
    const { name, surname, address } = req.body;
    const parent= new Parent({ name, surname, address });

    parent.save()
        .then(parent => res.status(HttpStatus.CREATED).send(parent)
        .catch(() => res.status(HttpStatus.INTERNAL_SERVER_ERROR).send(errorUtils.errorMessage('Parent could not be created'))))
        .then(next());
});

parentRouter.patch('/:id', (req, res, next) => {
    Parent.findById(req.params.id)
        .then(parent => {
            const { name, surname, address } = req.body;
            parent.name = name;
            parent.surname = surname;
            parent.address = address;
            parent.save()
                .then(parent => res.status(HttpStatus.OK).send(parent))
                // TODO: Remove duplicated code...
                .catch(() => res.status(HttpStatus.INTERNAL_SERVER_ERROR).send(errorUtils.errorMessage(`Parent with id ${req.params.id} could not be modified.`)))
                .then(next());
        })
        .catch(() => {
            res.status(HttpStatus.INTERNAL_SERVER_ERROR).send(errorUtils.errorMessage(`Parent with id ${req.params.id} could not be modified.`));
            next();
        });
});

parentRouter.delete('/:id', (req, res, next) => {
    Parent.remove({ _id: req.params.id })
        .then(() => res.status(HttpStatus.OK).send())
        .catch(() => res.status(HttpStatus.INTERNAL_SERVER_ERROR).send(errorUtils.errorMessage(`Parent with id ${req.params.id} could not be deleted.`)))
        .then(next());
});

module.exports = express().use('/parents', parentRouter);
