CC = gcc
TARGET =
CFLAGS = -g  
LIBS = -lstdc++
DEP =

all:    $(TARGET)

%.o:    %.cpp
	$(CC) -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o 
	rm -f *.class 
	rm -f $(TARGET)

