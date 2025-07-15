package com.aisl.shop.exception.handler;

import com.aisl.shop.exception.auth.*;
import com.aisl.shop.exception.cartitem.CartItemAlreadyExistsException;
import com.aisl.shop.exception.cartitem.CartItemNotFoundException;
import com.aisl.shop.exception.cartitem.InvalidCartItemQuantityException;
import com.aisl.shop.exception.common.ApiError;
import com.aisl.shop.exception.order.*;
import com.aisl.shop.exception.product.*;
import com.aisl.shop.exception.productoption.DuplicateProductOptionException;
import com.aisl.shop.exception.productoption.InvalidStockQuantityException;
import com.aisl.shop.exception.productoption.ProductOptionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.aisl.shop.exception.user.DuplicateResourceException;
import com.aisl.shop.exception.user.UserNotFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ [1] 로그인 실패
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        log.warn("[로그인 실패] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("BAD_CREDENTIALS", "아이디 또는 비밀번호가 잘못되었습니다."));
    }

    // ✅ [2] 유효성 검사 실패
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

    // ✅ [3] 회원 관련 예외
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

    // ✅ [4] 주문 관련 예외
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

    // ✅ [5] 상품 관련 예외
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("PRODUCT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<ApiError> handleDuplicateProductName(DuplicateProductNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("DUPLICATE_PRODUCT_NAME", ex.getMessage()));
    }

    @ExceptionHandler(InvalidDiscountRateException.class)
    public ResponseEntity<ApiError> handleInvalidDiscountRate(InvalidDiscountRateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_DISCOUNT_RATE", ex.getMessage()));
    }

    @ExceptionHandler(InvalidProductOptionException.class)
    public ResponseEntity<ApiError> handleInvalidProductOption(InvalidProductOptionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_PRODUCT_OPTION", ex.getMessage()));
    }

    // ✅ [6] 상품 옵션 관련 예외 (하나만 유지)
    @ExceptionHandler(ProductOptionNotFoundException.class)
    public ResponseEntity<ApiError> handleProductOptionNotFound(ProductOptionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("OPTION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateProductOptionException.class)
    public ResponseEntity<ApiError> handleDuplicateOption(DuplicateProductOptionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("DUPLICATE_OPTION", ex.getMessage()));
    }

    @ExceptionHandler(InvalidStockQuantityException.class)
    public ResponseEntity<ApiError> handleInvalidStock(InvalidStockQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_STOCK", ex.getMessage()));
    }

    // ✅ [7] 장바구니 관련 예외
    @ExceptionHandler(CartItemAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCartItemExists(CartItemAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("CART_ITEM_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiError> handleCartItemNotFound(CartItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("CART_ITEM_NOT_FOUND", ex.getMessage()));
    }
//user
    @ExceptionHandler(InvalidCartItemQuantityException.class)
    public ResponseEntity<ApiError> handleInvalidQuantity(InvalidCartItemQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_CART_QUANTITY", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("DUPLICATE_RESOURCE", ex.getMessage()));
    }




    // ✅ [8] 기타 예외 (마지막 방어선)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        log.error("[서버 오류] {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("RUNTIME_EXCEPTION", "요청 처리 중 오류가 발생했습니다."));
    }
}
