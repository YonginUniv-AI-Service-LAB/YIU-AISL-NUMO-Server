package com.aisl.shop.exception.handler;

import com.aisl.shop.exception.auth.*;
import com.aisl.shop.exception.common.ApiError;
import com.aisl.shop.exception.order.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ [1] 로그인 실패 (Spring Security 인증 오류)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        log.warn("[로그인 실패] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("BAD_CREDENTIALS", "아이디 또는 비밀번호가 잘못되었습니다."));
    }

    // ✅ [2] 유효성 검사 실패 (Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new ApiError("VALIDATION_FAILED", errorMessage));
    }

    // ✅ [3] 회원 도메인 관련 예외
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("USER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("EMAIL_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleNicknameExists(NicknameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("NICKNAME_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("INVALID_PASSWORD", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenUserAccessException.class)
    public ResponseEntity<ApiError> handleForbiddenAccess(ForbiddenUserAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError("FORBIDDEN_ACCESS", ex.getMessage()));
    }

    @ExceptionHandler(EmailVerificationFailedException.class)
    public ResponseEntity<ApiError> handleEmailVerificationFail(EmailVerificationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("EMAIL_VERIFICATION_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiError> handleEmailNotVerified(EmailNotVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("EMAIL_NOT_VERIFIED", ex.getMessage()));
    }

    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ApiError> handleGoogleLoginError(GoogleLoginException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("GOOGLE_LOGIN_FAILED", ex.getMessage()));
    }

    // ✅ [4] 주문(Order) 관련 예외
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("ORDER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(OrderAlreadyProcessedException.class)
    public ResponseEntity<ApiError> handleOrderAlreadyProcessed(OrderAlreadyProcessedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("ORDER_ALREADY_PROCESSED", ex.getMessage()));
    }

    @ExceptionHandler(OrderAlreadyPaidException.class)
    public ResponseEntity<ApiError> handleOrderAlreadyPaid(OrderAlreadyPaidException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("ORDER_ALREADY_PAID", ex.getMessage()));
    }

    @ExceptionHandler(OrderNotPaidException.class)
    public ResponseEntity<ApiError> handleOrderNotPaid(OrderNotPaidException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("ORDER_NOT_PAID", ex.getMessage()));
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderItemNotFound(OrderItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("ORDER_ITEM_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiError> handleInvalidOrderStatus(InvalidOrderStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_ORDER_STATUS", ex.getMessage()));
    }

    @ExceptionHandler(UnsupportedPaymentException.class)
    public ResponseEntity<ApiError> handleUnsupportedPayment(UnsupportedPaymentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("UNSUPPORTED_PAYMENT", ex.getMessage()));
    }

    // ✅ [5] 기타 Runtime 예외 (마지막 방어선)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        log.error("[서버 오류] {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("RUNTIME_EXCEPTION", "요청 처리 중 오류가 발생했습니다."));
    }
}
