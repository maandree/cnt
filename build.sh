mkdir bin 2>/dev/null

cp src/*.png bin/
cp src/*.png ./

jars="cling-core-1.0.5.jar:cling-support-1.0.5.jar:teleal-common-1.0.13.jar"


#javac7 -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1 | less
#javac7  -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,{d,g,m,t,u}*/*,*/*/*,n*/{B,O}*}.java 2>&1 | less
#javac7 -cp .:$jars -s src -d bin src/cnt/{*,{d,g,m,t,u}*/*,*/*/*,n*/{B,O}*}.java 2>&1 | less

#javac7 -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1
javac7  -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,{d,g,m,t,u}*/*,*/*/*,n*/{B,O}*}.java 2>&1
#javac7 -cp .:$jars -s src -d bin src/cnt/{*,{d,g,m,t,u}*/*,*/*/*,n*/{B,O}*}.java 2>&1
