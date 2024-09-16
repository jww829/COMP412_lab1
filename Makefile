JAR = jar
JAVAC = javac
SRC_DIR = src/FrontEnd
BIN_DIR = bin
JAR_FILE = lab1.jar          
SOURCES = $(wildcard $(SRC_DIR)/*.java)   # Find all .java files in the FrontEnd directory

# Build: compiles all .java files into .class files in the bin directory
build:
	@mkdir -p $(BIN_DIR) 
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)
	$(JAR) cvf $(JAR_FILE) -C $(BIN_DIR) .

# Clean: removes all .class files
clean:
	rm -rf $(BIN_DIR)
	rm -f lab1.jar
