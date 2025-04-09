import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { TextField, Button, Grid, Box, Typography, Paper, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';

const SkiPassForm = ({ skiPassId, onUpdateSuccess, onCloseForm }) => {
    const [skiPass, setSkiPass] = useState({
        passType: '',
        price: 0,
        duration: 0
    });

    // State to control the confirmation dialog for deletion
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

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

    // Функция для открытия диалогового окна подтверждения удаления
    const handleDeleteClick = () => {
        setDeleteDialogOpen(true);
    };

    // Функция для закрытия диалогового окна подтверждения удаления
    const handleDeleteCancel = () => {
        setDeleteDialogOpen(false);
    };

    // Функция для выполнения удаления
    const handleDeleteConfirm = () => {
        axios.delete(`http://localhost:8080/api/ski-passes/${skiPassId}`)
            .then(response => {
                alert('Ski pass deleted successfully!');
                onUpdateSuccess();  // Обновляем таблицу после успешного удаления
                onCloseForm();  // Скрываем форму
            })
            .catch(error => {
                console.error('Error deleting ski pass:', error);
            })
            .finally(() => {
                setDeleteDialogOpen(false); // Закрываем диалоговое окно
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
                        <Grid item xs={12} container justifyContent="space-between">
                            <Button variant="contained" color="primary" type="submit">
                                Update Ski Pass
                            </Button>
                            <Button variant="contained" color="error" onClick={handleDeleteClick}>
                                Delete Ski Pass
                            </Button>
                        </Grid>
                    </Grid>
                </form>
            </Paper>

            {/* Диалоговое окно подтверждения удаления */}
            <Dialog open={deleteDialogOpen} onClose={handleDeleteCancel}>
                <DialogTitle>Delete Ski Pass</DialogTitle>
                <DialogContent>
                    Are you sure you want to delete this ski pass?
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteCancel} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleDeleteConfirm} color="error" autoFocus>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default SkiPassForm;