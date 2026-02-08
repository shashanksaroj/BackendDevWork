

---

Spring

Annotations / Classes

Annotation / Class	Purpose	Interview Relevance

@RestController	Marks a class as handling web requests.	Explain its role in creating REST controllers.
@RequestMapping	Defines the base URL path for API endpoints.	Explain how to map requests to specific controllers or methods.
@GetMapping, @PostMapping, etc.	Maps HTTP methods to handler methods.	Explain different HTTP methods and their corresponding annotations.
@PathVariable	Extracts values from the URL path.	Explain how to extract dynamic values from URLs.
@RequestBody	Binds the request body to a Java object.	Explain how to process incoming data (usually JSON).
ResponseEntity	Represents the HTTP response (status codes, body).	Explain how to control HTTP responses, including status codes (200, 201, 400, 404, 500) and response bodies. Important for handling errors gracefully.



---

Example (✨): Improved with error handling

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) { // ? = flexible response
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(
                    ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Product not found")
                ); // More descriptive error
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody @Valid Product product) { // Use @Valid for input validation

        Product createdProduct =
                productService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }
}


---

