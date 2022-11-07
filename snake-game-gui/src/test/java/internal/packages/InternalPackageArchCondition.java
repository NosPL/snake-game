package internal.packages;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

class InternalPackageArchCondition extends ArchCondition<JavaClass> {

    InternalPackageArchCondition() {
        super("be accessed only by the classes adjacent to the internal package");
    }

    @Override
    public void check(JavaClass javaClassFromInternalPackage, ConditionEvents conditionEvents) {
        javaClassFromInternalPackage
                .getDirectDependenciesToSelf()
                .stream()
                .map(this::toConditionEvent)
                .forEach(conditionEvents::add);
    }

    private ConditionEvent toConditionEvent(Dependency dependency) {
        return toConditionEvent(dependency.getTargetClass(), dependency.getOriginClass());
    }

    private ConditionEvent toConditionEvent(JavaClass targetClass, JavaClass clientClass) {
        boolean conditionMet = InternalPackage.of(targetClass).containsOrIsAdjacentTo(clientClass);
        String conditionNotMetMessage = conditionNotMetMessage(targetClass, clientClass);
        return new SimpleConditionEvent(targetClass, conditionMet, conditionNotMetMessage);
    }

    private String conditionNotMetMessage(JavaClass targetClass, JavaClass clientClass) {
        String targetClassName = targetClass.getFullName();
        String clientClassName = clientClass.getFullName();
        return "class " + targetClassName + " should not be accessed by " + clientClassName;
    }
}