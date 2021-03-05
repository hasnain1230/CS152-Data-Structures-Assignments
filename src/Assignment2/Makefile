# java compiler
JAVAC = javac

# java runtime
JAVA = java

# source directory, where all the java files are located
SRCDIR = poly

# output directory, where all the class files will be 
OUTDIR = bin

# compilation flags
JFLAGS = -d $(OUTDIR)/

RM = rm

all:
	$(JAVAC) $(JFLAGS) poly/*.java

run:
	$(JAVA) -cp $(OUTDIR) poly.Polytest

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) $(OUTDIR)/poly/*.class
