mkdir bin 2>/dev/null

cp src/*.png bin/
cp src/*.png ./

dia Documentation/CNT_class_diagram.dia -e Documentation/CNT_class_diagram.pdf

jars="cling-core-1.0.5.jar:cling-support-1.0.5.jar:teleal-common-1.0.13.jar"


#javac7 -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1 | less
#javac7  -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,{d,g,t,u}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1 | less
#javac7 -cp .:$jars -s src -d bin src/cnt/{*,{d,g,t,u}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1 | less

#javac7 -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1
javac7  -Xlint:all,-serial -cp .:$jars -s src -d bin src/cnt/{*,{d,g,t,u}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1
#javac7 -cp .:$jars -s src -d bin src/cnt/{*,{d,g,t,u}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1
