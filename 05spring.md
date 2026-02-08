


---

🔹 STEP 3: REST API Design (Spring Boot)


---

1️⃣ How a REST Request Flows (YOU MUST SAY THIS)

Spring MVC flow:

Client
 → DispatcherServlet
   → Controller
     → Service
       → Repository
     ← DTO
 ← Response

🔥 Interview gold

> “Controller is thin, service holds business logic, repository only talks to DB.”




---

2️⃣ Controller Layer (THIN BY DESIGN)

❌ What juniors do

Business logic in controller

Entity returned directly


✅ What SDE-2 does

Controller = request/response mapping only

Uses DTOs



---

3️⃣ Production-Grade User API (CODE)

📁 Structure

controller/
service/
repository/
dto/
entity/


---

4️⃣ Entity (DB Layer Only)

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}

🚫 Never return this from controller


---

5️⃣ DTO (API Contract)

public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

🔥 Interview line:

> “DTO decouples API contract from persistence model.”




---

6️⃣ Repository

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}


---

7️⃣ Service Layer (ALL LOGIC HERE)

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}


---

8️⃣ Controller (CLEAN & SHORT)

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}


---

9️⃣ Exception Handling (MANDATORY)

❌ Don’t do try-catch in controller

✅ Global Exception Handler

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }
}

🔥 Interview line:

> “Centralized exception handling keeps controllers clean.”




---

🔥 COMMON INTERVIEW QUESTIONS (STEP 3)

❓ Why not return JPA entities?

Answer

Lazy loading issues

Security risk

Tight DB coupling



---

❓ DTO vs Entity?

Answer

Entity → persistence

DTO → API contract



---

❓ Why ResponseEntity?

Answer

Full HTTP control (status, headers, body)



---

🧠 SDE-2 DESIGN RULES (MEMORIZE)

✔ Thin controllers
✔ Fat services
✔ DTO everywhere
✔ No business logic in controllers
✔ No entity exposure


---

✅ YOU ARE DONE WITH STEP 3 WHEN

You can design API without coding

You instinctively create DTOs

Your controller has < 20 lines



---
