package holywars.server;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private static final JavaClasses SERVER_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("holywars.server");

    @Test
    void onlyEntityAndRepositoryPackagesDependOnThePersistenceFramework() {
        noClasses().that().resideOutsideOfPackages("holywars.server..entity..", "holywars.server..repository..")
                .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence..", "org.springframework.data..")
                .because("the persistence framework stays inside each feature's entity and repository")
                .check(SERVER_CLASSES);
    }

    @Test
    void controllersAndViewsDoNotDependOnPersistence() {
        noClasses().that().resideInAnyPackage("holywars.server..controller..", "holywars.server..view..")
                .should().dependOnClassesThat().resideInAnyPackage("holywars.server..entity..", "holywars.server..repository..")
                .because("controllers and views work with the domain, not with how it is persisted")
                .check(SERVER_CLASSES);
    }
}
