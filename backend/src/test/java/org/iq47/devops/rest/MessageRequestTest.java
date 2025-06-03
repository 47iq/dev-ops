package org.iq47.devops.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageRequestTest {

    @Test
    void defaultConstructor_shouldCreateEmptyObject() {
        MessageRequest request = new MessageRequest();
        assertNull(request.getMessage());
    }

    @Test
    void parameterizedConstructor_shouldSetMessage() {
        MessageRequest request = new MessageRequest("Hello!");
        assertEquals("Hello!", request.getMessage());
    }

    @Test
    void getterAndSetter_shouldWorkCorrectly() {
        MessageRequest request = new MessageRequest();
        request.setMessage("Test message");

        assertEquals("Test message", request.getMessage());
    }

    @Test
    void messageCanBeUpdated() {
        MessageRequest request = new MessageRequest("Initial");
        request.setMessage("Updated");

        assertEquals("Updated", request.getMessage());
    }
}
