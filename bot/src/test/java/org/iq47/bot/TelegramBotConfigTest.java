package org.iq47.bot;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.*;

@SpringBootTest
class TelegramBotConfigTest {

    @Test
    void testMainMethod() {
        // Arrange
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(TelegramBotConfig.class, new String[]{}))
                    .thenReturn(mockContext);

            // Act
            TelegramBotConfig.main(new String[]{});

            // Assert
            mockedSpringApplication.verify(() ->
                            SpringApplication.run(TelegramBotConfig.class, new String[]{}),
                    times(1)
            );
        }
    }

    @Test
    void testMainMethodWithArgs() {
        // Arrange
        String[] args = {"--test=value"};
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(TelegramBotConfig.class, args))
                    .thenReturn(mockContext);

            // Act
            TelegramBotConfig.main(args);

            // Assert
            mockedSpringApplication.verify(() ->
                            SpringApplication.run(TelegramBotConfig.class, args),
                    times(1)
            );
        }
    }
}