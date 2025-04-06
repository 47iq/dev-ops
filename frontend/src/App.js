// src/App.js

import React, { useState, useEffect } from 'react';
import SkiPassTable from './components/SkiPassTable';
import SkiPassForm from './components/SkiPassForm';
import AddSkiPassForm from './components/AddSkiPassForm';
import { Container, Paper, Box } from '@mui/material';
import axios from 'axios';

const App = () => {
    const [selectedSkiPassId, setSelectedSkiPassId] = useState(null);
    const [skiPasses, setSkiPasses] = useState([]);
    const [skiPassesUpdated, setSkiPassesUpdated] = useState(0); // Используем число для изменения ключа

    // Функция для обновления списка после добавления нового объекта
    const handleAdd = () => {
        setSkiPassesUpdated(prevState => prevState + 1); // Изменяем состояние для обновления таблицы
    };

    // Получение всех скипассов
    const fetchSkiPasses = () => {
        axios.get('http://localhost:8080/api/ski-passes')
            .then(response => {
                setSkiPasses(response.data);
            })
            .catch(error => {
                console.error('Error fetching ski passes:', error);
            });
    };

    useEffect(() => {
        fetchSkiPasses();
        const interval = setInterval(fetchSkiPasses, 3000);  // Обновление таблицы каждую 3 секунды

        return () => clearInterval(interval); // Очистка интервала при размонтировании компонента
    }, [skiPassesUpdated]); // Используем skiPassesUpdated как зависимость для перерисовки

    const handleUpdateSuccess = () => {
        setSkiPassesUpdated(prevState => prevState + 1); // Обновляем таблицу после успешного обновления
    };

    const handleCloseForm = () => {
        setSelectedSkiPassId(null);  // Скрываем форму после обновления
    };

    return (
        <Container sx={{ marginTop: 4 }}>
            <Paper sx={{ padding: 2 }}>
                <Box sx={{ marginBottom: 4 }}>
                    <AddSkiPassForm onAdd={handleAdd} />
                </Box>
                <SkiPassTable
                    key={skiPassesUpdated} // Обновляем ключ для перерисовки
                    onEdit={setSelectedSkiPassId}
                    skiPasses={skiPasses}
                />
                {selectedSkiPassId &&
                    <SkiPassForm
                        skiPassId={selectedSkiPassId}
                        onUpdateSuccess={handleUpdateSuccess}
                        onCloseForm={handleCloseForm}
                    />}
            </Paper>
        </Container>
    );
};

export default App;
