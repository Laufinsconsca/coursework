package ui;

import enums.Item;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IndexAnnotated
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface AutoInitializableController {
    /**
     * @return general parameters
     */
    String name();

    String parentController() default "InitController";

    Item type();

    /**
     * @return controller parameters
     */
    String pathFXML() default "";
}