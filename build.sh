mkdir bin 2>/dev/null

cp src/*.png bin/

javac7 -Xlint:all,-serial -cp . -s src -d bin src/cnt/{*,*/*,*/*/*}.java
