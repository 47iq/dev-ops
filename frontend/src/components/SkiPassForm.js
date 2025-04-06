// src/components/SkiPassForm.js

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { TextField, Button, Grid, Box, Typography, Paper } from '@mui/material';

const SkiPassForm = ({ skiPassId, onUpdateSuccess, onCloseForm }) => {
    const [skiPass, setSkiPass] = useState({
        passType: '',
        price: 0,
        duration: 0
    });

    // Загрузка данных скипасса для редактирования
    useEffect(() => {
        axios.get(`http://localhost:8080/api/ski-passes/${skiPassId}`)
            .then(response => {
                setSkiPass(response.data);
            })
            .catch(error => {
                console.error('There was an error fetching the ski pass data:', error);
            });
    }, [skiPassId]);

    // Обработчик изменения значений в полях формы
    const handleChange = (e) => {
        const { name, value } = e.target;
        setSkiPass(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Обработчик отправки формы для обновления данных
    const handleSubmit = (e) => {
        e.preventDefault();

        axios.put(`http://localhost:8080/api/ski-passes/${skiPassId}`, skiPass)
            .then(response => {
                alert('Ski pass updated successfully!');
                onUpdateSuccess();  // Обновляем таблицу после успешного обновления
                onCloseForm();  // Скрываем форму
            })
            .catch(error => {
                console.error('Error updating ski pass:', error);
            });
    };

    return (
        <Box sx={{ maxWidth: 600, margin: 'auto' }}>
            <Typography variant="h5" gutterBottom>
                Edit Ski Pass
            </Typography>
            <Paper sx={{ padding: 2 }}>
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
                                Update Ski Pass
                            </Button>
                        </Grid>
                    </Grid>
                </form>
            </Paper>
        </Box>
    );
};

export default SkiPassForm;
