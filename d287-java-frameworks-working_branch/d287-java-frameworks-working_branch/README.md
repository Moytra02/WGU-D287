# WESTERN GOVERNOR UNIVERSITY 
## D287 – JAVA FRAMEWORKS

C.  Customize the HTML user interface for your customer’s application. The user interface should include the shop name, the product names, and the names of the parts.
<pre>
mainscreen.html - line 14 -> Changed title to "My Skate Shop"
mainscreen.html - line 19 -> Changed < h1> to "Skate Shop"
mainscreen.html - line 53 -> Changed < h2> to "Complete Boards"
</pre>
D.  Add an “About” page to the application to describe your chosen customer’s company to web viewers and include navigation to and from the “About” page and the main screen.
<pre>
Created about.html
about.html - line 1-12 -> header and template
about.html - line 11 -> changed title to "About"
about.html - line 15 - 20 -> added the about us section
about.html - line 22 -> created a button to go back to mainscreen.html

mainscreen.html - line 20 -> created a button to go to about.html

created controller GoToAbout.java
</pre>
E.  Add a sample inventory appropriate for your chosen store to the application. You should have five parts and five products in your sample inventory and should not overwrite existing data in the database.
<pre>
BootStrapData.java lines 52 - 138 -> created parts and products
</pre>

F.  Add a “Buy Now” button to your product list. Your “Buy Now” button must meet each of the following parameters:
<pre>
Created confirmationbuyproduct.html

< !DOCTYPE html>
< html lang="en">
< head>
    < meta charset="UTF-8">
    < title>Purchase Confirmation</title>
</head>
< body>
    < h1> Your product has been purchased</h1>
    < a href="mainscreen.html" > Go Home </a>
< /body>
< /html>
</pre>
<pre>
Created errorbuyproduct.html

< !DOCTYPE html>
< html lang="en">
< head>
    < meta charset="UTF-8">
    < title>Purchasing Error</title>
< /head>
< body>
    < h1>Error purchasing product.</h1>
    < a href="mainscreen.html"> Go Home </a>
< /body>
< /html>
</pre>
<pre>
mainscreen.html - lines 89-90 -> 

< a th:href="@{/buyproduct(productID=${tempProduct.id})}" class="btn btn-primary btn-sm mb-3"
onclick="if(!(confirm('Are you sure you want to purchase this product?')))return false" > Buy Now < /a>
</pre>
<pre>
product.java - lines 108-116 -> 

public boolean buyProduct() {
        if (this.inv >= 1 ) {
            this.inv--;
            return true;
        } else {
            return false;
        }
    }
</pre>
<pre>
AddProductController.java - lines 172-182 ->

@GetMapping("/buyproduct")
    public String buyProduct(@RequestParam("productID") int theId, Model theModel ) {
        ProductService productService = context.getBean(ProductServiceImpl.class);
        Product product2 = productService.findById(theId);
        boolean purchaseConfirmation = product2.buyProduct();
        if ( purchaseConfirmation ) {
            productService.save(product2);
            return "confirmationbuyproduct";
        }
        return "errorbuyproduct";
    }
</pre>
G.  Modify the parts to track maximum and minimum inventory by doing the following:
•  Add additional fields to the part entity for maximum and minimum inventory.
<pre>
mainscreen.html - lines 39-40 and lines 48-49 ->

< th>Minimum</th>
< th>Maximum</th>

< td th:text="${tempPart.minimum}">1</td>
< td th:text="${tempPart.maximum}">1</td>
</pre>
•  Modify the sample inventory to include the maximum and minimum fields.
<pre>
Part.java - lines 42-50 ->

@Min (value = 0, message = "Minimum inventory must be > 0")
    int minimum;
    int maximum;

    public void setMinimum(int minimum) { this.minimum = minimum; }
    public int getMinimum() { return this.minimum; }

    public void setMaximum(int maximum) { this.maximum = maximum; }
    public int getMaximum() { return this.maximum; }
</pre>
<pre>
InhousePart.java and Outsourcedpart.java - lines 17-19 ->

this.minimum = 0;
this.maximum = 100;
</pre>
•  Add to the InhousePartForm and OutsourcedPartForm forms additional text inputs for the inventory so the user can set the maximum and minimum values.
<pre>
InhousePartForm.html and OutsourcedPartForm.html - lines 25-36 ->

