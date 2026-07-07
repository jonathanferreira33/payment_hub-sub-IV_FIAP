package infrastructure;

import com.fiap.payment_hub.infrastructure.error.ApiErrorResponse;
import com.fiap.payment_hub.infrastructure.error.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Test
    void deveRetornarBadRequestParaIllegalArgumentException() {

        when(request.getRequestURI()).thenReturn("/payments");

        IllegalArgumentException exception =
                new IllegalArgumentException("Valor inválido");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleDomainValidation(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Business Rule Violation", body.error());
        assertEquals("Valor inválido", body.message());
        assertEquals("/payments", body.path());
        assertNotNull(body.timestamp());
    }

    @Test
    void deveRetornarBadRequestParaIllegalStateException() {

        when(request.getRequestURI()).thenReturn("/payments");

        IllegalStateException exception =
                new IllegalStateException("Estado inválido");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleDomainValidation(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Business Rule Violation", body.error());
        assertEquals("Estado inválido", body.message());
        assertEquals("/payments", body.path());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {

        when(request.getRequestURI()).thenReturn("/payments");

        Exception exception = new RuntimeException("Erro qualquer");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ApiErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("Erro do Servidor Interno", body.error());
        assertEquals(
                "Ocorreu um erro inesperado. Entre em contato com o suporte.",
                body.message()
        );
        assertEquals("/payments", body.path());
        assertNotNull(body.timestamp());
    }
}
