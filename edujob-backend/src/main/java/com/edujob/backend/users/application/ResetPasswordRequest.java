package com.edujob.backend.users.application;

public record ResetPasswordRequest(String otp, String newPin) {}