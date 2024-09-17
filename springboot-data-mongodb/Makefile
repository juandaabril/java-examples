# Define the Gradle command
GRADLE := ./gradlew

# Define targets
.PHONY: all build test integration-test clean

# Default target
all: build test integration-test

# Build the project
build:
	$(GRADLE) assemble

# Run unit tests
test:
	$(GRADLE) test

# Run integration tests
integration-test:
	$(GRADLE) integrationTest

# Clean the project
clean:
	$(GRADLE) clean