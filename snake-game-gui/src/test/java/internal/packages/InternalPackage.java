package internal.packages;

import com.tngtech.archunit.core.domain.JavaClass;
import io.vavr.collection.Array;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class InternalPackage {
    private final String internalPackageName;

    static InternalPackage of(JavaClass javaClass) {
        String packageName = javaClass.getPackageName();
        if (isInternal(packageName))
            return new InternalPackage(trimToInternal(packageName));
        else
            throw new RuntimeException("class " + javaClass.getName() + " is not in internal package");
    }

    boolean containsOrIsAdjacentTo(JavaClass javaClass) {
        return contains(javaClass) || isAdjacentTo(javaClass);
    }

    private boolean contains(JavaClass javaClass) {
        return javaClass.getPackageName().startsWith(internalPackageName);
    }

    private boolean isAdjacentTo(JavaClass javaClass) {
        return parentOf(internalPackageName).equals(javaClass.getPackageName());
    }

    private String parentOf(String internalPackageName) {
        return splitByDots(internalPackageName)
                .dropRightWhile("internal"::equals)
                .mkString(".");
    }

    private static boolean isInternal(String packageName) {
        return splitByDots(packageName).contains("internal");
    }

    private static Array<String> splitByDots(String packageName) {
        String[] split = packageName.split("\\.");
        return Array.of(split);
    }

    private static String trimToInternal(String packageName) {
        return splitByDots(packageName)
                .dropRightUntil("internal"::equals)
                .mkString(".");
    }
}