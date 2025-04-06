// src/components/SkiPassTable.js

import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';

const SkiPassTable = ({ skiPasses, onEdit }) => {
    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Pass Type</TableCell>
                        <TableCell>Price</TableCell>
                        <TableCell>Duration (Days)</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {skiPasses.map(skiPass => (
                        <TableRow key={skiPass.id}>
                            <TableCell>{skiPass.passType}</TableCell>
                            <TableCell>{skiPass.price}</TableCell>
                            <TableCell>{skiPass.duration}</TableCell>
                            <TableCell>
                                <Button onClick={() => onEdit(skiPass.id)}>Edit</Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default SkiPassTable;