< p>< input type="text" th:field="*{companyName}" placeholder="Company Name" class="form-control mb-4 col-4"/>< /p>
< p>< input type="text" th:field="*{minimum}" placeholder="Minimum" class="form-control mb-4 col-4"/>< /p>
< p>< input type="text" th:field="*{maximum}" placeholder="Maximum" class="form-control mb-4 col-4"/>< /p>
< p>< input type="text" th:field="*{partId}" placeholder="Part ID" class="form-control mb-4 col-4"/>< /p>
< p>
< div th:if="${#fields.hasAnyErrors()}" >
< ul>< li th:each="err: ${#fields.allErrors()}" th:text="${err}">< /li>< /ul>
< /div>
< /p>
</pre>
•  Rename the file the persistent storage is saved to.
<pre>
application.properties line 6 -> 

changed to "spring.datasource.url=jdbc:h2:file:~/src/main/resources/spring-boot-h2-db102"
</pre>
•  Modify the code to enforce that the inventory is between or at the minimum and maximum value.
<pre>
Part.java - lines 102-108 ->

public void validateLimits() {
        if (this.inv < this.minimum) {
            this.inv = this.minimum;
        } else if (this.inv > this.maximum ) {
            this.inv = this.maximum;
        }
    }
</pre>
<pre>
InhousePartServiceImpl.java and OutsourcedPartServiceImpl.java - line 54 ->

thePart.validateLimits();
</pre>

H.  Add validation for between or at the maximum and minimum fields. The validation must include the following:

<pre>
Part.java - line 21-22 ->

@ValidPartInventory
@ValidPartInventoryMinimum
</pre>

•  Display error messages for low inventory when adding and updating parts if the inventory is less than the minimum number of parts.
<pre>
Created PartInventoryMinimumValidator.java ->

package com.example.demo.validators;
import com.example.demo.domain.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PartInventoryMinimumValidator implements ConstraintValidator < ValidPartInventoryMinimum, Part > {
    @Autowired
    private ApplicationContext context;
    public static  ApplicationContext myContext;
    @Override
    public void initialize(ValidPartInventoryMinimum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(Part part, ConstraintValidatorContext constraintValidatorContext) {
        return part.getInv() > part.getMinimum();
    }
}
</pre>

<pre>
Created ValidPartInventoryMinimum.java ->

package com.example.demo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {PartInventoryMinimumValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPartInventoryMinimum {
    String message() default "Inventory cannot be lower than required minimum";
    Class [] groups() default {};
    Class [] payload() default {};
}
</pre>
•  Display error messages for low inventory when adding and updating products lowers the part inventory below the minimum.
<pre>
***NOTE: There isn't a validator here because the requirements of section F is that the "Buy Now" button should not affect the 
inventory of associated parts. If I were to implement this, I would make changes to the buyProduct() function.

public boolean buyProduct() {
if (this.inv < 1) {
    return false;
}

for (Part part : this.getParts()) {
    if (part.getInv() < 1) {
        return false;
    }
}

this.inv--;
for (Part part : this.getParts()) {
    part.setInv(part.getInv() - 1);
}

return true;
}
I could have also updated the error template to display the validation error that would have been triggered by the PartInventoryMinimumValidator.

</pre>
•  Display error messages when adding and updating parts if the inventory is greater than the maximum.
<pre>
Created PartInventoryValidator.java ->

package com.example.demo.validators;

import com.example.demo.domain.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PartInventoryValidator implements ConstraintValidator < ValidPartInventory, Part > {
    @Autowired
    private ApplicationContext context;
    public static  ApplicationContext myContext;
    @Override
    public void initialize(ValidPartInventory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(Part part, ConstraintValidatorContext constraintValidatorContext) {
        return part.getInv() <= part.getMaximum();
    }
}
</pre>
<pre>
Created ValidPartInventory.java ->

package com.example.demo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = {PartInventoryValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPartInventory {
    String message() default "Inventory cannot exceed maximum value";
    Class [] groups() default {};
    Class [] payload() default {};
}
</pre>

I.  Add at least two unit tests for the maximum and minimum fields to the PartTest class in the test package.
<pre>
PartTest.java - lines 162-178 ->

@Test
    void getMinimum() {
        int minimum=1;
        partIn.setMinimum(minimum);
        assertEquals(minimum, partIn.getMinimum());
        partOut.setMinimum(minimum);
        assertEquals(minimum, partOut.getMinimum());
    }

    @Test
    void getMaximum() {
        int maximum=10;
        partIn.setMaximum(maximum);
        assertEquals(maximum, partIn.getMaximum());
        partOut.setMaximum(maximum);
        assertEquals(maximum, partOut.getMaximum());
    }
</pre>
J.  Remove the class files for any unused validators in order to clean your code.
<pre>
DeletePartValidator.java was removed since it had no references in the project.
</pre>
