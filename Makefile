CHANGELOG_PATH:=src/main/resources/db/changelog/changes
CHANGELOG_TIMESTAMP:=$(shell date +"%Y%m%d_%H%M%S")

db-diff:
	mvn clean install liquibase:diff \
	-DskipTests=true \
	-Dliquibase.diffChangeLogFile=$(CHANGELOG_PATH)/$(CHANGELOG_TIMESTAMP)_changelog.yaml

db-rollback:
	@if [ -z "$(count)" ]; then \
    	  echo "Error: 'count' is not defined. Usage: make db-rollback count=<number>"; \
    	  exit 1; \
    fi
	mvn clean install liquibase:rollback -DskipTests=true -Dliquibase.rollbackCount=$(count)

db-migrate:
	@if [ -z "$(version)" ]; then \
		echo "Migrating to latest version..."; \
		mvn clean install liquibase:update -DskipTests=true; \
	else \
		echo "Migrating to version: $(version)"; \
		mvn clean install liquibase:update -DskipTests=true -Dliquibase.toTag=$(version); \
	fi
