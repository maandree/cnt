mkdir bin 2>/dev/null

cp src/*.png bin/
cp src/*.png ./

#javac7 -Xlint:all,-serial -cp . -s src -d bin src/cnt/{*,*/*,*/*/*}.java
javac7 -Xlint:all,-serial -cp . -s src -d bin src/cnt/{*,{d,g,m,t,u}*/*,*/*/*,n*/{B,O}*}.java