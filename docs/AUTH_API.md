# Auth API

Base URL local:

```text
http://localhost:8083/api
```

Tat ca response JSON dung wrapper chung:

```json
{
  "success": true,
  "message": "...",
  "data": null
}
```

Luu y quan trong voi HttpOnly cookie:

- Backend set token bang header `Set-Cookie`.
- Frontend khong doc `accessToken` / `refreshToken` tu JSON body.
- Frontend phai gui request voi cookie:

```js
fetch(url, {
  method: "POST",
  credentials: "include",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(payload)
});
```

Axios:

```js
axios.post(url, payload, { withCredentials: true });
```

## Flow Tong Quan

### Register Flow

```text
POST /auth/send-otp
-> user nhan OTP qua email
-> POST /auth/verify-otp
-> backend set Redis flag email-verified:<email>
-> POST /auth/register
-> backend tao user, set HttpOnly accessToken cookie
```

Backend da enforce OTP:

- `register` se fail neu email chua verify OTP.
- Sau khi register thanh cong, backend xoa flag `email-verified:<email>`.

### Login Flow

```text
POST /auth/login
-> backend validate email/password
-> backend set HttpOnly accessToken cookie
-> neu rememberMe=true thi set them refreshToken cookie
```

### Refresh Flow

```text
POST /auth/refresh-token
-> backend doc refreshToken tu HttpOnly cookie
-> tao accessToken/refreshToken moi
-> set lai cookie moi
```

### Forgot Password Flow

```text
POST /auth/forgot-password
-> backend gui reset link qua email
-> user mo link tren FE
-> FE lay resetToken tu query string
-> POST /auth/reset-password
```

### Protected API Flow

```text
Frontend gui request voi credentials/include
-> browser tu gui cookie accessToken
-> JwtAuthenticationFilter doc accessToken tu cookie
-> set SecurityContext
```

## APIs

## 1. Send OTP

Gui OTP de xac thuc email truoc khi dang ky.

```http
POST /auth/send-otp
```

Request body:

```json
{
  "email": "user@example.com"
}
```

Validation:

- `email`: required, valid email format.

Success response:

```json
{
  "success": true,
  "message": "Send OTP success",
  "data": {
    "message": "Send OTP success"
  }
}
```

Flow backend:

- Kiem tra email chua ton tai.
- Tao OTP bang `SecureRandom`.
- Gui OTP qua email.
- Luu Redis:

```text
otp:<email>
```

Ghi chu:

- API khong tra OTP ve FE.
- Neu email da ton tai, backend tra loi voi loi conflict.

## 2. Verify OTP

Xac thuc OTP va danh dau email da duoc verify.

```http
POST /auth/verify-otp
```

Request body:

```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

Validation:

- `email`: required, valid email format.
- `otp`: required.

Success response:

```json
{
  "success": true,
  "message": "Verify OTP success",
  "data": null
}
```

Flow backend:

- Kiem tra OTP voi Redis key:

```text
otp:<email>
```

- Neu OTP dung:
  - Xoa `otp:<email>`.
  - Tao Redis flag:

```text
email-verified:<email> = true
```

- TTL cua flag hien tai: 10 phut.

Frontend next step:

```text
POST /auth/register
```

## 3. Register

Dang ky user moi. API nay bat buoc email da verify OTP.

```http
POST /auth/register
```

Request body:

```json
{
  "email": "user@example.com",
  "password": "123456",
  "firstName": "John",
  "lastName": "Doe"
}
```

Validation:

- `email`: required, valid email format, size 5-100.
- `password`: required, size 6-10.
- `firstName`: required, size 3-6.
- `lastName`: required, size 3-6.

Success response:

```json
{
  "success": true,
  "message": "Register success",
  "data": null
}
```

Success headers:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
```

Flow backend:

- Kiem tra email chua ton tai.
- Kiem tra Redis flag:

```text
email-verified:<email>
```

- Neu khong co flag hoac flag het han, register fail.
- Tao user voi role `CUSTOMER`.
- Xoa Redis flag `email-verified:<email>`.
- Tao `accessToken`.
- Set HttpOnly cookie `accessToken`.

Ghi chu FE:

- Khong can doc token.
- Sau khi register thanh cong, FE co the redirect vao app nhu trang thai da login.

## 4. Login

Dang nhap bang email/password.

```http
POST /auth/login
```

Request body:

```json
{
  "email": "user@example.com",
  "password": "123456",
  "rememberMe": true
}
```

Validation:

- `email`: required.
- `password`: required.
- `rememberMe`: boolean.

Success response:

```json
{
  "success": true,
  "message": "Login success",
  "data": null
}
```

Success headers:

Neu `rememberMe=false`:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
```

Neu `rememberMe=true`:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
Set-Cookie: refreshToken=...; HttpOnly; Path=/; ...
```

Flow backend:

- Tim user theo email.
- Check password.
- Tao `accessToken`.
- Neu `rememberMe=true`:
  - Tao `refreshToken`.
  - Luu refresh token vao Redis.
  - Set `refreshToken` cookie.
- Set `accessToken` cookie.

## 5. Refresh Token

Cap lai token bang HttpOnly `refreshToken` cookie.

```http
POST /auth/refresh-token
```

Request body:

```text
Khong can body.
```

Success response:

