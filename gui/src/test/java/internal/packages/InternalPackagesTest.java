package internal.packages;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InternalPackagesTest {

//    @Test
    public void test() {
        classes()
                .that()
                .resideInAPackage("..internal..")
                .should(beAccessedOnlyByClassesResidingInsideParentOfThisPackage())
                .check(forTheWholeProject());
    }

    private ArchCondition<JavaClass> beAccessedOnlyByClassesResidingInsideParentOfThisPackage() {
        return new InternalPackageArchCondition();
    }

    private JavaClasses forTheWholeProject() {
        return new ClassFileImporter().importPackages("..");
    }
}