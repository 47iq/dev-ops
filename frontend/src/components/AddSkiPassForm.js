// src/components/AddSkiPassForm.js

import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, Grid, Box, Typography } from '@mui/material';

const AddSkiPassForm = ({ onAdd }) => {
    const [skiPass, setSkiPass] = useState({
        passType: '',
        price: 0,
        duration: 0
    });

    // Обработчик изменения значений в полях формы
    const handleChange = (e) => {
        const { name, value } = e.target;
        setSkiPass(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Обработчик отправки формы
    const handleSubmit = (e) => {
        e.preventDefault();

        // Отправляем новые данные на сервер
        axios.post('http://localhost:8080/api/ski-passes', skiPass)
            .then(response => {
                alert('Ski pass added successfully!');
                // Очистка формы
                setSkiPass({
                    passType: '',
                    price: 0,
                    duration: 0
                });
                onAdd();  // Обновление списка после добавления
            })
            .catch(error => {
                console.error('Error adding ski pass:', error);
            });
    };

    return (
        <Box sx={{ maxWidth: 400, margin: 'auto' }}>
            <Typography variant="h5" gutterBottom>
                Add New Ski Pass
            </Typography>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            label="Pass Type"
                            variant="outlined"
                            fullWidth
                            name="passType"
                            value={skiPass.passType}
                            onChange={handleChange}
                            required
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="Price"
                            variant="outlined"
                            fullWidth
                            type="number"
                            name="price"
                            value={skiPass.price}
                            onChange={handleChange}
                            required
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="Duration (days)"
                            variant="outlined"
                            fullWidth
                            type="number"
                            name="duration"
                            value={skiPass.duration}
                            onChange={handleChange}
                            required
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Button variant="contained" color="primary" fullWidth type="submit">
                            Add Ski Pass
                        </Button>
                    </Grid>
                </Grid>
            </form>
        </Box>
    );
};

export default AddSkiPassForm;
