package holywars.server;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    @Test
    void onlyJpaPrefixedClassesDependOnThePersistenceFramework() {
        JavaClasses serverClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("holywars.server");

        noClasses().that().haveSimpleNameNotStartingWith("Jpa")
                .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence..", "org.springframework.data..")
                .because("the persistence framework stays inside the Jpa implementations of each feature")
                .check(serverClasses);
    }
}
