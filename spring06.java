


---

🔹 STEP 4: Exception Handling & Validation (Spring Boot)


---

1️⃣ Why Exception Handling Matters (Say This in Interview)

> “In production, failures are normal. APIs must return consistent, meaningful error responses, not stack traces.”



🔥 This line scores points.


---

2️⃣ Custom Exception (DO THIS ALWAYS)

❌ Bad

throw new RuntimeException("User not found");

✅ Good

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}


---

3️⃣ Service Layer Throws Exception (NOT Controller)

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + id)
            );

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}

🔥 Interview line:

> “Services throw domain exceptions; controllers stay clean.”




---

4️⃣ Global Exception Handler (PRODUCTION STYLE)

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }
}


---

5️⃣ Standard Error Response (VERY IMPORTANT)

public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

🔥 Interview line:

> “Standard error format helps frontend and monitoring systems.”




---

6️⃣ Validation (REQUEST LEVEL)

DTO with Validation Annotations

public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    private String email;
}


---

7️⃣ Controller Uses @Valid

@PostMapping
public ResponseEntity<String> createUser(
        @Valid @RequestBody CreateUserRequest request) {

    return ResponseEntity.ok("User created");
}


---

8️⃣ Handle Validation Errors (MOST ASKED)

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidation(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error ->
          errors.put(error.getField(), error.getDefaultMessage())
      );

    return ResponseEntity
        .badRequest()
        .body(errors);
}

Sample Response

{
  "name": "Name is required",
  "email": "Invalid email"
}


---

🔥 COMMON INTERVIEW QUESTIONS (STEP 4)

❓ Why not try-catch in controller?

Answer

Breaks separation of concerns

Duplicates code

Hard to maintain



---

❓ Where should validation happen?

Answer

Request DTOs

Not entities

Not controllers



---

❓ Difference: @ControllerAdvice vs @ExceptionHandler?

Answer

@ControllerAdvice → global

@ExceptionHandler → local



---

🧠 SDE-2 RULES (MEMORIZE)

✔ Custom exceptions
✔ Global handler
✔ Standard error format
✔ Validation at boundary
✔ No stack traces to client


---

✅ YOU ARE DONE WITH STEP 4 WHEN

You can design error responses

You never throw RuntimeException randomly

Validation errors are readable



---