```json
{
  "success": true,
  "message": "Refresh token success",
  "data": null
}
```

Success headers:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
Set-Cookie: refreshToken=...; HttpOnly; Path=/; ...
```

Flow backend:

- Doc `refreshToken` tu cookie.
- Validate JWT refresh token.
- Check refresh token co hop le trong Redis.
- Tao `accessToken` moi.
- Tao `refreshToken` moi.
- Luu refresh token moi vao Redis.
- Set lai cookie moi.

Ghi chu FE:

- Goi API nay voi `credentials: "include"`.
- Neu bi `401`, redirect ve login.

## 6. Forgot Password

Gui email reset password.

```http
POST /auth/forgot-password
```

Request body:

```json
{
  "email": "user@example.com"
}
```

Validation:

- `email`: required, valid email format.

Success response:

```json
{
  "success": true,
  "message": "Forgot password success",
  "data": null
}
```

Flow backend:

- Tim user theo email.
- Tao reset password JWT token.
- Gui email reset password voi link:

```text
http://localhost:5173/auth/reset-password?resetToken=<token>
```

- Luu Redis:

```text
reset-password:<email>
```

- TTL lay theo `security.jwt.reset-expiration`.

## 7. Reset Password

Dat lai mat khau bang reset token trong email.

```http
POST /auth/reset-password
```

Request body:

```json
{
  "token": "reset-token-from-email",
  "newPassword": "123456"
}
```

Validation:

- `token`: required.
- `newPassword`: required, size 6-10.

Success response:

```json
{
  "success": true,
  "message": "Reset password success",
  "data": null
}
```

Flow backend:

- Decode reset token lay `userId`.
- Tim user.
- Check token co khop Redis key khong:

```text
reset-password:<email>
```

- Neu hop le:
  - Xoa `reset-password:<email>`.
  - Encode password moi.
  - Save user.

## 8. Change Password

Doi mat khau khi user dang login.

```http
POST /auth/change-password
```

Request body:

```json
{
  "email": "user@example.com",
  "currentPassword": "old123",
  "newPassword": "new123",
  "rePassword": "new123"
}
```

Validation:

- `email`: required.
- `currentPassword`: required.
- `newPassword`: required.
- `rePassword`: required.

Success response:

```json
{
  "success": true,
  "message": "Change password success",
  "data": null
}
```

Success headers:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
```

Flow backend:

- Lay current user tu `SecurityContext`.
- Check mat khau hien tai.
- Check `newPassword` va `rePassword` trung nhau.
- Save password moi.
- Tao `accessToken` moi.
- Set lai `accessToken` cookie.

Ghi chu FE:

- API nay can login cookie.
- Goi voi `credentials: "include"`.

## 9. Google Login

Dang nhap Google bang Google ID token lay tu frontend.

```http
POST /auth/google
```

Request body:

```json
{
  "idToken": "google-id-token"
}
```

Validation:

- `idToken`: required.

Success response:

```json
{
  "success": true,
  "message": "Google login success",
  "data": null
}
```

Success headers:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
Set-Cookie: refreshToken=...; HttpOnly; Path=/; ...
```

Flow backend:

- Verify `idToken` voi Google.
- Lay email/provider id tu Google payload.
- Neu user/provider link da co thi login user do.
- Neu chua co thi tao user/link provider.
- Check account dang active.
- Tao `accessToken` va `refreshToken`.
- Luu refresh token vao Redis.
- Set HttpOnly cookie.

## 10. Google Callback

Callback OAuth2 Google bang authorization code.

```http
GET /auth/google/callback?code=<authorization-code>
```

Request query:

```text
code=<authorization-code>
```

Success response:

```text
Khong tra JSON. Backend redirect.
```

Success headers:

```http
Set-Cookie: accessToken=...; HttpOnly; Path=/; ...
Set-Cookie: refreshToken=...; HttpOnly; Path=/; ...
Location: http://localhost:5173/auth/google-callback?success=true
```

Error redirect:

```text
http://localhost:5173/auth/google-callback?error=<message>
```

Flow backend:

- Nhan Google authorization `code`.
- Exchange code lay Google token.
- Verify Google ID token.
- Tao hoac link user.
- Tao app tokens.
- Set HttpOnly cookie.
- Redirect ve frontend.

## 11. Logout

Dang xuat va clear auth cookies.

```http
POST /auth/logout
```

Request body:

```text
Khong can body.
```

Success response:

```json
{
  "success": true,
  "message": "Logout success",
  "data": null
}
```

Success headers:

```http
Set-Cookie: accessToken=; Max-Age=0; HttpOnly; Path=/; ...
Set-Cookie: refreshToken=; Max-Age=0; HttpOnly; Path=/; ...
```

Flow backend:

- Clear `accessToken` cookie.
- Clear `refreshToken` cookie.

Ghi chu FE:

- Goi voi `credentials: "include"` de browser gui cookie hien tai va nhan clear-cookie headers.

## Error Responses

Validation error:

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "email": "Email invalid"
  }
}
```

Common error:

```json
{
  "success": false,
  "message": "..."
}
```

HTTP status dang dung:

- `400`: business/validation error.
- `401`: invalid or missing auth token.
- `404`: resource not found.
- `409`: duplicate resource, vi du email da ton tai.
- `500`: unexpected server error.

